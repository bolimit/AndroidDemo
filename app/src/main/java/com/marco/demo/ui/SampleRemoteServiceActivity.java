package com.marco.demo.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marco.demo.ISampleRemoteService;
import com.marco.demo.ISampleRemoteServiceCallback;
import com.marco.demo.R;
import com.marco.demo.model.Config;
import com.marco.demo.service.SampleRemoteService;

/**
 * @author yangbo
 */

public class SampleRemoteServiceActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SampleRemoteService";

    private TextView mInfoText;
    private Button mBindButton, mUnBindButton, mSendButton;

    private ISampleRemoteService mRemoteService;
    private IBinder mBinder;
    private boolean isBound = false;
    private ISampleRemoteServiceCallback mCallback = new ISampleRemoteServiceCallback.Stub() {
        @Override
        public void onValueChanged(int value) throws RemoteException {
            Log.i(TAG, "Process:" + Process.myPid() + " , " + "SampleRemoteServiceActivity -> onValueChanged responsed, value is: " + value);
            mInfoText.setText("Callback received, value is " + value);
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i(TAG, "SampleRemoteServiceActivity -> binderDied");

            mInfoText.setText("Service Died!");
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.i(TAG, "Process:" + Process.myPid() + " , " + "SampleRemoteServiceActivity -> onServiceConnected");

            mInfoText.setText("Service Connected!");
            try {
                mBinder = binder;
                binder.linkToDeath(mDeathRecipient, 0);
                mRemoteService = ISampleRemoteService.Stub.asInterface(binder);
                mRemoteService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        /**
         * unBind时不会调用，服务器因为异常情况，导致连接断开时会回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Process:" + Process.myPid() + " , " + "SampleRemoteServiceActivity -> onServiceDisconnected");
            mInfoText.setText("Service Disconnected!");
            disConnectService();
        }
    };

    private void disConnectService() {
        try {
            if (mRemoteService != null) {
                mRemoteService.unregisterCallback(mCallback);
            }
            mRemoteService = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (mBinder != null) {
            mBinder.unlinkToDeath(mDeathRecipient, 0);
            mBinder = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_remote_service);

        mInfoText = (TextView) findViewById(R.id.text_info);
        mBindButton = (Button) findViewById(R.id.btn_bind);
        mUnBindButton = (Button) findViewById(R.id.btn_unbind);
        mSendButton = (Button) findViewById(R.id.btn_sendMessage);

        mBindButton.setOnClickListener(this);
        mUnBindButton.setOnClickListener(this);
        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_bind) {
            doBindService();
        } else if (id == R.id.btn_unbind) {
            doUnbindService();
        } else if (id == R.id.btn_sendMessage) {
            mInfoText.setText("Message Send!");
            doSendMsg();
        }
    }

    private void doBindService() {
        isBound = true;
        Intent intent = new Intent(SampleRemoteServiceActivity.this, SampleRemoteService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        if (isBound && mRemoteService != null) {
            isBound = false;
            unbindService(mConnection);
            disConnectService();
            mInfoText.setText("Service Disconnected!");
        }
    }

    private void doSendMsg() {
        if (!isBound || mRemoteService == null) {
            return;
        }
        Config config = new Config();
        config.setNum(15);
        try {
            mRemoteService.runShadowSocks(config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
