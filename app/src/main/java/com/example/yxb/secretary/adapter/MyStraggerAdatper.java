package com.example.yxb.secretary.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/16
 * MODIFY_BY:
 */
public class MyStraggerAdatper extends RecyclerView.Adapter<MyStraggerAdatper.StraggerViewHolder>{
    private List<Integer> mHeights;
    private List<String> dataList;

    public interface OnItemClickListener{
        void onItemClickListener(View view, int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClickListener(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public MyStraggerAdatper(Context context, List<String> datas){
        this.dataList = datas;
        mHeights = new ArrayList();
        for (int i = 0; i < dataList.size(); i++)
        {
            mHeights.add( (int) (100 + Math.random() * 300));
        }
    }
    @Override
    public StraggerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StraggerViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.stragger_grid_item, parent,false));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final StraggerViewHolder holder, int position) {
        ViewGroup.LayoutParams lp = holder.textview_home_item.getLayoutParams();
        lp.height = mHeights.get(position);
        lp.width = getWindowsWidth() / 2;
        holder.textview_home_item.setLayoutParams(lp);
        holder.textview_home_item.setText(dataList.get(position));
        holder.cardView.setCardBackgroundColor(getColor());
        holder.textview_home_item.setGravity(Gravity.CENTER);

        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClickListener(holder.itemView,pos);
                }
            });
        }
        if (onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemLongClickListener.onItemLongClickListener(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    class StraggerViewHolder extends RecyclerView.ViewHolder{
        TextView textview_home_item;
        CardView cardView;
        public StraggerViewHolder(View itemView) {
            super(itemView);
            textview_home_item = (TextView) itemView.findViewById(R.id.textview_home_item);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }

    public int getColor(){
        int red = (int) (Math.random() * 150 + 30);
        int green = (int) (Math.random() * 150 + 30);
        int blue = (int) (Math.random() * 150 + 30);
        return Color.rgb(red,green,blue);
    }

    public int getWindowsWidth(){
        WindowManager manager = (WindowManager) MyApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}
