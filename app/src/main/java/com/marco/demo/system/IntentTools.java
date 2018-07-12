package com.marco.demo.system;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.marco.demo.ui.ActionProtocolSenderActivity;

import java.util.List;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/7/12
 */

public class IntentTools {

    private static final int RUNNING_SERVICES_MAX_NUM = 100;

    /**
     * 判断intent指定的服务是否注册
     *
     * @param intent
     * @return
     */
    public static boolean isServiceIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList.size() != 0) {
            return true;
        }
        return false;
    }

    public static boolean isActivityIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList.size() != 0) {
            return true;
        }
        return false;
    }

    public static boolean checkActivityIntentAvailableWithPackageName(Context context, Intent intent, String matchPackageName) {
        if (StringUtils.isBlank(matchPackageName)) {
            return false;
        }

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList == null || resolveInfoList.size() == 0) {
            return false;
        }

        for (ResolveInfo info : resolveInfoList) {
            if (info != null && info.activityInfo != null) {
                String processName = info.activityInfo.processName;
                if (StringUtils.isNotBlank(processName) && processName.contains(matchPackageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkServiceIntentAvailableWithPackageName(Context context, Intent intent, String matchPackageName) {
        if (StringUtils.isBlank(matchPackageName)) {
            return false;
        }

        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(intent,
                PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
        if (resolveInfoList == null || resolveInfoList.size() == 0) {
            return false;
        }

        for (ResolveInfo info : resolveInfoList) {
            if (info != null && info.serviceInfo != null) {
                String processName = info.serviceInfo.processName;
                if (StringUtils.isNotBlank(processName) && processName.contains(matchPackageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean printRunningServices(Context context) {
        if (context == null) {
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(RUNNING_SERVICES_MAX_NUM);
        if (runningServices != null && runningServices.size() > 0) {
            Log.d(ActionProtocolSenderActivity.TAG, "RunningServiceInfo size is :" + runningServices.size());
            for (ActivityManager.RunningServiceInfo info : runningServices) {
                Log.d(ActionProtocolSenderActivity.TAG, "RunningServiceInfo Process Name:" + info.process);
            }
        }

        return false;
    }

    public static boolean checkServiceIsRunningWithPackageName(Context context, String matchPackageName) {
        if (context == null) {
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(RUNNING_SERVICES_MAX_NUM);
        if (runningServices != null && runningServices.size() > 0) {
            for (ActivityManager.RunningServiceInfo info : runningServices) {
                String processName = info.process;
                if (StringUtils.isNotBlank(processName) && processName.contains(matchPackageName)) {
                    return true;
                }
            }
        }

        return false;
    }

}
