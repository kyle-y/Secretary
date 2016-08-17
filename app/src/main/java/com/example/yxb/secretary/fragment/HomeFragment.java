package com.example.yxb.secretary.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.MainActivity;
import com.example.yxb.secretary.adapter.MyStraggerAdatper;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.deroctions.DividerGridItemDecoration;

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
    private MainActivity mActivity;
    private LinearLayout layout_buttons;
    private boolean isHide = false;
    private float height;
    private List<String> tittles = Arrays.asList(new String[]{"手机归属地","黄历","违章查询",
            "星座运势","单位换算","驾考题库","计算器","火车票查询","IP地址查询","区号查询",
            "微信热门","笑话段子","汇率转换","语音合成","彩票","名人名言","历史今天",});

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        home_recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        home_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        MyStraggerAdatper adatper = new MyStraggerAdatper(MyApplication.getContext(),tittles);
        home_recyclerView.setAdapter(adatper);
        home_recyclerView.addItemDecoration(new DividerGridItemDecoration(MyApplication.getContext()));

        mActivity = (MainActivity) getActivity();
        layout_buttons = mActivity.layout_buttons;
        height = layout_buttons.getHeight();



        home_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

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

        adatper.setOnItemLongClickListener(new Mylistener());
        return view;
    }

    class Mylistener implements MyStraggerAdatper.OnItemLongClickListener {

        @Override
        public void onItemLongClickListener(View view, int position) {
            Toast.makeText(MyApplication.getContext(), "长按了" + position, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideMenu() {
        Log.i("aaa","hideMenu");

        Log.i("aaa","height:"+height);
        ValueAnimator animator = ValueAnimator.ofFloat(height);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                layout_buttons.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
        isHide = true;
    }

    private void showMenu() {
        Log.i("aaa","showMenu");
        ObjectAnimator animator = ObjectAnimator.ofInt(layout_buttons,"height",(int)height);
        animator.setDuration(500);
        animator.start();
        isHide = false;
    }

}
