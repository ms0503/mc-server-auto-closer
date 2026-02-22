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
初期値は900000ms(=15分)です。
