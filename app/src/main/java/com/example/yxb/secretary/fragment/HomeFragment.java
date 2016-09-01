package com.example.yxb.secretary.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
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
    private Toolbar toolbar_home;
    private BottomNavigationBar bottomNavigationBar;
    private DividerGridItemDecoration dividerGridItemDecoration;
    private boolean isHide = false;
    private List<String> tittles = Arrays.asList(new String[]{"百度","手机归属地","黄历","违章查询",
            "彩票开奖","汇率换算","驾考题库","标准体重","火车票/汽车票","IP地址查询",
            "汽车标志","常用电话"});
    private List<String> urls = Arrays.asList(new String[]{"http://wap.baidu.com"   //百度
            ,"http://wap.ip138.com/sim.html"    //手机归属地
            ,"http://m.laohuangli.net"    //黄历
            ,"http://m.chaxun.weizhang8.cn"   //违章查询
            ,"http://m.500.com/info/kaijiang/#h5"  //彩票开奖
            ,"http://m.cngold.org/home/dy157.html"  //汇率换算
            ,"http://m.jkydt.com/"  //驾考题库
            ,"http://tools.2345.com/m/study_tizhong.htm"    //标准体重
            ,"http://m.tieyou.com"  //火车票、汽车票
            ,"http://www.ip.cn/"    //IP地址查询
            ,"http://m.58che.com/car/brand.html"    //汽车标志
            ,"http://tools.2345.com/m/tefudh.htm"   //常用电话
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
        dividerGridItemDecoration.setSize(UiUtils.dip2px(10));
        home_recyclerView.addItemDecoration(dividerGridItemDecoration);

        toolbar_home = (Toolbar) view.findViewById(R.id.toolbar_home);
        toolbar_home.setTitle("分类查询工具");
        toolbar_home.setTitleTextColor(Color.WHITE);

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
