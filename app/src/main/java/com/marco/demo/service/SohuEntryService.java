package com.marco.demo.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/5/7
 */

public class SohuEntryService extends BroadcastReceiver {

    private static final String TAG = "SohuEntryService";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
    }
}
