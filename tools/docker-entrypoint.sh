#!/usr/bin/env bash

# load .env
eval "$(cat .env <(echo) <(declare -x))"

_build() {
    ./gradlew build --no-daemon --parallel
}

_help() {
    printf "Usage: docker run ... <target>\n"
    printf "\n"
    printf "Arguments:\n"
    printf "\t<target>\ttarget of publication (\"ghr\" or \"modrinth\")\n"
}

_ghr() {
    local ver
    ver=$(<buildSrc/src/main/kotlin/**/Constants.kt grep "const val VERSION = " | sed 's/.* = "\(.*\)"/\1/')
    echo "$GITHUB_TOKEN" | gh auth login --with-token
    gh release create "v$ver" --draft --title "v$ver"
}

_modrinth() {
    ./gradlew modrinth --no-daemon --parallel
}

if [[ $# != 1 ]]; then
    _help
    exit 1
fi
case "$1" in
ghr)
    _build
    _ghr
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
exit 0
