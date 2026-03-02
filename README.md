# MC Server Auto Closer

[![License](https://img.shields.io/badge/License-MIT-00cc00.svg)](LICENSE.md)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/eNYGqJbG?label=Modrinth+downloads&logo=modrinth)](https://modrinth.com/project/eNYGqJbG)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_Forge_%2F_NeoForged-_1.20–1.20.1,1.20.2,1.20.4-blue)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_NeoForged-_1.20.5–1.20.6-blue)

This is a mod that automatically shuts down the Minecraft server if it remains unattended for a set period of time.

Minecraftサーバーが無人状態で一定時間経過した場合に、自動でサーバーを停止させるMODです。

## Dependencies / 依存関係

- Minecraft 1.20.6
- Architectury API 12.0.27 or later
- Additional dependencies per mod loader below / 以下のMODローダーごとの追加依存

### Fabric

- Fabric Loader 0.14.24 or later
- Fabric API 0.97.8+1.20.6 or later
- Fabric Language Kotlin 1.11.0+kotlin.2.0.0 or later

### NeoForged

- NeoForged 20.6.1-beta or later
- Kotlin for Forge 5.0.0 or later

## Configuration / 設定

Configuration file is `config/mc_server_auto_closer.json`.

設定ファイルは`config/mc_server_auto_closer.json`です。

### `waitTime`

The time to wait after becoming unmanned until stopping.\
The unit is milliseconds.\
The initial value is `900000` ms (=15 minutes).

無人状態になってから停止させるまで待機する時間です。\
単位はmsです。\
初期値は`900000`ms(=15分)です。

### `enableTimeStart`/`enableTimeEnd`

Specify the time to enable automatic stop.\
Specify in `HH:MM:SS` format.\
The default values are `02:00:00` and `08:00:00` respectively.

For example, if `waitTime` is 15 minutes, and the last player leaves 14 minutes before `enableTimeEnd`, automatic stop
will not activate as it will be outside the enabled time by the time `waitTime` expires.\
If you wish to initiate the automatic stop wait check until that time, specify that time plus `waitTime` as
`enableTimeEnd`.

自動停止を有効化する時間を指定します。\
`HH:MM:SS`で指定します。\
初期値はそれぞれ`02:00:00`/`08:00:00`です。

例えば、`waitTime`が15分だとした時、`enableTimeEnd`の14分前に最後のプレイヤーが抜けた場合、`waitTime`
が満了する頃には有効化時間外であるため自動停止は働きません。\
「その時間まで自動停止待機開始判定をさせたい」場合には、その時間+`waitTime`を`enableTimeEnd`に指定してください。
