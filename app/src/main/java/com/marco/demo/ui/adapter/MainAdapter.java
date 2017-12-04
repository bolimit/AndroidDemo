package com.marco.demo.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marco.demo.MainActivity;
import com.marco.demo.R;

import java.util.Map;

/**
 * @author yangbo
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    private Context mContext;
    private Map<Integer, MainActivity.ActionDisplayPair> mDataMap;

    public MainAdapter(Context context, Map<Integer, MainActivity.ActionDisplayPair> dataMap) {
        mContext = context;
        mDataMap = dataMap;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainViewHolder holder = new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_main, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.textView.setText(mDataMap.get(Integer.valueOf(position)).getTitle());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, mDataMap.get(Integer.valueOf(position)).getCls());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataMap.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MainViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
