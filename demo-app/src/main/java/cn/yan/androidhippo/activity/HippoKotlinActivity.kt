package cn.yan.androidhippo.activity

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import cn.yan.hippo.annotations.OnActivityResult
import cn.yan.hippo.annotations.OnKeyDown
import cn.yan.hippo.annotations.OnKeyLongPress
import cn.yan.hippo.annotations.OnKeyMultiple
import cn.yan.hippo.annotations.OnKeyShortcut
import cn.yan.hippo.annotations.OnKeyUp
import cn.yan.hippo.annotations.OnRequestPermissionsResult
import cn.yan.hippo.annotations.OnTrimMemory

/**
 * Demo for kotlin activity simple.
 */

class HippoKotlinActivity: BaseActivity() {
    companion object {
        private const val TAG = "HippoKotlinActivity"
    }

    /*******************************************************
     * {@link OnActivityResult}
     ******************************************************/

    @OnActivityResult(requestCode = [23])
    fun resultHandle1(resultCode: Int, data: Intent) {
        Log.i(TAG, "resultHandle1 resultCode=${resultCode} data=${data}")
    }

    @OnActivityResult(requestCode = [24, 25])
    fun resultHandle2(resultCode: Int, data: Intent) {
        Log.i(TAG, "resultHandle2 resultCode=${resultCode} data=${data}")
    }

    /*******************************************************
     * {@link OnRequestPermissionsResult}
     ******************************************************/

    @OnRequestPermissionsResult(requestCode = [77777])
    fun permissionsResult1(permissions: Array<String>, grantResults: IntArray) {
        Log.i(TAG, "permissionsResult1 permissions=${permissions} grantResults=${grantResults}")
    }

    @OnRequestPermissionsResult(requestCode = [77778, 77779])
    fun permissionsResult2(permissions: Array<String>, grantResults: IntArray) {
        Log.i(TAG, "permissionsResult2 permissions=${permissions} grantResults=${grantResults}")
    }

    /*******************************************************
     * {@link OnKeyDown}
     ******************************************************/

    @OnKeyDown(keyCode = [KeyEvent.ACTION_DOWN])
    fun onKeyDown1(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyDown1 keyCode=${keyCode} event=${event}")
        return false
    }

    @OnKeyDown(keyCode = [KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK])
    fun onKeyDown2(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyDown2 keyCode=${keyCode} event=${event}")
        return true
    }

    /*******************************************************
     * {@link OnKeyLongPress}
     ******************************************************/
    @OnKeyLongPress(keyCode = [KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK])
    fun onKeyLongPress1(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyLongPress1 keyCode=${keyCode} event=${event}")
        return false
    }

    @OnKeyLongPress(keyCode = [KeyEvent.ACTION_DOWN])
    fun onKeyLongPress2(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyLongPress2 keyCode=${keyCode} event=${event}")
        return false
    }

    /*******************************************************
     * {@link OnKeyMultiple}
     ******************************************************/

    @OnKeyMultiple(keyCode = [KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK])
    fun OnKeyMultiple1(keyCode: Int, repeatCount: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "OnKeyMultiple1 keyCode=${keyCode} repeatCount=${repeatCount} event=${event}")
        return false
    }

    @OnKeyMultiple(keyCode = [KeyEvent.ACTION_DOWN])
    fun OnKeyMultiple2(keyCode: Int, repeatCount: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "OnKeyMultiple2 keyCode=${keyCode} repeatCount=${repeatCount} event=${event}")
        return false
    }

    /*******************************************************
     * {@link OnKeyShortcut}
     ******************************************************/

    @OnKeyShortcut(keyCode = [KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK])
    fun OnKeyShortcut1(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "OnKeyShortcut1 keyCode=${keyCode} event=${event}")
        return false
    }

    @OnKeyShortcut(keyCode = [KeyEvent.ACTION_DOWN])
    fun OnKeyShortcut2(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "OnKeyShortcut2 keyCode=${keyCode} event=${event}")
        return false
    }

    /*******************************************************
     * {@link OnKeyUp}
     ******************************************************/

    @OnKeyUp(keyCode = [KeyEvent.KEYCODE_Y])
    fun onKeyUp1(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyUp1 keyCode=${keyCode} event=${event}")
        return false
    }

    @OnKeyUp(keyCode = [KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_Q])
    fun onKeyUp2(keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKeyUp2 keyCode=${keyCode} event=${event}")
        return false
    }

    /*******************************************************
     * {@link OnTrimMemory}
     ******************************************************/

    @OnTrimMemory(level = [Activity.TRIM_MEMORY_RUNNING_MODERATE])
    fun onTrimMemory1(level: Int) {
        Log.i(TAG, "onTrimMemory1 level=${level}")
    }

    @OnTrimMemory(level = [
            Activity.TRIM_MEMORY_BACKGROUND,
            Activity.TRIM_MEMORY_RUNNING_LOW
    ])
    fun onTrimMemory2(level: Int) {
        Log.i(TAG, "onTrimMemory2 level=${level}")
    }
}
