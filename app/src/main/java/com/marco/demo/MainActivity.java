package com.marco.demo;

import android.app.Activity;
import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

/**
 * @author yangbo
 * Created by yangbo on 2017/12/4.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
