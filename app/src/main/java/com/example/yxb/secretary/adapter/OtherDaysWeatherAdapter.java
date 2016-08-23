package com.example.yxb.secretary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

import java.util.List;
import java.util.Map;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/16
 * MODIFY_BY:
 */
public class OtherDaysWeatherAdapter extends RecyclerView.Adapter<OtherDaysWeatherAdapter.MyViewHolder>{
    private List<Map<String,String>> dataList;

    public OtherDaysWeatherAdapter(List<Map<String,String>> dataList) {
        this.dataList = dataList;
    }
    public void updateData(List<Map<String,String>> list){
        this.dataList = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_list_weather, null));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text_sub_time.setText(dataList.get(position).get("time"));
        holder.text_sub_weather.setText(dataList.get(position).get("weather"));
        holder.text_sub_tem.setText(dataList.get(position).get("tem"));
        holder.text_sub_wind.setText(dataList.get(position).get("wind"));
        holder.text_wind_level.setText(dataList.get(position).get("wind_level"));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_sub_time,text_sub_weather,text_sub_tem,text_sub_wind,text_wind_level;
        public MyViewHolder(View itemView) {
            super(itemView);
            text_sub_time = (TextView) itemView.findViewById(R.id.text_sub_time);
            text_sub_weather = (TextView) itemView.findViewById(R.id.text_sub_weather);
            text_sub_tem = (TextView) itemView.findViewById(R.id.text_sub_tem);
            text_sub_wind = (TextView) itemView.findViewById(R.id.text_sub_wind);
            text_wind_level = (TextView) itemView.findViewById(R.id.text_wind_level);
        }
    }
}
