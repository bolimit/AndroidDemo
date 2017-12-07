package com.marco.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.marco.demo.service.MessengerService;
import com.marco.demo.ui.MessengerServiceActivity;
import com.marco.demo.ui.SampleRemoteServiceActivity;
import com.marco.demo.ui.ViewDragHelperActivity;
import com.marco.demo.ui.adapter.MainAdapter;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangbo
 */

public class MainActivity extends Activity {

    private RecyclerView mRecyclerView;
    private Map<Integer, ActionDisplayPair> mActionMap;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mActionMap = new HashMap<>(10);
        mActionMap.put(Integer.valueOf(0), new ActionDisplayPair("ViewDragHelper Demo", ViewDragHelperActivity.class));
        mActionMap.put(Integer.valueOf(1), new ActionDisplayPair("SampleRemoteService Demo", SampleRemoteServiceActivity.class));
        mActionMap.put(Integer.valueOf(2), new ActionDisplayPair("MessengerService Demo", MessengerServiceActivity.class));
        mActionMap.put(Integer.valueOf(3), new ActionDisplayPair("ViewDragHelper Demo", ViewDragHelperActivity.class));
        mActionMap.put(Integer.valueOf(4), new ActionDisplayPair("ViewDragHelper Demo", ViewDragHelperActivity.class));


        mAdapter = new MainAdapter(MainActivity.this, mActionMap);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }


    public static class ActionDisplayPair {
        private String title;
        private Class cls;

        public ActionDisplayPair(String title, Class cls) {
            this.title = title;
            this.cls = cls;
        }

        public String getTitle() {
            return title;
        }

        public Class getCls() {
            return cls;
        }
    }
}
