package cn.yan.androidhippo.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import cn.yan.hippo.annotations.OnActivityResult;
import cn.yan.hippo.annotations.OnKeyDown;
import cn.yan.hippo.annotations.OnKeyLongPress;
import cn.yan.hippo.annotations.OnKeyMultiple;
import cn.yan.hippo.annotations.OnKeyShortcut;
import cn.yan.hippo.annotations.OnKeyUp;
import cn.yan.hippo.annotations.OnRequestPermissionsResult;
import cn.yan.hippo.annotations.OnTrimMemory;

/**
 * Demo for activity simple.
 */

public class HippoActivity extends BaseActivity {
    private static final String TAG = "HippoActivity";

    /*******************************************************
     * {@link OnActivityResult}
     ******************************************************/

    @OnActivityResult(requestCode = 23)
    public void resultHandle1(int resultCode, Intent data) {
        Log.i(TAG, "resultHandle1");
    }

    @OnActivityResult(requestCode = {24, 25})
    public void resultHandle2(int resultCode, Intent data) {
        Log.i(TAG, "resultHandle2");
    }

    /*******************************************************
     * {@link OnRequestPermissionsResult}
     ******************************************************/

    @OnRequestPermissionsResult(requestCode = 77777)
    public void permissionsResult1(String[] permissions, int[] grantResults) {
        Log.i(TAG, "permissionsResult1");
    }

    @OnRequestPermissionsResult(requestCode = {77778, 77779})
    public void permissionsResult2(String[] permissions, int[] grantResults) {
        Log.i(TAG, "permissionsResult2");
    }

    /*******************************************************
     * {@link OnKeyDown}
     ******************************************************/

    @OnKeyDown(keyCode = KeyEvent.ACTION_DOWN)
    public boolean onKeyDown1(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown1");
        return true;
    }

    @OnKeyDown(keyCode = {KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK})
    public boolean onKeyDown2(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyDown2");
        return true;
    }

    /*******************************************************
     * {@link OnKeyLongPress}
     ******************************************************/
    @OnKeyLongPress(keyCode = {KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK})
    public boolean onKeyLongPress1(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyLongPress1");
        return true;
    }

    @OnKeyLongPress(keyCode = KeyEvent.ACTION_DOWN)
    public boolean onKeyLongPress2(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyLongPress2");
        return true;
    }

    /*******************************************************
     * {@link OnKeyMultiple}
     ******************************************************/

    @OnKeyMultiple(keyCode = {KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK})
    public boolean OnKeyMultiple1(int keyCode, int repeatCount, KeyEvent event) {
        Log.i(TAG, "OnKeyMultiple1");
        return true;
    }

    @OnKeyMultiple(keyCode = KeyEvent.ACTION_DOWN)
    public boolean OnKeyMultiple2(int keyCode, int repeatCount, KeyEvent event) {
        Log.i(TAG, "OnKeyMultiple2");
        return true;
    }

    /*******************************************************
     * {@link OnKeyShortcut}
     ******************************************************/

    @OnKeyShortcut(keyCode = {KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK})
    public boolean OnKeyShortcut1(int keyCode, KeyEvent event) {
        Log.i(TAG, "OnKeyShortcut1");
        return true;
    }

    @OnKeyShortcut(keyCode = KeyEvent.ACTION_DOWN)
    public boolean OnKeyShortcut2(int keyCode, KeyEvent event) {
        Log.i(TAG, "OnKeyShortcut2");
        return true;
    }

    /*******************************************************
     * {@link OnKeyUp}
     ******************************************************/

    @OnKeyUp(keyCode = KeyEvent.KEYCODE_Y)
    public boolean onKeyUp1(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp1");
        return true;
    }

    @OnKeyUp(keyCode = {KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_Q})
    public boolean onKeyUp2(int keyCode, KeyEvent event) {
        Log.i(TAG, "onKeyUp2");
        return true;
    }

    /*******************************************************
     * {@link OnTrimMemory}
     ******************************************************/

    @OnTrimMemory(level = Activity.TRIM_MEMORY_RUNNING_MODERATE)
    public void onTrimMemory1(int level) {
        Log.i(TAG, "onTrimMemory1");
    }

    @OnTrimMemory(level = {
            Activity.TRIM_MEMORY_BACKGROUND,
            Activity.TRIM_MEMORY_RUNNING_LOW
    })
    public void onTrimMemory2(int level) {
        Log.i(TAG, "onTrimMemory2");
    }
}
