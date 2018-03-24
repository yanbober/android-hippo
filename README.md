# android-hippo

河马是一个基于依赖注解的 Android 核心 case 方法优雅写法库。

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


