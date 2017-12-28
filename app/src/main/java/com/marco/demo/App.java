package com.marco.demo;

import android.app.Application;
import android.util.Log;

import com.marco.demo.system.SystemLifecycle;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2017/12/28
 */

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        SystemLifecycle.getInstance().initiate(App.this);
        SystemLifecycle.getInstance().setOnSystemLifecycleListener(new SystemLifecycle.OnSystemLifecycleListener() {
            @Override
            public void onEnterBackground(long stayTimeMs) {
                Log.i(TAG, "SystemLifecycle->onEnterBackground : " + stayTimeMs);
            }

            @Override
            public void onEnterForeground() {
                Log.i(TAG, "SystemLifecycle->onEnterForeground!");
            }

            @Override
            public void onAppInstalledActivate() {
                Log.i(TAG, "SystemLifecycle->onAppInstalledActivate!");
            }

            @Override
            public void onLauncherStarted() {
                Log.i(TAG, "SystemLifecycle->onLauncherStarted!");
            }
        });

    }
}
