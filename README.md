## 项目说明🎈

欢迎加入sap_rfc项目开发，在这里你将快速学会如何调用**RFC函数**以获取SAP的数据。

### 架构

```mermaid
flowchart LR
	subgraph FC
	id2
	end
	id1[(SAP-HANA)]--RFC-->id2[ACR镜像]--Tunnel-->id3[(Maxcompute)]
```

本项目采用**Serveless**架构，通过阿里云的函数计算服务（**FunctionCompute 下称FC**）启动阿里云容器仓库中的**ACR镜像**并执行对应函数，以实现将数据从**RFC**抽取数据至**FC**，并调用**Tunnel组件**的SDK将数据持久化进数仓**STG层**。每个RFC函数都将对应一个阿里云的FC函数，函数间使用的触发器，逻辑，计算资源相互独立。

### 代码目录说明

#### 根目录

![image-20230614111703605](images/image-20230614111703605.png)

- main是存储业务代码的路径

- test是存储测试代码的路径

- Dockerfile会将含jdk1.8和SAP运行时作为基础镜像，并将Maven构建好的jar文件构造新的镜像，存储在阿里云的容器仓库ACR中；

### QuickStart





## Git-Codeup版本管理说明

### 3 分钟了解如何进入开发

欢迎使用 Codeup，通过阅读以下内容，你可以快速熟悉 Codeup ，并立即开始今天的工作。

### 提交**文件**

首先，你需要了解在 Codeup 中如何提交代码文件，跟着文档「[__提交第一行代码__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f6786b81620014ef7574)」一起操作试试看吧。

### 开启扫描

开发过程中，为了更好的管理你的代码资产，Codeup 内置了「[__代码规约扫描__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f68b6b81620014ef7588)」和「[__敏感信息检测__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f6886b81620014ef7587)」服务，你可以在代码库设置-集成与服务中一键开启，开启后提交或合并请求的变更将自动触发扫描，并及时提供结果反馈。

![](https://img.alicdn.com/tfs/TB1nRDatoz1gK0jSZLeXXb9kVXa-1122-380.png "")

![](https://img.alicdn.com/tfs/TB1PrPatXY7gK0jSZKzXXaikpXa-1122-709.png "")

### 代码评审

功能开发完毕后，通常你需要发起「[__代码合并和评审__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f6876b81620014ef7585)」，Codeup 支持多人协作的代码评审服务，你可以通过「[__保护分支__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f68e6b81620014ef758c)」策略及「[__合并请求设置__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f68f6b81620014ef758d)」对合并过程进行流程化管控，同时提供 WebIDE 在线代码评审及冲突解决能力，让你的评审过程更加流畅。

![](https://img.alicdn.com/tfs/TB1XHrctkP2gK0jSZPxXXacQpXa-1432-887.png "")

![](https://img.alicdn.com/tfs/TB1V3fctoY1gK0jSZFMXXaWcVXa-1432-600.png "")

### 编写文档

项目推进过程中，你的经验和感悟可以直接记录到 Codeup 代码库的「[__文档__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5e13107eedac6e001bd84889)」内，让智慧可视化。

![](https://img.alicdn.com/tfs/TB1BN2ateT2gK0jSZFvXXXnFXXa-1432-700.png "")

### 成员协作

是时候邀请成员一起编写卓越的代码工程了，请点击右上角「成员」邀请你的小伙伴开始协作吧！

### 更多

Git 使用教学、高级功能指引等更多说明，参见[__Codeup帮助文档__](https://thoughts.teambition.com/sharespace/5d88b152037db60015203fd3/docs/5dc4f6756b81620014ef7571)。
