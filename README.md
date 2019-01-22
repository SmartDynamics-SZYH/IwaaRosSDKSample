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

## (一) 机器人Ros模块

​        该模块是与主控板和舵机板的通信，控制机器人各个模块，主控板是机器人电池、运动、红外、充电过程、电源管理、超声波、超宽带的控制核心板，舵机板是机器人灯光、头部舵机、手臂舵控制核心板。IwaaRosSDK中对应的类是：RobotRosApi类，使用该模块之前，需要在Application中初始化：

```java
    //初始化RobotRosApi
   RobotRosApi.get().init(getApplicationContext(), new InitListener() {
       @Override
       public void onInit() {
           Log.i(TAG, "onInit: ");
       }
       @Override
       public void onReConnect() {
           Log.i(TAG, "onReConnect: ");
       }
   });
```

使用结束之后，需要销毁：

```java
   //销毁RobotRosApi
   RobotRosApi.get().destroy();
```
### 1、版本号

​      版本号类，见 DeviceVersion，根据type可以分主控板版本号、超宽带、电源管理板、底盘左、底盘右、超声波1、超声波2、舵机板八个版本。具体的详细情况，请查询javaDoc文档。

使用结束之后，需要销毁：

```java
   //主控板版本号查询
   DeviceVersion queryMainBroadVersion(boolean isReQuery);
   //舵机板版本号查询
   DeviceVersion queryRudderBroadVersion(boolean isReQuery);
   //其余版本号集合查询,可以根据type区分
   List<DeviceVersion> queryMountDeviceVersions(boolean isReQuery);
```

### 2、主控板状态

​      主控板状态类，见 MainBroadStatus类，具体的详细情况，请查询javaDoc文档。可以通过queryMainBroadStatus()主动查询或者registerMainBroadStatusListener()被动获取。

### 3、电池信息

  电池信息类，见 BatteryInfo类，具体的详细情况，请查询javaDoc文档。可以通过queryBatteryInfo()主动查询或者registerBatterInfoListener()被动获取。

### 4、运动信息

### 5、红外信息

### 6、充电状态信息

### 7、电源管理板信息

### 8、超声波扫描顺序

### 9、超声波避障距离

### 10、红外信息

### 11、主控板使能信息

### 12、超声波扫描序号

### 13、超声波盲区大小

### 14、UWB信息

### 15、主控板RTC时间

### 16、红外避障阈值

### 17、呼吸灯

### 18、头部运动

### 19、手臂运动

### 20、关闭机器人系统

### 21、MCU升级



​	

## (二) 机器人导航模块

 

## (三) 人脸识别模块



## (四) 六麦模块





