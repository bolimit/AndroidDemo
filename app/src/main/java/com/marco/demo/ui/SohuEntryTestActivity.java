package com.marco.demo.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.marco.demo.R;

/**
 * @author boyang116245@sohu-inc.com
 * @date 2018/5/7
 */

public class SohuEntryTestActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sohuentry_test);

        findViewById(R.id.btn_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionUrl = "sohuvideo://action.cmd?action=3.11";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(actionUrl));
                sendBroadcast(intent);
            }
        });

        findViewById(R.id.btn_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://tv.sohu.com/upload/touch/testAction/index.html");
                intent.setData(content_url);
                startActivity(intent);
            }
        });
    }
}
