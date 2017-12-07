package com.marco.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangbo on 2017/12/7.
 */

public class MessengerService extends Service {

    public static final int MSG_REGISTER_CLIENT = 0X001;
    public static final int MSG_UNREGISTER_CLIENT = 0X010;
    /**
     * Client发到Service的消息
     */
    public static final int MSG_FROM_CLIENT = 0X100;
    /**
     * Service发到Client的消息
     */
    public static final int MSG_FROM_SERVICE = 0X101;
    private static final String TAG = "MessengerService";
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private List<Messenger> mClients = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> onBind");
        return mMessenger.getBinder();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> onDestroy");
        super.onDestroy();
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> handleMessage -> MSG_REGISTER_CLIENT");
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> handleMessage -> MSG_UNREGISTER_CLIENT");
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_FROM_CLIENT:
                    Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> handleMessage -> MSG_FROM_CLIENT : " + msg.arg1);
                    for (Messenger client : mClients) {
                        try {
                            Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerService -> sendMessage -> MSG_FROM_SERVICE : " + 1001);
                            // Service发给Client的含有1001的消息
                            client.send(Message.obtain(null, MSG_FROM_SERVICE, 1001, 0));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            mClients.remove(client);
                            Log.i(TAG, "MessengerService -> handleMessage -> MSG_HANDLE,RemoteException");
                        }
                    }

                default:
                    super.handleMessage(msg);
            }
        }
    }
}
