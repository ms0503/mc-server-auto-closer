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
          {
            jdk17 = mkJdkShell pkgs.temurin-bin-17;
            jdk21 = mkJdkShell pkgs.temurin-bin-21;
            jdk8 = mkJdkShell pkgs.temurin-bin-8;
          };
        packages.publisher = pkgs.callPackage ./tools/publisher.nix { };
      };
      systems = import systems;
    };
}
# vim: et sw=2
