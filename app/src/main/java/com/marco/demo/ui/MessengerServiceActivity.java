package com.marco.demo.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marco.demo.R;
import com.marco.demo.service.MessengerService;

/**
 * @author yangbo
 */

public class MessengerServiceActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MessengerService";
    /**
     * 用于处理服务端发送的信息
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());
    private boolean isBound = false;
    /**
     * 关联远端服务的messenger
     */
    private Messenger mServiceWrapper;
    private TextView mInfoText;
    private Button mBindButton, mUnBindButton, mSendButton;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerServiceActivity -> onServiceConnected");
            mServiceWrapper = new Messenger(service);
            mInfoText.setText("Service Connected!");

            try {
                // 添加监听注册
                Message msg = Message.obtain(null, MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceWrapper.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerServiceActivity -> onServiceDisconnected");
            mServiceWrapper = null;
            mInfoText.setText("Service Disconnected!");
            disConnectService();
        }
    };

    private void disConnectService() {
        if (isBound && mServiceWrapper != null) {
            try {
                Message msg = Message.obtain(null, MessengerService.MSG_UNREGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceWrapper.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        unbindService(mConnection);
        isBound = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_messenger_service);

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
        Intent intent = new Intent(MessengerServiceActivity.this, MessengerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void doUnbindService() {
        if (isBound && mServiceWrapper != null) {
            disConnectService();
            mInfoText.setText("Service Disconnected!");
        }
    }

    private void doSendMsg() {
        if (!isBound || mServiceWrapper == null) {
            return;
        }
        try {
            Message msg = Message.obtain(null, MessengerService.MSG_FROM_CLIENT, 500, 0);
            mServiceWrapper.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理服务端发送的信息
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_FROM_SERVICE:
                    Log.i(TAG, "Process:" + Process.myPid() + " , " + "MessengerServiceActivity -> handleMessage -> MSG_FROM_SERVICE is: " + msg.arg1);
                    mInfoText.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
