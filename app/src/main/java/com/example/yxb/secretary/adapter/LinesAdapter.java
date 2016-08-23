package com.example.yxb.secretary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/19
 * MODIFY_BY:
 */
public class LinesAdapter extends BaseAdapter{
    private List<String> list;

    public LinesAdapter(List<String> list) {
        this.list = list;
    }

    public void update(List<String> newList){
        this.list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_line, null);
            viewHolder = new ViewHolder();
            viewHolder.item_text_line = (TextView) convertView.findViewById(R.id.item_text_line);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String tempStr = list.get(position);
        if (tempStr.equals("")){
            viewHolder.item_text_line.setText("没有相关信息！");
        }else{
            viewHolder.item_text_line.setText(tempStr);
        }
        return convertView;
    }

    class ViewHolder{
        TextView item_text_line;
    }
}
