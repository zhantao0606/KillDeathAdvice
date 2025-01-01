 

### 任务2：构建现有模组

1. 克隆项目：

https://github.com/Roinflam/CarianStyle

2. 配置步骤：

​	 使用IDEA打开项目

​	 确保使用JDK 8

​	等待Gradle完成依赖下载

3. 构建并测试：

​	 运行Gradle build任务

​	 在Minecraft 1.12.2 Forge环境中测试

4. 记录过程中遇到的问题和解决方案

1.问题一，jdk8配置问题。

​		在配置项目时，由于没有配置jdk8的系统环境（原版jdk17），去编辑环境变量添加jdk8的存放地址。并在项目结构中使用jdk8.

​	2.问题二，网络配置问题（外网连接）。

​		下载国外gradle时，由于我使用手机热点开启vpn，需同时在手机和电脑端下载cuteCloud开启全局模式并在window端开启tun工作模式，然后在idea的http配置中开启自动监测代理。

​	3.问题三，不知道如何构建build gradle，以及环境异常报错。

​		 通过询问查找发现系统自动生成8.1的gradle版本,与当前的配置冲突,要改用8.8的版本,并且通过build后,通过runClient直接启动。

 

### 任务3：开发Fabric模组

使用IDEA的Minecraft Development插件创建新项目，实现两个功能：

1. 死亡信息通知

​	 在玩家死亡时显示玩家名称

​	 显示周围16格内的生物名称

2. 生物秒杀功能

​	  实现一击必杀生物的功能

 	 保持正常的掉落物品机制

创建项目过程中

​	1.环境出现问题：

​		每次下载jdk系统都会自己产生一个大于JAVA_HOME优先级的本地配置。删掉然后将JAVA_HOME对准目标jdk21的文件路径即可。

 

​	2.文件构建完成，runClient时出现模组冲突:

​		发现使用的loader_version=0.15.7低于版本需要至少loader_version=0.15.11版本

 

​	3.运行出现击杀生物崩溃退出，根据控制台爆错信息得知出现java.lang.StackOverflowError（堆栈溢出错误）

1. 错误详细信息

​		 异常发生在LivingEntity.damage()方法中，这是Minecraft中处理生物受伤的核心方法。

​		错误触发点在com.gec.killdeathadvice.mod.DeathNoticeMod类的lambda$onInitialize$1方法中，该方法在DeathNoticeMod.java的第63行。

​		 错误表现为递归调用，导致堆栈溢出。

​		发现其中的lamba表达式中第一次调用ALLOW_DAMAGE时监测玩家攻击调用entity.damage(),然后entity.damage()会重新调用ALLOW_DAMAGE发生递归

// 注册生物伤害事件监听器

​    ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {

​      // 检查是否是玩家造成的伤害

​      if (source.getAttacker() instanceof PlayerEntity) {

​        // 如果目标不是玩家，则秒杀

​        if (!(entity instanceof PlayerEntity)) {

​          // 设置伤害为目标生物的最大生命值，确保一击必杀

​          entity.damage(source, entity.getMaxHealth());

​          return false; // 阻止原始伤害

​        }

​      }

​      return true; // 允许正常伤害处理

​    });

  }

解决方案:

​		放弃该代码逻辑使用.使用 isProcessingDamage 标志防止递归调用,然后在直接使用LivingEntity时会出现类型报错"出现模式类型 'LivingEntity' 与表达式类型相同".不知道是不是语言级别的问题.所以使用了一个实体类型转换LivingEntity livingEntity = (LivingEntity) entity

 

4.提交仓库时出现下列代码：

$ git add .

warning: adding embedded git repository: CarianStyle

hint: You've added another git repository inside your current repository.

hint: Clones of the outer repository will not contain the contents of

hint: the embedded repository and will not know how to obtain it.

hint: If you meant to add a submodule, use:

hint:

hint:  git submodule add <url> CarianStyle

hint:

hint: If you added this path by mistake, you can remove it from the

hint: index with:

hint:

hint:  git rm --cached CarianStyle

hint:

hint: See "git help submodule" for more information.

hint: Disable this message with "git config advice.addEmbeddedRepo false"

error: 'TestMC/' does not have a commit checked out

fatal: adding files failed

 

​		表示原来已经有一个嵌入的仓库，里面是CarianStyle/.git，需要重新提交两个一起

​		解决方案：

​		先做删除

​		rm -rf CarianStyle/.git

​		rm -rf TestMC/.git

​		然后重新初始化仓库

​		git init

​		然后git add .来添加所有文件

​		提交仓库名git commit -m "Initial commit: Kill Death Advice Mod

​		给到我的github仓库git remote add origin "仓库url"  并   git push -u origin main

 

 

 

**AI使用心得:**

Claude是一款强大的AI软件,当我的方案做的有问题甚至完全不熟悉它都能为我提供一个代码环境,我可以通过它来了解一个功能背后实现的逻辑,当然,ai有个弊端,它直接告诉我们代码,如果你光依赖它不去自己学习，这对我们的未来发展其实不大好,尤其是初学者。 当然,在急着交代码实现功能前提下，是最好的选择。我目前对java的底层逻辑都不熟。可以通过它先看下它的一些方案如何实现的，去先了解借鉴。还有一些报错也可以跟它说，来快速定位问题错误位置，并解决。总体来说它的功能非常强大

 

 

 