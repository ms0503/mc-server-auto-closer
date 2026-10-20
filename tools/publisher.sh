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

_build() {
    ./gradlew build --no-daemon --parallel
}

_ghr() {
    local token
    local ver
    token=$1
    ver=$(<buildSrc/src/main/kotlin/**/Constants.kt grep "const val VERSION = " | sed 's/.* = "\(.\+\)"/\1/')
    echo "$token" | gh auth login --with-token
    gh release create "v$ver" --draft --title "v$ver"
}

_modrinth() {
    ./gradlew modrinth --no-daemon --parallel
}

if [[ $# != 2 ]]; then
    _help
    exit 1
fi

# load .env
eval "$(cat "$1" <(echo) <(declare -x))"

case "$2" in
ghr)
    _build
    _ghr "$GITHUB_TOKEN"
    ;;
modrinth)
    _build
    _modrinth
    ;;
*)
    _help
    exit 1
    ;;
esac
