package com.marco.demo.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marco.demo.R;
import com.marco.demo.system.IntentTools;

import java.util.List;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/7/12
 */

public class ActionProtocolSenderActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "ActionProtocolTest";
    public static final String ACTION_URL_3 = "svaction://action.cmd?openservice://open_refer=sogou_app";
    private static final String ACTION_URL_1 = "sohuvideo://action.cmd?action=1.33&enterid=sogou_app";
    private static final String ACTION_URL_2 = "sohunews://pr/openservice://open_refer=6";
    private TextView textView1, textView2, textView3;

    private Button button1, button2, button3;

    /**
     * 隐式intent转为显式intent，note：5.0以上系统必须使用显示intent
     *
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfoList = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfoList == null || resolveInfoList.size() != 1) {
            return null;
        }
        ResolveInfo resolveInfo = resolveInfoList.get(0);
        String packageName = resolveInfo.serviceInfo.packageName;
        String className = resolveInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_protocol_sender);

        IntentTools.printRunningServices(this);

        // 判断新闻APK的服务是否活着
        if (IntentTools.checkServiceIsRunningWithPackageName(this, "com.sohu.newsclient")) {
            Log.d(TAG, "新闻客户端后台活跃");
        }
        textView1 = (TextView) findViewById(R.id.action_url_text1);
        button1 = (Button) findViewById(R.id.action_btn1);
        textView2 = (TextView) findViewById(R.id.action_url_text2);
        button2 = (Button) findViewById(R.id.action_btn2);
        textView3 = (TextView) findViewById(R.id.action_url_text3);
        button3 = (Button) findViewById(R.id.action_btn3);

        textView1.setText(ACTION_URL_1);
        button1.setOnClickListener(this);

        textView2.setText(ACTION_URL_2);
        button2.setOnClickListener(this);

        textView3.setText(ACTION_URL_3);
        button3.setOnClickListener(this);

    }

    private void sendActivityAction1() {
        String url = "sohuvideo://action.cmd?action=1.33&enterid=sogou_app";
        Intent intent = new Intent(Intent.ACTION_DEFAULT);
        intent.setData(Uri.parse(url));
        if (IntentTools.isActivityIntentAvailable(ActionProtocolSenderActivity.this, intent)) {
            startActivity(intent);
        }
    }

    private void sendServiceAction2() {
        boolean isNewsAlive = IntentTools.checkServiceIsRunningWithPackageName(this, "com.sohu.newsclient");
        if (!isNewsAlive) {
            // 如果后台不活跃，才去拉活
            Intent intent = new Intent(Intent.ACTION_DEFAULT);
            intent.setData(Uri.parse(ACTION_URL_2));
            if (IntentTools.checkServiceIntentAvailableWithPackageName(this, intent, "com.sohu.newsclient")) {
                Intent newIntent = getExplicitIntent(this, intent);
                if (newIntent != null) {
                    startService(newIntent);
                }
            }
        }
    }

    private void sendServiceAction3() {
        String url = "svaction://action.cmd?openservice://open_refer=sogou_app";
        Intent intent = new Intent(Intent.ACTION_DEFAULT);
        intent.setData(Uri.parse(url));
        if (IntentTools.checkServiceIntentAvailableWithPackageName(this, intent, "com.sohu.sohuvideo")) {
            Intent newIntent = getExplicitIntent(this, intent);
            if (newIntent != null) {
                startService(newIntent);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.action_btn1: {
                sendActivityAction1();
                break;
            }

            case R.id.action_btn2: {
                sendServiceAction2();
                break;
            }

            case R.id.action_btn3: {
                sendServiceAction3();
                break;
            }
        }
    }
}
