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
        devShells = {
          jdk17 = pkgs.mkShell {
            JAVA_HOME = pkgs.temurin-bin-17;
            packages = builtins.concatLists [
              (with pkgs; [
                gh
                temurin-bin-17
              ])
              (with self'.packages; [
                publisher
              ])
            ];
          };
          jdk21 = pkgs.mkShell {
            JAVA_HOME = pkgs.temurin-bin-21;
            packages = builtins.concatLists [
              (with pkgs; [
                gh
                temurin-bin-21
              ])
              (with self'.packages; [
                publisher
              ])
            ];
          };
        };
        packages.publisher = pkgs.callPackage ./tools/publisher.nix { };
      };
      systems = import systems;
    };
}
# vim: et sw=2
