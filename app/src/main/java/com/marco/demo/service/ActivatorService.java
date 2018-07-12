package com.marco.demo.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/7/4
 */

public class ActivatorService extends IntentService {
    public static final String TAG = "ActivatorService";

    private AtomicBoolean mProcessing = new AtomicBoolean(false);

    public ActivatorService(String name) {
        super(name);
    }

    public ActivatorService() {
        super("ActivatorService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (mProcessing.compareAndSet(false, true)) {
            parseIntent(intent);

            mProcessing.set(false);
        }
    }

    private void parseIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        String dataPath = intent.getDataString();
        if (TextUtils.isEmpty(dataPath)) {
            return;
        }

        Log.d(TAG, dataPath);


        String intentFrom = "";

        try {
            Uri uri = intent.getData();
            intentFrom = uri.getQueryParameter("from");
            Log.d(TAG, "from = " + intentFrom);

            Log.d(TAG, "sv_action_extra = " + intent.getStringExtra("sv_action_extra"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
