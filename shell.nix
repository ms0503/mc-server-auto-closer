(import (
  let
    lock = ./flake.lock |> builtins.readFile |> builtins.fromJSON;
    nodeName = lock.nodes.root.inputs.flake-compat;
  in
  fetchTarball {
    sha256 = lock.nodes.${nodeName}.locked.narHash;
    url =
      lock.nodes.${nodeName}.locked.url
        or "https://github.com/NixOS/flake-compat/archive/${lock.nodes.${nodeName}.locked.rev}.tar.gz";
  }
) { src = ./.; }).shellNix
# vim: et sw=2
