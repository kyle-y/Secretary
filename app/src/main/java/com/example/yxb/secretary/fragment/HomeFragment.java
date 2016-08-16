package com.example.yxb.secretary.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import java.util.Arrays;
import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class HomeFragment extends Fragment{
    private RecyclerView home_recyclerView;
    private List<String> tittles = Arrays.asList(new String[]{"手机归属地","黄历","违章查询",
            "星座运势","单位换算","驾考题库","计算器","火车票查询","IP地址查询","区号查询",
            "微信热门","笑话段子","汇率转换","语音合成","彩票","名人名言","历史今天",});

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        home_recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        home_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        home_recyclerView.setAdapter(new StraggerAdapter(MyApplication.getContext(),tittles));
        return view;
    }

    private List<Integer> mHeights;
    class StraggerAdapter extends RecyclerView.Adapter<StraggerAdapter.StraggerViewHolder>{

        public StraggerAdapter(Context context, List<String> datas)
        {

            mHeights = new ArrayList();
            for (int i = 0; i < tittles.size(); i++)
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
        public void onBindViewHolder(StraggerViewHolder holder, int position) {
            ViewGroup.LayoutParams lp = holder.textview_home_item.getLayoutParams();
            lp.height = mHeights.get(position);
            lp.width = getWindowsWidth() / 3;
            holder.textview_home_item.setLayoutParams(lp);
            holder.textview_home_item.setText(tittles.get(position));
            holder.textview_home_item.setBackground(new ColorDrawable(getColor()));
            holder.textview_home_item.setGravity(Gravity.CENTER);
        }

        @Override
        public int getItemCount() {
            return tittles.size();
        }


        class StraggerViewHolder extends RecyclerView.ViewHolder{
            TextView textview_home_item;
            public StraggerViewHolder(View itemView) {
                super(itemView);
                textview_home_item = (TextView) itemView.findViewById(R.id.textview_home_item);
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



}
