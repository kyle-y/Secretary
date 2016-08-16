package com.example.yxb.secretary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.deroctions.DividerItemlistDraction;

import java.util.Arrays;
import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class NewsFragment extends Fragment{

    private AppBarLayout news_appBar;
    private RecyclerView news_recycleView;

    private List<String> tempStrings = Arrays.asList(new String[]{"我是item_1",
            "我是item_2",
            "我是item_3",
            "我是item_4",
            "我是item_5",
            "我是item_6",
            "我是item_7",
            "我是item_8",
            "我是item_9",
            "我是item_10",
            "我是item_11",
            "我是item_12",
            "我是item_13",
            "我是item_14",});

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);

        news_appBar = (AppBarLayout) view.findViewById(R.id.news_appBar);
        news_recycleView = (RecyclerView) view.findViewById(R.id.news_recycleView);
        news_recycleView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        news_recycleView.setAdapter(new NewsAdapter());
        news_recycleView.addItemDecoration(new DividerItemlistDraction(MyApplication.getContext(), DividerItemlistDraction.VERTICAL_LIST));
        return view;
    }

    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.list_item, null));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textView_news_item.setText(tempStrings.get(position));
        }

        @Override
        public int getItemCount() {
            return tempStrings.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView_news_item;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView_news_item = (TextView) itemView.findViewById(R.id.textView_news_item);
            }
        }
    }
}
