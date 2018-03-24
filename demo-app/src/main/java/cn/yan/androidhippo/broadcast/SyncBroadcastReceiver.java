package cn.yan.androidhippo.broadcast;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import cn.yan.hippo.annotations.OnReceive;

/**
 * Demo for BroadcastReceiver simple.
 */

public class SyncBroadcastReceiver extends BaseBroadcastReceiver {
    public static final String ACTION_TEST = "cn.yan.demo.test.actionTest";

    @OnReceive(action = ACTION_TEST)
    public void bcActionTest(Context context, Intent intent) {
        Toast.makeText(context, "bcActionTest", Toast.LENGTH_SHORT).show();
    }

    @OnReceive(action = {Intent.ACTION_CHOOSER, Intent.ACTION_BATTERY_LOW})
    public void bcChooseOrBatteryLow(Context context, Intent intent) {
        Toast.makeText(context, "bcChooseOrBatteryLow", Toast.LENGTH_SHORT).show();
    }

    @OnReceive(action = Intent.ACTION_CALL)
    public void bcCall(Context context, Intent intent) {
        Toast.makeText(context, "bcCall", Toast.LENGTH_SHORT).show();
    }

    @OnReceive(action = "cn.yan.demo.XXXXXX")
    public void bcXxxxxx(Context context, Intent intent) {
        Toast.makeText(context, "bcXxxxxx", Toast.LENGTH_SHORT).show();
    }
}
