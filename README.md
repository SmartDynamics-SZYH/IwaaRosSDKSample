# 神州云海IwaaRosSDK集成说明文档V1.0.0

## 一、概述

本文档是在Android 5.1系统上，Android程序集成Iwaa四代客服机器人相关功能SDK的说明文档。  
IwaaRosSDK分为四大模块：1、Ros模块，与底层MCU模块通信；2、导航模块，与工控机通信处理导航相关业务；3、人脸识别模块，与人脸盒子通信处理人脸检测和识别；4、六麦模块，处理与六麦核心板通信。


## 二、集成说明

Android开发工具为Android Studio,不建议使用Eclipse集成。
IwaaRosSDK 适用Android SDK使用版本：4.4及以上。
IwaaRosSDK是以aar文件打包，名称如IwaaRosSDK-release-1.0.0.aar，集成步骤说明如下：

1. 把aar文件 拷贝到 自己Android工程的app/libs目录下；  

   . 在app/build.gradle中添加如下	 

```groovy
repositories {
	flatDir {
		dirs 'libs'
	}
}
dependencies {
	//机器人控制需要添加的aar
	implementation(name: 'IwaaRosSDK-release-1.0.0, ext: 'aar')；
	implementation 'com.alibaba:fastjson:1.1.70.android';
	implementation 'com.squareup.okhttp3:okhttp:3.8.1';
}
```

或者简洁版

```groovy
dependencies {
	implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
	implementation 'com.alibaba:fastjson:1.1.70.android'
	implementation 'com.squareup.okhttp3:okhttp:3.8.1';
}
```

1. 在AndroidManifest.xml中添加权限，如有则忽略 

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

# 三、功能说明

功能说明见[wiki](https://github.com/SmartDynamics-SZYH/IwaaRosSDKSample/wiki)