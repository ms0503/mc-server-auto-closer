#!@bash@
# shellcheck disable=SC2239
set -euo pipefail
shopt -s globstar

_help() {
    printf "Usage: %s <dotenv_file> <target>\n" "$0"
    printf "\n"
    printf "Arguments:\n"
    printf "\t<dotenv_file>\tpath to .env file\n"
    printf "\t<target>\ttarget of publication (\"ghr\" or \"modrinth\")\n"
}

_ghr() {
    local token
    local ver
    token=$1
    ver=$(
        <buildSrc/src/main/kotlin/**/Constants.kt grep "const val VERSION = " | sed 's/.* = "\(.\+\)"/\1/'
    )
    echo "$token" | gh auth login --with-token
    gh release create "v$ver" --draft --title "v$ver"
}

_maven() {
    local endpoint
    local bucket
    local access_key
    local secret_key
    endpoint=$1
    bucket=$2
    access_key=$3
    secret_key=$4
    ./gradlew publishMavenPublicationToBuildRepository \
        --no-daemon \
        --no-parallel
    export AWS_ACCESS_KEY_ID=$access_key
    export AWS_SECRET_ACCESS_KEY=$secret_key
    aws s3 sync build/repo "s3://$bucket" \
        --endpoint-url "$endpoint" \
        --exclude '*.asc' \
        --exclude '*.md5' \
        --exclude '*.module' \
        --exclude '*.sha1' \
        --exclude '*.sha256' \
        --exclude '*.sha512'
    aws s3 sync build/repo "s3://$bucket" \
        --endpoint-url "$endpoint" \
        --exclude '*' \
        --include '*.asc' \
        --include '*.md5' \
        --include '*.sha1' \
        --include '*.sha256' \
        --include '*.sha512' \
        --content-type text/plain
    aws s3 sync build/repo "s3://$bucket" \
        --endpoint-url "$endpoint" \
        --exclude '*' \
        --include '*.module' \
        --content-type application/json
}

_modrinth() {
    ./gradlew modrinth --no-daemon --no-parallel
}

if [[ $# != 2 ]]; then
    _help
    exit 1
fi

# load .env
eval "$(cat "$1" <(echo) <(declare -x))"

case "$2" in
ghr)
    _ghr "$GITHUB_TOKEN"
    ;;
maven)
    _maven \
        "$R2_ENDPOINT_URL" \
        "$R2_BUCKET_NAME" \
        "$R2_ACCESS_KEY" \
        "$R2_SECRET_KEY"
    ;;
modrinth)
    _modrinth
    ;;
*)
    _help
    exit 1
    ;;
esac
