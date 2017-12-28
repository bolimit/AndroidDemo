package com.marco.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.marco.demo.R;

/**
 * @author yangbo
 */

public class ContentProviderActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider_example);
    }
}
