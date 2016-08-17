package com.example.yxb.secretary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/16
 * MODIFY_BY:
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyViewHolder>{
    private List<String> dataList;

    public MyNewsAdapter(List<String> dataList) {
        this.dataList = dataList;
    }
    private void updateData(List<String> list){
        this.dataList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView_news_item.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView_news_item;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView_news_item = (TextView) itemView.findViewById(R.id.textView_news_item);
        }
    }
}
