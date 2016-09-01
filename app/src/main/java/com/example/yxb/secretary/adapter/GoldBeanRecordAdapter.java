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
 * CREATE_TIME:2016/8/31
 * MODIFY_BY:
 */
public class GoldBeanRecordAdapter extends BaseAdapter{

    private List<Map<String, Object>> list;

    public GoldBeanRecordAdapter(List<Map<String, Object>> list) {
        this.list = list;
    }

    public void updateData(List<Map<String, Object>> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
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
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_goldbeanrecord, null);
            viewHolder = new ViewHolder();
            viewHolder.text_record_type = (TextView) convertView.findViewById(R.id.text_record_type);
            viewHolder.text_record_num = (TextView) convertView.findViewById(R.id.text_record_num);
            viewHolder.text_record_time = (TextView) convertView.findViewById(R.id.text_record_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text_record_type.setText(list.get(position).get("type").toString());
        viewHolder.text_record_num.setText(list.get(position).get("num").toString());
        viewHolder.text_record_time.setText(list.get(position).get("time").toString());
        return convertView;
    }

    class ViewHolder{
        TextView text_record_type, text_record_num, text_record_time;
    }
}
