/**
 * MIT License
 *
 * Copyright (c) 2018 yanbo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cn.yan.hippo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import cn.yan.hippo.inner.IHippoActivityFragment;
import cn.yan.hippo.inner.IHippoBroadcastReceiver;

/**
 * Hippo for user method.
 */

public final class Hippo {
    private static final String SUFFIX = "_HippoProxy";
    private static final String PREFIX_ANDROID = "android.";
    private static final String PREFIX_JAVA = "java.";

    private static final Map<Class<?>, IHippoActivityFragment<?>> S_HIPPO_AFS = new LinkedHashMap<>();
    private static final Map<Class<?>, IHippoBroadcastReceiver<?>> S_HIPPO_BC = new LinkedHashMap<>();

    private Hippo() {
        throw new AssertionError("Hippo can't be instance!");
    }

    public static void onReceive(BroadcastReceiver target, Context context, Intent intent) {
        Class<?> targetClass = target.getClass();
        IHippoBroadcastReceiver hippoBroadcastReceiver = findHippoBroadcastReceiverClass(targetClass);
        if (hippoBroadcastReceiver != null) {
            hippoBroadcastReceiver.onReceive(target, context, intent);
        }
    }

    public static void onActivityResult(Object target, int requestCode, int resultCode, Intent data) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            hippoActivityFragment.onActivityResult(target, requestCode, resultCode, data);
        }
    }

    public static void onRequestPermissionsResult(final Object target, final int requestCode, final String[] permissions, final int[] grantResults) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            hippoActivityFragment.onRequestPermissionsResult(target, requestCode, permissions, grantResults);
        }
    }

    public static void onTrimMemory(final Object target, final int level) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            hippoActivityFragment.onTrimMemory(target, level);
        }
    }

    public static boolean onKeyDown(final Object target, int keyCode, KeyEvent event) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            return hippoActivityFragment.onKeyDown(target, keyCode, event);
        }
        return false;
    }

    public static boolean onKeyUp(final Object target, int keyCode, KeyEvent event) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            return hippoActivityFragment.onKeyUp(target, keyCode, event);
        }
        return false;
    }

    public static boolean onKeyLongPress(final Object target, int keyCode, KeyEvent event) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            return hippoActivityFragment.onKeyLongPress(target, keyCode, event);
        }
        return false;
    }

    public static boolean onKeyMultiple(final Object target, int keyCode, int repeatCount, KeyEvent event) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            return hippoActivityFragment.onKeyMultiple(target, keyCode, repeatCount, event);
        }
        return false;
    }

    public static boolean onKeyShortcut(final Object target, int keyCode, KeyEvent event) {
        Class<?> targetClass = target.getClass();
        IHippoActivityFragment hippoActivityFragment = findHippoActivityFragmentClass(targetClass);
        if (hippoActivityFragment != null) {
            hippoActivityFragment.onKeyShortcut(target, keyCode, event);
        }
        return false;
    }

    private static IHippoActivityFragment findHippoActivityFragmentClass(Class<?> targetClass) {
        IHippoActivityFragment hippoActivityFragment = S_HIPPO_AFS.get(targetClass);
        if (hippoActivityFragment != null) {
            return hippoActivityFragment;
        }

        String className = targetClass.getName();
        if (className.startsWith(PREFIX_ANDROID) || className.startsWith(PREFIX_JAVA)) {
            return null;
        }

        try {
            Class<?> attachClass = Class.forName(className + SUFFIX);
            hippoActivityFragment = (IHippoActivityFragment) attachClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            hippoActivityFragment = findHippoActivityFragmentClass(targetClass.getSuperclass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        S_HIPPO_AFS.put(targetClass, hippoActivityFragment);
        return hippoActivityFragment;
    }

    private static IHippoBroadcastReceiver findHippoBroadcastReceiverClass(Class<?> targetClass) {
        IHippoBroadcastReceiver hippoBroadcastReceiver = S_HIPPO_BC.get(targetClass);
        if (hippoBroadcastReceiver != null) {
            return hippoBroadcastReceiver;
        }

        String className = targetClass.getName();
        if (className.startsWith(PREFIX_ANDROID) || className.startsWith(PREFIX_JAVA)) {
            return null;
        }

        try {
            Class<?> attachClass = Class.forName(className + SUFFIX);
            hippoBroadcastReceiver = (IHippoBroadcastReceiver) attachClass.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            hippoBroadcastReceiver = findHippoBroadcastReceiverClass(targetClass.getSuperclass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        S_HIPPO_BC.put(targetClass, hippoBroadcastReceiver);
        return hippoBroadcastReceiver;
    }
}
