package com.marco.demo.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.marco.demo.MainActivity;
import com.marco.demo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/7/12
 */

public class PushStyleActivity extends Activity implements View.OnClickListener {

    public final String TAG = "PushStyleActivity";
    private Button button1, button2, button3;
    private TextView textView0, textView1, textView2, textView3;

    @TargetApi(16)
    public void sendResidentNotice(Context context, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //注意：如果不是Ongoing方式，在OPPO-A59m上面无法显示自定义RemoteView方式的通知栏，所以一般这种自定义样式需要做系统区分还有用在下载等模块
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_push_img);
        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_des, content);
        remoteViews.setImageViewBitmap(R.id.iv_img, BitmapFactory.decodeResource(getResources(), R.drawable.img14481));
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.notice_view_type_0, pendingIntent);
        builder.setSmallIcon(R.mipmap.notify_5);


        Notification notification = builder.build();

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
            notification.bigContentView = remoteViews;
        }

        notification.contentView = remoteViews;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1003, notification);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_push_style);

        textView0 = (TextView) findViewById(R.id.push_text0);
        textView1 = (TextView) findViewById(R.id.push_text1);
        button1 = (Button) findViewById(R.id.push_btn1);
        textView2 = (TextView) findViewById(R.id.push_text2);
        button2 = (Button) findViewById(R.id.push_btn2);
        textView3 = (TextView) findViewById(R.id.push_text3);
        button3 = (Button) findViewById(R.id.push_btn3);

        button1.setOnClickListener(this);

        button2.setOnClickListener(this);

        button3.setOnClickListener(this);

        // EMUI,MIUI判断
        String emui = getProperty("ro.build.version.emui", "");
        String miui = getSystemProperty("ro.miui.ui.version.name");
        String res = "EMUI:" + emui + "\nMIUI:" + miui;
        textView0.setText(res);

    }

    /**
     * 判断EMUI
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getProperty(String key, String defaultValue) {
        String value = defaultValue;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, ""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    /**
     * 判断MIUI
     *
     * @param propName
     * @return
     */
    public String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.push_btn1: {
                sendNormalNotification();
                break;
            }

            case R.id.push_btn2: {
                sendBigPictureStyleNotification();
                break;
            }

            case R.id.push_btn3: {
                sendResidentNotice(this, "Test", "TestContent");
                break;
            }
        }
    }

    private void sendNormalNotification() {
        NotificationManager nm =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, ActionProtocolSenderActivity.class), 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.notify_5)
                        // setLargeIcon可选
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("Content Title")
                        .setContentText("Content Text")
                        .setContentIntent(contentIntent);

        nm.notify(1001, builder.build());
    }

    private void sendBigPictureStyleNotification() {
        NotificationManager nm =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, ActionProtocolSenderActivity.class), 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("BigPictureStyle Content Title")
                        .setContentText("BigPictureStyle Content Text,BigPictureStyle Content Text2,BigPictureStyle Content Text3")
                        .setSmallIcon(R.mipmap.notify_5)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setAutoCancel(true);

        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        style.setSummaryText("BigPictureStyle Content Text,BigPictureStyle Content Text2,BigPictureStyle Content Text3 SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.timg640));
        builder.setStyle(style);

        builder.setContentIntent(contentIntent);


        nm.notify(1002, builder.build());
    }
}
