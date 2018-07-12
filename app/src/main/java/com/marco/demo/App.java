package com.marco.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.marco.demo.service.ActivatorService;

import java.util.List;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2017/12/28
 */

public class App extends Application {
    private static final String TAG = "App";


    @Override
    public void onCreate() {
        super.onCreate();
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
        if (runningAppProcesses != null && runningAppProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo process : runningAppProcesses) {
                if (process.pid == pid) {
                    processName = process.processName;
                    break;
                }
            }
        }


        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(10);
        if (runningServices != null && runningServices.size() > 0) {
            for (ActivityManager.RunningServiceInfo info : runningServices) {
                Log.d(ActivatorService.TAG, "RunningServiceInfo->name = " + info.process);
            }
        }

        Log.d(ActivatorService.TAG, "processName = " + processName);

    }
}
