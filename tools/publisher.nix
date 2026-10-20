{
  bash,
  lib,
  writeScriptBin,
}:
let
  substituteVars = builtins.replaceStrings [ "@bash@" ] [ (lib.getExe bash) ];
in
./publisher.sh
|> builtins.readFile
|> substituteVars
|> writeScriptBin "publisher"
# vim: et sw=2
