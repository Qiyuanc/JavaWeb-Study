## Maven学习笔记

### 1. 配置Maven

#### 1.1 Maven有什么作用？

1.在JavaWeb开发中需要导入大量jar包，需要手动导入，多次下来就很麻烦；

2.需要一个工具帮助我们导入和配置jar包；

3.Maven诞生了。

#### 1.2 配置Maven

下载Maven，解压到本地文件夹中

![](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714154049552.png)

配置环境变量

- M2_HOME 指向maven目录下的bin目录
- MAVEN_HOME 指向maven的目录
- 在系统path中配置 %MAVEN_HOME%\bin

运行cmd命令测试一下

![](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714160206113.png)

#### 1.3 配置阿里云镜像

作用：加速下载

在...apache-maven-3.8.1\conf\settings.xml中的mirror段中加入

```xml
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>central</mirrorOf>
    <name>aliyun maven</name>
    <url>https://maven.aliyun.com/repository/public </url>
</mirror>
```

#### 1.4 配置本地仓库

有本地仓库，也有远程仓库。

在...\apache-maven-3.8.1下创建maven-repo，作为本地仓库（默认的仓库在C盘，不太好）

在...apache-maven-3.8.1\conf\settings.xml中的localRepository段下添加

```xml
<localRepository>F:\Enviroment\apache-maven-3.8.1\maven-repo</localRepository>
```

### 2. 在IDEA中使用Maven

#### 2.1 搭建Maven项目

创建一个maven项目

![](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714162655029.png)

下一步

![](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714163426945.png)

填写完成

![image-20210714164341832](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714163441832.png)

下一步

![image-20210714163735468](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714163735468.png)

配置一下

![image-20210714164008321](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714164008321.png)

完成后可以看到右下角，并且在疯狂下载东西，如果没有下载或者下载很慢说明之前的镜像配置错了

![image-20210714164559790](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714164559790.png)

等待下载完成出现BUILD SUCCESS说明搭建成功了。

![image-20210714165153715](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714165153715.png)

#### 2.2 查看maven仓库

看看配置完后本地仓库中有什么变化

![](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714165748887.png)

多了一大堆配置文件

#### 2.3 查看IDEA的maven配置

创建完后IDEA可能自动将maven配置设为默认，来看一眼

![image-20210714170127816](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714170127816.png)

可以选择导入源码和文档

![image-20210714170346682](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714170346682.png)

#### 2.4 创建纯净的maven项目

在创建maven项目是不勾选使用模板，直接创建，可以得到一个纯净的maven项目

![image-20210714171450154](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714171450154.png)

#### 2.5 为Webapp项目添加文件夹

在src\main目录下新建文件夹，IDEA2020版本可以自动提示帮助生成文件夹

![image-20210714171918691](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714171918691.png)

创建完的目录

![image-20210714172008509](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714172008509.png)

若不能自动创建，手动创建完文件夹后右键文件夹选择Mark改变文件夹的属性

![image-20210714172115948](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714172115948.png)

或者在IDEA File设置下的Project Structrue中更改

![image-20210714172431775](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714172431775.png)

#### 2.6 在IDEA中配置Tomcat

IDEA2020版本选择上面的Add Configuration

![image-20210714172743828](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714172743828.png)

点击 + 号，创建新设置

![image-20210714173417633](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714173417633.png)

进行Tomcat的配置

![image-20210714174405700](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714174405700.png)

添加war包，解决下面的Warning问题

![image-20210714174624790](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714174624790.png)

普通war包即可，exploded用于导出

添加完后报错信息消失，主要用于确定访问路径，如本项目添加后本地访问的路径是localhost:8080/javaweb_01_maven

这个过程即为**虚拟路径映射**。

![image-20210714181957605](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714181957605.png)

接下来就可以开启Tomcat服务了

![image-20210714182412370](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714182412370.png)

Hello World成功！

![image-20210714182509060](https://gitee.com/qiyuanc/markdown-picture/raw/master/picture/JavaWeb%E5%A4%8D%E4%B9%A0%E3%80%81Maven%E5%AD%A6%E4%B9%A0%E7%AC%94%E8%AE%B0/image-20210714182509060.png)

#### 2.7 Maven pom.xml

pom.xml文件是Maven的核心。

其中包括主要配置

```xml
<!--Maven版本和头文件-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <!--配置的GAV-->
  <groupId>com.qiyuan</groupId>
  <artifactId>javaweb-01-maven</artifactId>
  <version>1.0-SNAPSHOT</version>
  <!--打包方式
  jar：java项目
  war：javawab项目-->
  <packaging>war</packaging>
```

还有依赖的jar包，可以去maven仓库寻找依赖

https://mvnrepository.com/

如spring web mvc 的依赖（dependency在denpendencies里面QAQ）

```xml
<!--Maven的强大之处：自动导入需要的jar包的前置jar包-->
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>5.2.15.RELEASE</version>
    </dependency>
</dependencies>
```

添加进pom.xml后maven会自动导入依赖。

Maven由于约定大于配置，我们写的配置文件可能无法导出或生效，

解决方案

```xml
<build>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <excludes>
                    <exclude>**/*.properties</exclude>
                    <exclude>**/*.xml</exclude>
                </excludes>
                <filtering>false</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.properties</include>
                    <include>**/*.xml</include>
                </includes>
                <filtering>false</filtering>
            </resource>
        </resources>
</build>
```

目前还用不到。。。

#### 
