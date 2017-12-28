package com.marco.demo.system;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.marco.demo.MainActivity;

/**
 * @author yangbo
 *         用于监测应用的运行状态，是前台，还是后台运行
 */

public class SystemLifecycle {
    private static final int MSG = 1;
    private static final int MSG_DELAY_TIME = 600;
    private int mActivityCount = 0;
    private OnSystemLifecycleListener mListener;
    private boolean mCancelled = false;
    private long mAppForeTime = 0;
    private SystemState mLastSystemState = SystemState.STATE_NONE;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            synchronized (SystemLifecycle.this) {
                if (mCancelled) {
                    return;
                }

                if (mLastSystemState == SystemState.STATE_FOREGROUND) {
                    // 进入后台
                    if (mActivityCount <= 0) {
                        // 说明全部退出
                        mLastSystemState = SystemState.STATE_NONE;
                    } else {
                        mLastSystemState = SystemState.STATE_BACKGROUND;
                    }
                    final long millisPassed = mAppForeTime != 0 ? System.currentTimeMillis() - mAppForeTime : 0;
                    enterBackground(millisPassed);
                }
            }
        }
    };
    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ++mActivityCount;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            if (mLastSystemState == SystemState.STATE_FOREGROUND) {
                cancel();
            }

            if (mLastSystemState == SystemState.STATE_NONE) {
                // TODO 修改为FirstNavigationActivityGroup
                if (activity instanceof MainActivity) {
                    launcherActivityStarted();
                    // TODO 取消SharedPreference数据存储
//                    if (BasePreferenceTools.isFirstInstallSohuvideoApp(activity.getApplicationContext())) {
//                        BasePreferenceTools.updateFirstInstallSohuvideoApp(activity.getApplicationContext(), false);
//                        appInstalledActivate();
//                    }
                }
            }

            if (mLastSystemState == SystemState.STATE_NONE || mLastSystemState == SystemState.STATE_BACKGROUND) {
                mAppForeTime = System.currentTimeMillis();
                mLastSystemState = SystemState.STATE_FOREGROUND;
                enterForeground();
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            mCancelled = false;
            mHandler.sendMessageDelayed(Message.obtain(mHandler), MSG_DELAY_TIME);
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            --mActivityCount;
            // 处理退后台以后，最后一个Activity才被Destroy的问题
            if (mActivityCount <= 0 && mLastSystemState == SystemState.STATE_BACKGROUND) {
                mLastSystemState = SystemState.STATE_NONE;
            }
        }
    };

    public static SystemLifecycle getInstance() {
        return HolderClass.INSTANCE;
    }

    public void initiate(Application application) {
        application.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    public void setOnSystemLifecycleListener(OnSystemLifecycleListener listener) {
        mListener = listener;
    }

    private synchronized void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    /**
     * 进入后台
     */
    private void enterBackground(long millsTime) {
        if (mListener != null) {
            mListener.onEnterBackground(millsTime);
        }
    }

    /**
     * 进入前台
     */
    private void enterForeground() {
        if (mListener != null) {
            mListener.onEnterForeground();
        }
    }

    /**
     * 启动应用
     */
    private void launcherActivityStarted() {
        if (mListener != null) {
            mListener.onLauncherStarted();
        }
    }

    /**
     * 首次安装
     */
    private void appInstalledActivate() {
        if (mListener != null) {
            mListener.onAppInstalledActivate();
        }
    }

    public enum SystemState {
        /**
         * init State
         */
        STATE_NONE,
        /**
         * foreGround State
         */
        STATE_FOREGROUND,
        /**
         * backGround State
         */
        STATE_BACKGROUND
    }

    public interface OnSystemLifecycleListener {
        /**
         * 应用进入后台
         *
         * @param stayTimeMs 在前台保持的时间，单位ms
         */
        void onEnterBackground(long stayTimeMs);

        /**
         * 应用进入前台
         */

        void onEnterForeground();

        /**
         * 应用首次安装使用
         */

        void onAppInstalledActivate();

        /**
         * 启动应用
         */

        void onLauncherStarted();
    }

    public final static class HolderClass {
        private final static SystemLifecycle INSTANCE = new SystemLifecycle();
    }
}
