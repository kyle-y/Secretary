package com.example.yxb.secretary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yxb.secretary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/14.
 */
public class ProvinceAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater;
    private List<Map<String, Object>> listItemProvince;

    public ProvinceAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        listItemProvince = new ArrayList<>();
    }
    public void setListData(List<Map<String, Object>> listItemProvince){
        if (listItemProvince != null){
            this.listItemProvince = listItemProvince;
        }else{
            this.listItemProvince = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listItemProvince.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return listItemProvince.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.item_list_cities, null);
            viewHolder.textview_item = (TextView) convertView.findViewById(R.id.textview_item);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textview_item.setText(listItemProvince.get(position).get("name").toString());

        return convertView;
    }

    class ViewHolder{
        TextView textview_item;
    }
}
