package com.marco.demo;

import android.app.Application;
import android.util.Log;

import com.marco.demo.system.SystemLifecycleCallbacks;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2017/12/28
 */

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
//        SystemLifecycle.getInstance().initiate(App.this);
//        SystemLifecycle.getInstance().setOnSystemLifecycleListener(new SystemLifecycle.OnSystemLifecycleListener() {
//            @Override
//            public void onEnterBackground(long stayTimeMs) {
//                Log.i(TAG, "SystemLifecycle->onEnterBackground : " + stayTimeMs);
//            }
//
//            @Override
//            public void onEnterForeground() {
//                Log.i(TAG, "SystemLifecycle->onEnterForeground!");
//            }
//
//            @Override
//            public void onAppInstalledActivate() {
//                Log.i(TAG, "SystemLifecycle->onAppInstalledActivate!");
//            }
//
//            @Override
//            public void onLauncherStarted() {
//                Log.i(TAG, "SystemLifecycle->onLauncherStarted!");
//            }
//        });

//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//                Log.v(TAG, activity.getClass().getSimpleName() + " onActivityStarted");
//                if (count == 0) {
//                    Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
//                }
//                count++;
//
//                Log.v(TAG, activity.getClass().getSimpleName() + " onActivityStarted, count = " + count);
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivityStopped");
//                count--;
//                if (count == 0) {
//                    Log.v(TAG, ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
//                }
//
//                Log.v(TAG, activity.getLocalClassName() + " onActivityStopped, count = " + count);
//            }
//
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivitySaveInstanceState");
//            }
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivityResumed");
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivityPaused");
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivityDestroyed");
//            }
//
//            @Override
//            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                Log.v(TAG, activity.getLocalClassName() + " onActivityCreated, CallingActivity: " + activity.getCallingActivity());
//            }
//        });

        Log.e(TAG, "xxxxxxxx");
        registerActivityLifecycleCallbacks(new SystemLifecycleCallbacks() {
            @Override
            public void onEnterBackground(long stayTimeMs) {
                Log.v(TAG, "onEnterBackground, stayTimeMs is: " + stayTimeMs);
            }

            @Override
            public void onEnterForeground() {
                Log.v(TAG, "onEnterForeground");
            }

            @Override
            public void onAppInstalledActivate() {
                Log.v(TAG, "onAppInstalledActivate");
            }

            @Override
            public void onLauncherStarted() {
                Log.v(TAG, "onLauncherStarted");
            }

            @Override
            public void onReDisplayLauncher() {
                Log.v(TAG, "onReDisplayLauncher");
            }
        });

    }
}
