package com.marco.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.marco.demo.ISampleRemoteService;
import com.marco.demo.ISampleRemoteServiceCallback;
import com.marco.demo.model.Config;

/**
 * @author yangbo
 */

public class SampleRemoteService extends Service {
    private static final String TAG = "SampleRemoteService";

    private final RemoteCallbackList<ISampleRemoteServiceCallback> mCallBacks = new RemoteCallbackList<>();

    private final ISampleRemoteService.Stub mBinder = new ISampleRemoteService.Stub() {
        @Override
        public int getState() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public void registerCallback(ISampleRemoteServiceCallback callback) throws RemoteException {
            Log.i(TAG, "registerCallback");
            if (callback != null) {
                mCallBacks.register(callback);
            }
        }

        @Override
        public void unregisterCallback(ISampleRemoteServiceCallback callback) throws RemoteException {
            Log.i(TAG, "unregisterCallback");
            if (callback != null) {
                mCallBacks.unregister(callback);
            }
        }

        @Override
        public void runShadowSocks(Config config) throws RemoteException {
            Log.i(TAG, "runShadowSocks: num in Config is : " + config.getNum());
            notifyCallBack(config);
        }
    };

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        mCallBacks.kill();
    }

    private void notifyCallBack(Config config) {
        int size = mCallBacks.beginBroadcast();

        Log.i(TAG, "notifyCallBack, size is :" + size);

        for (int i = 0; i < size; i++) {
            try {
                mCallBacks.getBroadcastItem(i).onValueChanged(config.getNum());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallBacks.finishBroadcast();
    }
}
