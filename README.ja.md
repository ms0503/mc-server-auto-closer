# MC Server Auto Closer

[![License](https://img.shields.io/badge/License-MIT-00cc00.svg)](LICENSE.md)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/eNYGqJbG?label=Modrinth+downloads&logo=modrinth)](https://modrinth.com/project/eNYGqJbG)
![Supported platforms](https://img.shields.io/badge/Forge-1.7.10,1.12.2-blue)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_Forge-1.16.5,1.17.1,1.18–1.18.2,1.19–1.19.4-blue)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_Forge_%2F_NeoForged-1.20–1.20.2,1.20.4-blue)
![Supported platforms](https://img.shields.io/badge/Fabric_%2F_NeoForged-1.20.5–1.20.6-blue)

[en](https://github.com/ms0503/mc-server-auto-closer/blob/HEAD/README.md)

Minecraftサーバーが無人状態で一定時間経過した場合に、自動でサーバーを停止させるMODです。

## 依存関係

- Minecraft 1.20.2
- 以下のMODローダーごとの追加依存

### Fabric

- Fabric Loader 0.14.24〜
- Fabric API 0.89.0+1.20.2〜
- Fabric Language Kotlin 1.10.10+kotlin.1.9.10〜
- Architectury API 10.0.7〜

### Forge

- Forge 1.20.2-48.0.0〜
- Kotlin for Forge 4.4.0〜
- Architectury API 10.0.7〜

### NeoForged

- NeoForged 20.2.3-beta〜
- Kotlin for Forge 4.4.0〜
- Architectury API 10.0.11〜

## 設定

設定ファイルは`config/mc_server_auto_closer.json`です。

### `waitTime`

無人状態になってから停止させるまで待機する時間です。\
単位はmsです。\
初期値は`900000`ms (=15分)です。

### `enableTimeStart`/`enableTimeEnd`

自動停止を有効化する時間を指定します。\
`HH:MM:SS`で指定します。\
初期値はそれぞれ`02:00:00`/`08:00:00`です。

例えば、`waitTime`が15分だとした時、`enableTimeEnd`の14分前に最後のプレイヤーが抜けた場合、`waitTime`
が満了する頃には有効化時間外であるため自動停止は働きません。\
「その時間まで自動停止待機開始判定をさせたい」場合には、その時間+`waitTime`を`enableTimeEnd`に指定してください。
