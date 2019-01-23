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

​      主控板状态类，见 MainBroadStatus类，详细请查阅javaDoc文档。可以通过queryMainBroadStatus()主动查询或者registerMainBroadStatusListener()被动获取。

```java
/**
 * 查询主控板状态
 *
 * @return 主控板状态
 * @see MainBroadStatus
 */
MainBroadStatus queryMainBroadStatus();

/**
 * 注册主控板状态回调
 *
 * @param mainBroadStatusListener 电池信息监听器
 */
void registerMainBroadStatusListener(@NonNull MainBroadStatusListener mbsListener);
/**
 * 反注册主控板状态回调
 *
 * @param mainBroadStatusListener 电池信息监听器
 */
void unregisterMainBroadStatusListener(@NonNull MainBroadStatusListener mbsListener);
```

### 3、电池信息

  电池信息类，包括电压、电流、电量、电池百分比，电源管理板异常信息、电机电压、电机电流。见 BatteryInfo类，详细请查阅javaDoc文档。可以通过queryBatteryInfo()主动查询或者registe、rBatterInfoListener()被动获取。

```java
/**
 * 查询电池信息
 *
 * @return 电池信息
 * @see BatteryInfo
 */
BatteryInfo queryBatteryInfo();

/**
 * 注册电池信息回调
 *
 * @param mBatteryInfoListener 电池信息监听器
 */
void registerBatterInfoListener(@NonNull BatteryInfoListener mBatteryInfoListener);
/**
 * 反注册电池信息回调
 *
 * @param mBatteryInfoListener 电池信息监听器
 */
void unregisterBatterInfoListener(@NonNull BatteryInfoListener mBatteryInfoListener);
```

### 4、运动信息

运动信息类，包含电机速度、里程计、本体位置和朝向等信息，见 MotionInfo类，详细请查阅javaDoc文档。可以通过queryMotionInfo()主动查询当前的运动信息。

```java
/**
 * 查询运动信息
 *
 * @return 运动信息
 * @see MotionInfo
 */
MotionInfo queryMotionInfo();
```

### 5、红外信息

红外信息类，包含红外测距值，红外解码值等信息。见InfraRedInfo类，详细请查阅javaDoc文档。可以通过queryInfraRedInfo()主动查询当前的红外信息。其中有设置和查询红外避障阈值，详细如下：

```java
/**
 * 查询红外信息
 *
 * @return 红外信息
 * @see InfraRedInfo
 */
InfraRedInfo queryInfraRedInfo();
/**
 * 查询红外避障阈值
 *
 * @return 红外避障阈值
 */
int[] queryInfraredThreshold();
/**
 * 更新红外避障阈值
 *
 * @param values 红外避障阈值
 */
void updateInfraredThreshold(@NonNull int[] values);
```

### 6、充电状态信息

充电的状态信息，包含：

（1）充电状态：0：非充电；1：搜索充电桩；2：到达充电桩；3：充电中；4：超时；5：无信号；

（2）充电运动调整：0：保持 1：左行 2：右行 3：直行；充电次数；放电次数。

见ChargeStatus类，详细请查阅javaDoc文档。可以通过queryChargeStatus()主动查询当前的充电状态信息。

```java
/**
 * 查询充电状态信息
 *
 * @return 充电状态
 * @see ChargeStatus
 */
ChargeStatus queryChargeStatus();
```

### 7、电源管理板信息

电源管理板信息，包含：

（1）工作模式：0：上电初始模式 1：正常断电模式  2：正常工作模式 3：延时关机模式  4：延时开机模式；

（2）开关机记录：0：无记录  1：按键触发 2：软件触发  3：定时触发  4：遥控触发。

见PowerBroadInfo类，详细请查阅javaDoc文档。可以通过queryPowerBroadInfo()主动查询当前的充电状态信息。

```java
/**
 * 查询电源管理板信息
 *
 * @return 电源管理板信息
 * @see PowerBroadInfo
 */
PowerBroadInfo queryPowerBroadInfo();
```

### 8、超声波

 （1）查询超声波值

```java
/**
 * 查询超声波值
 *
 * @return 超声波值
 */
int[] queryUltrasoundValue();
```

 （2）设置超声波扫描顺序值和查询超声波扫描序号

```java
  /**
   * 设置超声波扫描顺序值
   *
   * @param ultrasoundIndex 超声波序号 ：0 - 11
   * @param scanIndex       超声波扫描通道序号：0-5
   */
 void setUltrasoundScanIndex(int ultrasoundIndex, int scanIndex);

  /**
   * 查询超声波扫描序号
   *
   * @return 超声波扫描序号数组
   */
  int[] queryUltrasoundScanIndexs();
```

 （3）设置超声波避障距离和查询超声波避障距离

```java
/**
 * 设置超声波避障距离
 *
 * @param ultrasoundIndex 超声波序号：0-11
 * @param distance        0-255 盲区大小,cm为单位
 */
void setUltrasoundBlindAreaDistances(int ultrasoundIndex, int distance);
```

### 9、控制车轮信息

控制车轮信息，包括：运动控制字： (0x00)空档，无阻力 ，(0x01)向前行驶，(0x02)向后行驶， (0x03)速度环旋转逆时，(0x04)速度环旋转顺时， (0x05)被动刹停， (0x06)主动刹停，(0x07)位置环控制， (0x08)位置环旋转逆时，(0x09)位置环旋转顺时， (0x80)自动充电模式；轮子速度、加速度、减速度、旋转角度、位置环绝对值和驱动器控制域。

见WheelInfo类，详细请查阅javaDoc文档。可以通过queryWheelInfo()主动查询当前的控制车轮信息。

```java
/**
 * 查询主控板控制车轮信息
 *
 * @return 主控板控制车轮信息
 * @see WheelInfo
 */
WheelInfo queryWheelInfo();
```

### 10、主控板使能信息 

主控板使能信息，包含自动充电使能、超声波使能、超宽带使能、左右盘驱动电机使能、安卓板通信使能、工控机通信使能和电源管理板使能。见MainBroadEnable类，详细请查阅javaDoc文档。可以通过queryMainBroadEnable()查询主控板使能信息，updateMainBroadEnable()更新主控板使能信息。

```java
/**
 * 查询主控板使能信息
 *
 * @return 主控板使能信息
 * @see MainBroadEnable
 */
MainBroadEnable queryMainBroadEnable();

/**
 * 更新主控板使能信息
 *
 * @param mainBroadEnable 主控板使能信息
 */
void updateMainBroadEnable(@NonNull MainBroadEnable mainBroadEnable);
```

### 11、UWB信息

UWB信息，见UWBInfo类字段，详细请查阅javaDoc文档。

```java
/**
 * 查询UWB信息
 *
 * @return UWBInfo 实例
 * @see UWBInfo
 */
UWBInfo queryUWBInfo();
/**
 * 更新 UWBInfo
 *
 * @param uwbInfo UWBInfo实例
 */
void updateUWBInfo(@NonNull UWBInfo uwbInfo);
```

### 12、主控板RTC时间

查询主控板RTC时间，对应MainBroadRTC，包含有当前时间的时分秒，定时开机的时分字段。详细请查阅javado文档。

```java
/**
 * 查询主控板RTC时间
 *
 * @return MainBroadRTC实例
 * @see MainBroadRTC
 */
MainBroadRTC queryMainBroadRTC();

/**
 * 更新主控板RTC时间
 *
 * @param mainBroadRTC MainBroadRTC实例
 */
void updateMainBroadRTC(@NonNull MainBroadRTC mainBroadRTC);
```

### 13、呼吸灯

四代客服呼吸灯改为双耳上，可以随意RGB值控制灯光颜色，如下：

```java
/**
 * LED呼吸灯开关
 */
void ledSetOnOff(boolean onOff);

/**
 * LED呼吸灯设置RGB值
 */
void ledSetColor(int r, int g, int b);
```

### 14、头部信息和运动

获取头部信息，需要根据queryHeadInfo()主动获取，见类HeadInfo，包含头部当前的角度、电机电流及其各种状态，详细见javaDoc文档。头部运动调用turnhead()，具体见如下：

```java
/**
 * 查询舵机板的头部信息
 *
 * @return 头部信息
 * @see HeadInfo
 */
HeadInfo queryHeadInfo();

/**
 * 头部运动
 *
 * @param speed 默认速度5 r/min,取值范围（0-15）
 * @param angle 头部转到的角度（0-180，正前方为90度）
 */
void turnHead(int speed, int angle);
```

### 15、手臂运动

获取手臂信息，需要根据queryArmInfo()主动获取，见类ArmInfo，包含手臂当前的角度、运动速度及其各种状态，详细见javaDoc文档。左臂运动调用turnLeftArm()，右臂运动调用turnRghtArm()。若需要双臂同步运动，先要syncBothArms()设置双臂同步模式，再调用左臂turnLeftArm()运动。

```java
/**
 * 查询舵机板的手臂信息
 *
 * @param type 手臂类型：左臂或者右臂
 * @return 手臂信息
 * @see ArmInfo
 */
ArmInfo queryArmInfo(int type);

/**
 * 左臂运动
 *
 * @param speed 默认速度 10 r/min,取值范围（0-10）
 * @param angle 手臂转动到的角度（120-360，垂直地面方向180度）
 */
void turnLeftArm(int speed, int angle);
/**
 * 右臂转动
 *
 * @param speed 默认速度 10 r/min,取值范围（0-10）
 * @param angle 手臂转动到的角度（120-360，垂直地面方向180度）
 */
void turnRightArm(int speed, int angle);
/**
 * 双臂同步
 *
 * @param armSyncMode 同步默认
 */
void syncBothArms(@SyncArmMode int armSyncMode);
```

### 16、关闭机器人系统

```java
/**
 * 关闭机器人系统
 * 主控板会延迟60s后切段所有的电源。
 *
 * @return true 表示关闭成功，false表示关闭失败
 */
boolean shutdownRobotSystem();
```

### 17、MCU升级

mcu升级，需要先指定升级的类型，详细见McuBinType类，类型有：左轮电机驱动板、右轮电机驱动板、

电源管理板、超声波板1、超声波板2、主控板、舵机板。升级过程中出现错误会返回错误码，具体错误码见McuUpdateErrorCode类。更加详细的说明请查阅javaDoc文档。

```java
/**
 * 升级主控板相关bin
 *
 * @param binType           bin文件类型
 * @param binPath           bin文件的路径地址
 * @param mcuUpdateListener mcu升级监听器
 * @see com.szyh.iwaasdk.sdk.ros.define.RosDefine.McuBinType
 */
void updateMcuByBinType(@McuBinType int binType, @NonNull String binPath, @NonNull McuUpdateListener mcuUpdateListener);
```

​	

## (二) 机器人导航模块

 

## (三) 人脸识别模块



## (四) 六麦模块





