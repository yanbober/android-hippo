# android-hippo

[![](https://jitpack.io/v/yanbober/android-hippo.svg)](https://jitpack.io/#yanbober/android-hippo)

河马是一个基于依赖注解的 Android 核心 case 方法优雅写法库，避免了多 case 的 switch 或者 if 条件判断，规避了条件判断中 equals 方法潜在空指针问题。

平时写广播接收的处理是如下写法：
```java
public class BaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_CALL.equals(action)) {
            Toast.makeText(context, "bcCall", Toast.LENGTH_SHORT).show();
        } else if (Intent.ACTION_CHOOSER.equals(action) || Intent.ACTION_BATTERY_LOW.equals(action)) {
            Toast.makeText(context, "bcChooseOrBatteryLow", Toast.LENGTH_SHORT).show();
        } else ......
    }
}
```

使用 android-hippo 河马后的优雅写法如下：
```java
public class BaseBroadcastReceiver extends BroadcastReceiver {
    @OnReceive(action = {Intent.ACTION_CHOOSER, Intent.ACTION_BATTERY_LOW})
    public void bcChooseOrBatteryLow(Context context, Intent intent) {
        Toast.makeText(context, "bcChooseOrBatteryLow", Toast.LENGTH_SHORT).show();
    }

    @OnReceive(action = Intent.ACTION_CALL)
    public void bcCall(Context context, Intent intent) {
        Toast.makeText(context, "bcCall", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Hippo.onReceive(this, context, intent);
    }
}
```
其他更多 case 支持参见 API 列表。

## 配置

在你的 root build.gradle 中添加如下脚本片段：
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

在你需要用 andrond-hippo 的模块添加如下依赖片段：
```gradle
dependencies {
    compile 'com.github.yanbober.android-hippo:hippo:1.0.3'
    annotationProcessor 'com.github.yanbober.android-hippo:hippo-compiler:1.0.3'
}
```

## 使用

具体所有 API 样例可参见 demo-app 中详细示范。

## API 列表

|case 注解名|Android 库方法名|
|----|----|
|@OnActivityResult|onActivityResult(int requestCode, int resultCode, Intent data)|
|@OnRequestPermissionsResult|onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)|
|@OnTrimMemory|onTrimMemory(int level)|
|@OnKeyDown|onKeyDown(int keyCode, KeyEvent event)|
|@OnKeyUp|onKeyUp(int keyCode, KeyEvent event)|
|@OnKeyLongPress|onKeyLongPress(int keyCode, KeyEvent event)|
|@OnKeyMultiple|onKeyMultiple(int keyCode, int repeatCount, KeyEvent event)|
|@OnKeyShortcut|onKeyShortcut(int keyCode, KeyEvent event)|
|@OnReceive|onReceive(Context context, Intent intent)|


