package com.example.yxb.secretary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

import java.util.List;
import java.util.Map;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/19
 * MODIFY_BY:
 */
public class WeatherDetailsAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;

    public WeatherDetailsAdapter(List<Map<String,Object>> list) {
        this.list = list;
    }

    public void update(List<Map<String,Object>> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String,Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_weather_details, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.text_other_name = (TextView) convertView.findViewById(R.id.text_other_name);
            viewHolder.text_other_index = (TextView) convertView.findViewById(R.id.text_other_index);
            viewHolder.text_other_details = (TextView) convertView.findViewById(R.id.text_other_details);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_other_name.setText(list.get(position).get("name").toString());
        viewHolder.text_other_index.setText(list.get(position).get("index").toString());
        viewHolder.text_other_details.setText(list.get(position).get("details").toString());
        return convertView;
    }

    class ViewHolder{
        TextView text_other_name,text_other_index,text_other_details;
    }
}
