package com.marco.demo.system;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.marco.demo.LaunchActivity;
import com.marco.demo.MainActivity;

import java.util.List;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/5/2
 */

public abstract class SystemLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    // 通过判断LaunchActivity的Intent是否还有startType的值，来判断是否是点击Launcher启动
    public static final String LAUNCH_ACTIVITY_START_TYPE_KEY = "startType";
    public static final String LAUNCH_ACTIVITY_START_TYPE_VALUE = "not_launch_system";
    private static final long ONE_HOUR = 60 * 60 * 1000L;
    private static final long ONE_MONTH = 30 * 24 * 60 * 60 * 1000L;
    private static final String TAG = "SystemLifecycle";
    private int mCount = 0;

    // 是否首次安装
    private boolean mAppFirstInstalled = true;
    // 进入前台的时间点
    private long mAppForeTime = 0L;
    // 进入后台的时间点
    private long mAppBackTime = 0L;
    // APP是否正在后台
    private boolean mIsAppBackgroundState = true;

    /**
     * 应用进入后台
     *
     * @param stayTimeMs 在前台保持的时间，单位ms
     */
    public abstract void onEnterBackground(long stayTimeMs);

    /**
     * 应用进入前台
     */

    public abstract void onEnterForeground();

    /**
     * 应用首次安装使用
     */

    public abstract void onAppInstalledActivate();

    /**
     * 启动应用
     */

    public abstract void onLauncherStarted();


    /**
     * 后台超过1小时，重新显示启动图
     */
    public abstract void onReDisplayLauncher();

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.d(TAG, activity.getLocalClassName() + " onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {

        Log.d(TAG, activity.getLocalClassName() + " onActivityStarted");
        if (mCount == 0) {
            // 切到前台
            mIsAppBackgroundState = false;
            mAppForeTime = System.currentTimeMillis();

            // 判断是否首次安装
            if (mAppFirstInstalled) {
                // 判断SP里面的值
//                if (BasePreferenceTools.isFirstInstallSohuvideoApp(SohuApplication.getInstance().getApplicationContext())) {
//                    BasePreferenceTools.updateFirstInstallSohuvideoApp(SohuApplication.getInstance().getApplicationContext(), false);
//                    onAppInstalledActivate();
//                }

                mAppFirstInstalled = false;
            }

            // 变量记录主要用于控制 onenterforeground() 和 onReDisplayLauncher()调用时序
            boolean displayLauncher = false;

            // 判断是否是点击Launcher启动
            if (activity instanceof LaunchActivity) {
                Intent intent = activity.getIntent();
                String stringExtra = intent.getStringExtra(LAUNCH_ACTIVITY_START_TYPE_KEY);
                if (!LAUNCH_ACTIVITY_START_TYPE_VALUE.equals(stringExtra)) {
                    onLauncherStarted();
                }
            } else {
                // 在else可排除在启动页重复拉起启动页的情况
                // 判断是否需要按照1小时策略拉起启动页
                final long millisPassed = mAppBackTime != 0 ? mAppForeTime - mAppBackTime : 0;
                if (millisPassed >= ONE_HOUR && millisPassed <= ONE_MONTH) {
                    // 再次显示启动图广告
                    displayLauncher = true;
                }
            }

            onEnterForeground();

            if (displayLauncher) {
                Log.d(TAG, "onReDisplayLauncher");
                onReDisplayLauncher();
            }
        }
        mCount++;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " onActivityStopped");
        mCount--;
        if (mCount == 0) {
            // 切到后台
            mIsAppBackgroundState = true;
            mAppBackTime = System.currentTimeMillis();
            final long millisPassed = mAppForeTime != 0 ? mAppBackTime - mAppForeTime : 0;
            onEnterBackground(millisPassed);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " onActivityPaused");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.d(TAG, activity.getLocalClassName() + " onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, activity.getLocalClassName() + " onActivityDestroyed");
    }

    public boolean isMainActivityInTasks(Context context) {
        final String mainActivityClsName = MainActivity.class.getName();
        Log.d(TAG, "mainActivityClsName = " + mainActivityClsName);

        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
            if (info != null && !(info.isEmpty())) {
                int size = info.size();
                for (int i = 0; i < size; i++) {
                    String baseClsName = info.get(i).baseActivity.getClassName();
                    if (mainActivityClsName.equals(baseClsName)) {
                        Log.d(TAG, "isMainActivityInTasks = true");
                        return true;
                    }
                }
            }
        }

        Log.d(TAG, "isMainActivityInTasks = false");
        return false;
    }

    /**
     * APP是否运行在后台
     *
     * @return
     */
    public boolean isAppBackgroundState() {
        return mIsAppBackgroundState;
    }
}
