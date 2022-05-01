package cn.yan.androidhippo.broadcast

import android.content.Context
import android.content.Intent
import android.widget.Toast
import cn.yan.hippo.annotations.OnReceive

/**
 * Demo for kotlin BroadcastReceiver simple.
 */

class SyncKotlinBroadcastReceiver: BaseBroadcastReceiver() {
    companion object {
        const val ACTION_TEST = "cn.yan.demo.test.actionKotlinTest"
    }

    @OnReceive(action = [ACTION_TEST])
    fun bcActionTest(context: Context, intent: Intent) {
        Toast.makeText(context, "bcActionTest intent=${intent}", Toast.LENGTH_SHORT).show()
    }

    @OnReceive(action = [Intent.ACTION_CHOOSER, Intent.ACTION_BATTERY_LOW])
    fun bcChooseOrBatteryLow(context: Context, intent: Intent) {
        Toast.makeText(context, "bcChooseOrBatteryLow intent=${intent}", Toast.LENGTH_SHORT).show()
    }

    @OnReceive(action = [Intent.ACTION_CALL])
    fun bcCall(context: Context, intent: Intent) {
        Toast.makeText(context, "bcCall intent=${intent}", Toast.LENGTH_SHORT).show()
    }

    @OnReceive(action = ["cn.yan.demo.XXXXXX"])
    fun bcXxxxxx(context: Context, intent: Intent) {
        Toast.makeText(context, "bcXxxxxx intent=${intent}", Toast.LENGTH_SHORT).show()
    }
}
