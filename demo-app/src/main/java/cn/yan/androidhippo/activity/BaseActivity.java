package cn.yan.androidhippo.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import cn.yan.hippo.Hippo;

/**
 * Demo for activity base Hippo.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Hippo.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Hippo.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onTrimMemory(int level) {
        Hippo.onTrimMemory(this, level);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return Hippo.onKeyDown(this, keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return Hippo.onKeyUp(this, keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return Hippo.onKeyLongPress(this, keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return Hippo.onKeyMultiple(this, keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return Hippo.onKeyShortcut(this, keyCode, event);
    }
}
