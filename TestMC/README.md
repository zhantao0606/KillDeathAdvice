# Kill Death Advice Mod

一个为 Minecraft Fabric 开发的模组，提供增强的生物状态显示和一击必杀功能。

## 模组介绍

Kill Death Advice Mod 为游戏添加了以下功能：
- 实时显示生物血量
- 玩家一击必杀生物功能
- 玩家死亡时的周边16格内生物提示
- 玩家死亡时显示玩家名称
- 智能的生物状态显示系统

## 安装要求

- Minecraft 1.21.1
- Fabric Loader 0.15.11
- Fabric API 0.102.1+1.21.1
- Java 21

## 安装方法

1. 安装 [Fabric Loader](https://fabricmc.net/use/)
2. 下载并安装 [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
3. 下载本模组的 .jar 文件
4. 将下载的 .jar 文件放入游戏的 `mods` 文件夹中
5. 启动游戏

## 功能说明

### 生物血量显示
- 在生物头顶实时显示血量信息
- 显示格式：`生物名称 ❤当前血量/最大血量`
- 仅在玩家16格范围内显示
- 超出范围自动隐藏显示

### 一击必杀系统
- 玩家可以一击必杀任何非玩家生物
- 保持正常的掉落物品机制
- 不影响玩家之间的战斗

### 死亡通知
- 玩家死亡时显示详细信息
- 显示死亡玩家的名称
- 列出死亡地点16格范围内的所有生物

## 使用说明

### 血量显示
- 靠近生物（16格以内）即可看到血量信息
- 血量信息会随着生物受伤自动更新
- 离开范围后血量显示自动消失

### 一击必杀
- 直接攻击任何非玩家生物即可触发
- 无需特殊操作或命令
- 对所有生物（除玩家外）有效

### 死亡信息
- 玩家死亡时，所有在线玩家都会收到通知
- 通知包含死亡玩家名称和周围生物列表
- 帮助队友了解死亡现场情况

## 注意事项

- 本模组仅影响非玩家生物
- 不会影响服务器的平衡性
- 适用于单人游戏和多人服务器

## 问题反馈

如果您在使用过程中遇到任何问题，请通过以下方式反馈：
- 在 GitHub 上提交 Issue
- 通过邮件联系作者
