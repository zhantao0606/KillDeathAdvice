1. # Minecraft模组开发测试任务

   ## 项目概述
   
   这是一个Minecraft模组开发的实践任务，通过完成一系列开发任务来提升您的模组开发能力。整个过程欢迎使用AI工具辅助完成。
   
   ## 开发环境准备
   
   ### Java环境要求

   任务中需要使用两个不同的Java版本：
   
   - CarianStyle模组（任务2）：需要JDK 8
   - Fabric模组（任务3）：需要JDK 21
   
   ### JDK安装指南
   
   1. **JDK 8**：
      - 下载链接：[Java SE Development Kit 8u202](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
      - 推荐版本：8u202（稳定版本）
   
   2. **JDK 21**：
      - 下载链接：[JDK 21](https://www.oracle.com/java/technologies/downloads/#java21)
      - 选择最新版本即可
   
   注：安装后请确保正确配置JAVA_HOME环境变量。
   
   ### IDE设置
   
   推荐使用IntelliJ IDEA：
   - 下载社区版即可：[IDEA下载](https://www.jetbrains.com/idea/download/)
   - 安装Minecraft Development插件（用于任务3）

   ## 任务详情
   
   ### 任务1：Github仓库准备
   
   1. 创建个人GitHub账号（如果已有可跳过）
   2. 设置基本的个人信息
   3. 创建一个新的仓库用于提交作业
   
   ### 任务2：构建现有模组
   
   1. 克隆项目：`https://github.com/Roinflam/CarianStyle`
   2. 配置步骤：
      - 使用IDEA打开项目
      - 确保使用JDK 8
      - 等待Gradle完成依赖下载
   3. 构建并测试：
      - 运行Gradle build任务
      - 在Minecraft 1.12.2 Forge环境中测试
   4. 记录过程中遇到的问题和解决方案
   
   ### 任务3：开发Fabric模组
   
   使用IDEA的Minecraft Development插件创建新项目，实现两个功能：
   
   1. **死亡信息通知**
      - 在玩家死亡时显示玩家名称
      - 显示周围16格内的生物名称
   
   2. **生物秒杀功能**
      - 实现一击必杀生物的功能
      - 保持正常的掉落物品机制
   
   ### 任务4：文档编写
   
   为Fabric模组编写README文档，包含：
   - 模组介绍
   - 安装方法
   - 功能说明
   - 使用说明
   
   ## 提交要求
   
   创建一个新的GitHub仓库，包含：
   
   1. 构建好的CarianStyle模组jar文件
   2. Fabric模组的完整代码
   3. 开发过程的问题记录和解决方案
   4. AI工具使用心得
   
   ## 常见问题解决方案
   
   ### Gradle相关
   
   1. 依赖下载失败
      - 检查网络连接
      - 尝试使用国内镜像源
      - 清理Gradle缓存后重试
   
   2. 构建失败
      - 确认JDK版本正确
      - 检查Gradle控制台的错误信息
      - 确保所有依赖都已下载完成
   
   ### 模组开发相关
   
   1. 代码编译错误
      - 检查API使用是否正确
      - 确认使用的Minecraft版本
      - 查看Fabric/Forge文档
   
   2. 运行时错误
      - 检查日志文件
      - 使用调试模式运行
      - 确认模组版本兼容性
   
   ## 学习资源
   
   1. [Fabric Wiki](https://fabricmc.net/wiki/start)
   2. [Forge Documentation](https://mcforge.readthedocs.io/)
   3. [Minecraft Modding教程](https://github.com/fabricmc/fabric-example-mod)
   4. 问AI
   