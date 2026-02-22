# MC Server Auto Closer

Minecraftサーバーが無人状態で一定時間経過した場合に、自動でサーバーを停止させるMODです。

## 依存関係

- Minecraft 1.20.1
- Architectury API 9.2.14
- 以下、MODローダーごとの追加依存

### Fabric

- Fabric API 0.92.7+1.20.1
- Fabric Language Kotlin 1.13.9+kotlin.2.3.10

### Forge

- Forge 1.20.1-47.4.16
- Kotlin for Forge 4.11.0

## 設定

設定ファイルは`config/mc_server_auto_closer.json`です。

### `waitTime`

無人状態になってから停止させるまで待機する時間です。\
単位はmsです。\
初期値は`900000`ms(=15分)です。

### `enableTimeStart`/`enableTimeEnd`

自動停止を有効化する時間を指定します。\
`HH:MM:SS`で指定します。\
初期値はそれぞれ`02:00:00`/`08:00:00`です。

例えば、`waitTime`が15分だとした時、`enableTimeEnd`の14分前に最後のプレイヤーが抜けた場合、`waitTime`が満了する頃には有効化時間外であるため自動停止は働きません。\
「その時間まで自動停止待機開始判定をさせたい」場合には、その時間+`waitTime`を`enableTimeEnd`に指定してください。
