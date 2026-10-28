{
  inputs = {
    flake-compat = {
      flake = false;
      url = "github:NixOS/flake-compat";
    };
    flake-parts = {
      inputs.nixpkgs-lib.follows = "nixpkgs";
      url = "github:hercules-ci/flake-parts";
    };
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    systems = {
      flake = false;
      url = "github:nix-systems/default";
    };
  };
  outputs =
    inputs@{ flake-parts, systems, ... }:
    flake-parts.lib.mkFlake { inherit inputs; } {
      perSystem = { self', pkgs, ... }: {
        devShells =
          let
            mkJdkShell =
              jdk:
              pkgs.mkShell {
                JAVA_HOME = jdk;
                packages = [
                  jdk
                  pkgs.gh
                  self'.packages.publisher
                ];
              };
          in
          [
            17 # 1.12.2 1.19 1.20-1.20.4
            21 # 1.20.5-1.20.6
            25 # 1.7.10
          ]
          |> builtins.map builtins.toString
          |> builtins.map (ver: {
            name = "jdk${ver}";
            value = mkJdkShell pkgs."temurin-bin-${ver}";
          })
          |> builtins.listToAttrs;
        packages.publisher = pkgs.callPackage ./tools/publisher.nix { };
      };
      systems = import systems;
    };
}
# vim: et sw=2
