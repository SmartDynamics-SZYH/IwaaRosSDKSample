# 神州云海IwaaRosSDK集成说明文档V1.0.0

## 概述

​	IwaaRosSDK适应于神州云海艾娃客服四代机器人（安卓系统为RK3288 Android5.1.1版本）。该SDK分为四大模块，分别如下：

​	1、机器人ROS模块：该模块与底层MCU通信，处理单片机相关业务；

​	2、机器人导航模块：与工控机通信通信，处理导航相关业务；

​	3、人脸识别模块：与人脸盒子通信，处理人脸检测和识别业务；

​	4、六麦核心板模块：与六麦核心板通信，处理麦相关业务。

​	四大模块，相互独立，不互相牵扯。


## 集成说明

Android开发工具为Android Studio,不建议使用Eclipse集成。
IwaaRosSDK 适用Android SDK使用版本：4.4及以上。
IwaaRosSDK是以aar文件打包，名称如IwaaRosSDK-release-1.0.0.aar，集成步骤说明如下：

1. 把aar文件 拷贝到 自己Android工程的app/libs目录下；  
	.  在app/build.gradle中添加如下	 

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

3. 在AndroidManifest.xml中添加权限，如有则忽略 

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

# 功能说明

功能说明详情请见   【 [wiki](https://github.com/SmartDynamics-SZYH/IwaaRosSDKSample/wiki/IwaaRosSDK概述和集成说明) 】



# 联系我们

邮箱：[ruanhouli@szyh-smart.com]()

# License
 ```
  
Copyright 2015-2019 Smart Dynamcis Co.,Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 ```
