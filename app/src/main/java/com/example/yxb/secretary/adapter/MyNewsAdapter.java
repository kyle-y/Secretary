package com.example.yxb.secretary.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/16
 * MODIFY_BY:
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyViewHolder>{
    private List<Map<String, Object>> dataList;

    public MyNewsAdapter(List<Map<String, Object>> dataList) {
        this.dataList = dataList;
    }
    public void updateData(List<Map<String, Object>> list){
        this.dataList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.news_tittle.setText(dataList.get(position).get("title").toString());
        holder.news_time.setText(dataList.get(position).get("time").toString());
        holder.news_topic.setText(dataList.get(position).get("topic").toString());
        Picasso.with(MyApplication.getContext()).load(dataList.get(position).get("imageurl").toString()).into(holder.news_image);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView news_tittle,news_time,news_topic;
        ImageView news_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            news_tittle = (TextView) itemView.findViewById(R.id.news_tittle);
            news_time = (TextView) itemView.findViewById(R.id.news_time);
            news_topic = (TextView) itemView.findViewById(R.id.news_topic);
            news_image = (ImageView) itemView.findViewById(R.id.news_image);
        }
    }
}
