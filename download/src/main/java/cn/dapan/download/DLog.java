package cn.dapan.download;

import android.util.Log;

public class DLog {

    private static final String TAG = "DLog";
    private static final boolean DEBUG = true;

    private static String prefix() {
        return "[" + Thread.currentThread().getName() + "]，";
    }

    public static void d(String msg) {
        if (DEBUG) {
            msg = prefix() + msg;
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            msg = prefix() + msg;
            Log.e(TAG, msg);
        }
    }
}
