package cn.yan.androidhippo.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.yan.hippo.Hippo;

/**
 * Demo for BroadcastReceiver base Hippo.
 */

public class BaseBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Hippo.onReceive(this, context, intent);
    }
}
