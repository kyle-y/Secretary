package com.example.yxb.secretary.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.MainActivity;
import com.example.yxb.secretary.adapter.MyNewsAdapter;
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
    private MainActivity mActivity;
    private LinearLayout layout_buttons;
    private boolean isHide = false;

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
        news_recycleView.setAdapter(new MyNewsAdapter(tempStrings));
        news_recycleView.addItemDecoration(new DividerItemlistDraction(MyApplication.getContext(), DividerItemlistDraction.VERTICAL_LIST));

        mActivity = (MainActivity) getActivity();
        layout_buttons = mActivity.layout_buttons;
        news_recycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && dy > 10 && !isHide){
                    hideMenu();
                }
                if (dy < 0 && -dy > 10 && isHide){
                    showMenu();
                }
            }
        });
        return view;
    }

    private void hideMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_buttons,"y",layout_buttons.getY(),layout_buttons.getY()+ layout_buttons.getHeight());
        animator.setDuration(500);
        animator.start();
        isHide = true;
    }

    private void showMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_buttons,"y",layout_buttons.getY(),layout_buttons.getY()- layout_buttons.getHeight());
        animator.setDuration(500);
        animator.start();
        isHide = false;
    }
}
