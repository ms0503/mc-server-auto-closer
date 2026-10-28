# MC Server Auto Closer

[![License](https://img.shields.io/badge/License-MIT-00cc00.svg)](LICENSE.md)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/eNYGqJbG?label=Modrinth+downloads&logo=modrinth)](https://modrinth.com/project/eNYGqJbG)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_Forge_%2F_NeoForged-_1.20–1.20.1,1.20.2,1.20.4-blue)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_NeoForged-_1.20.5–1.20.6-blue)

[ja](README.ja.md)

This is a mod that automatically shuts down the Minecraft server if it remains unattended for a set period of time.

## Dependencies

- Minecraft 1.20 - 1.20.1
- Architectury API 9.0.6 or later
- Additional dependencies per mod loader below

### Fabric

- Fabric Loader 0.14.21 or later
- Fabric API 0.83.0+1.20 or later
- Fabric Language Kotlin 1.9.4+kotlin.1.8.21 or later

### Forge

- Forge 1.20-46.0.1 or later
- Kotlin for Forge 4.3.0 or later

## Configuration

Configuration file is `config/mc_server_auto_closer.json`.

### `waitTime`

The time to wait after becoming unmanned until stopping.\
The unit is milliseconds.\
The initial value is `900000` ms (=15 minutes).

### `enableTimeStart`/`enableTimeEnd`

Specify the time to enable automatic stop.\
Specify in `HH:MM:SS` format.\
The default values are `02:00:00` and `08:00:00` respectively.

For example, if `waitTime` is 15 minutes, and the last player leaves 14 minutes before `enableTimeEnd`, automatic stop
will not activate as it will be outside the enabled time by the time `waitTime` expires.\
If you wish to initiate the automatic stop wait check until that time, specify that time plus `waitTime` as
`enableTimeEnd`.
