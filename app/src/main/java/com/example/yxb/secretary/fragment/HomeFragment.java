package com.example.yxb.secretary.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
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
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.MainActivity;
import com.example.yxb.secretary.activity.WebViewActivity;
import com.example.yxb.secretary.adapter.MyStraggerAdatper;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.deroctions.DividerGridItemDecoration;
import com.example.yxb.secretary.utils.UiUtils;

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
    private BottomNavigationBar bottomNavigationBar;
    private DividerGridItemDecoration dividerGridItemDecoration;
    private boolean isHide = false;
    private List<String> tittles = Arrays.asList(new String[]{"百度","手机归属地","黄历","违章查询",
            "彩票开奖","汇率换算","驾考题库","标准体重","火车票查询","IP地址查询","汽车票",
            "微信热门","汽车标志","常用电话"});
    private List<String> urls = Arrays.asList(new String[]{"http://wap.baidu.com"
            ,"http://wap.ip138.com/sim.html"
            ,"http://http://tools.2345.com/jrhl.htm"
            ,"http://light.weiche.me/#&index"
            ,"http://m.500.com/info/kaijiang/"
            ,"http://m.cngold.org/home/dy157.html"
            ,"http://m.jkydt.com/"
            ,"http://tools.2345.com/m/study_tizhong.htm"
            ,"http://m.ctrip.com/webapp/train"
            ,"http://www.ip.cn/"
            ,"http://m.ctrip.com/webapp/bus"
            ,"http://wx.html5.qq.com"
            ,"http://m.58che.com/car/brand.html"
            ,"http://tools.2345.com/m/tefudh.htm"
            });
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        home_recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerView);
        home_recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        MyStraggerAdatper adatper = new MyStraggerAdatper(MyApplication.getContext(),tittles);
        home_recyclerView.setAdapter(adatper);
        dividerGridItemDecoration = new DividerGridItemDecoration(MyApplication.getContext());
        dividerGridItemDecoration.setSize(UiUtils.dip2px(20));
        home_recyclerView.addItemDecoration(dividerGridItemDecoration);

        mActivity = (MainActivity) getActivity();
        bottomNavigationBar = mActivity.bottomnavigationbar;
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

        adatper.setOnItemLongClickListener(new Mylistener1());
        adatper.setOnItemClickListener(new Mylistener2());
        return view;
    }

    class Mylistener1 implements MyStraggerAdatper.OnItemLongClickListener {

        @Override
        public void onItemLongClickListener(View view, int position) {
            Toast.makeText(MyApplication.getContext(), "长按了" + position, Toast.LENGTH_SHORT).show();
        }
    }
    class Mylistener2 implements MyStraggerAdatper.OnItemClickListener {

        @Override
        public void onItemClickListener(View view, int position) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("URL",urls.get(position));
            intent.putExtra("DATA", bundle);
            intent.setClass(MyApplication.getContext(), WebViewActivity.class);
            startActivity(intent);
        }
    }



    private void hideMenu() {
        Log.i("aaa","hideMenu");
        ValueAnimator animator = ValueAnimator.ofFloat(0f, mActivity.getBottom_height());
        animator.setTarget(bottomNavigationBar);
        animator.setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottomNavigationBar.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        isHide = true;
    }

    private void showMenu() {
        Log.i("aaa","showMenu");
        ValueAnimator animator = ValueAnimator.ofFloat(mActivity.getBottom_height(), 0f);
        animator.setTarget(bottomNavigationBar);
        animator.setDuration(500);
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bottomNavigationBar.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
        isHide = false;
    }
}
