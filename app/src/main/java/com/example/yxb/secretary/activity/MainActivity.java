package com.example.yxb.secretary.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.fragment.FragmentFactory;
import com.example.yxb.secretary.utils.ChangeFragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/17
 * MODIFY_BY:
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener,FragmentFactory.MeClick{
    public BottomNavigationBar bottomnavigationbar;
    private DrawerLayout drawerLayout;
    private FragmentFactory factory;
    public FrameLayout layout_fragments;
    public FrameLayout layout_fragment_query;
    private List<Fragment> fragments;
    private float bottom_height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        bottomnavigationbar = (BottomNavigationBar) findViewById(R.id.bottomnavigationbar);
        layout_fragments = (FrameLayout) findViewById(R.id.layout_fragments);
        layout_fragment_query = (FrameLayout) findViewById(R.id.layout_fragment_query);

        bottomnavigationbar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomnavigationbar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_home,"分类").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_qurey,"查询").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_wechat,"工具").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_news,"头条").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_me,"个人").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.initialise();
        bottomnavigationbar.setTabSelectedListener(this);

        factory = new FragmentFactory();
        factory.setMeClick(this);
        fragments = new ArrayList<>();

        onTabSelected(0);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        bottom_height = bottomnavigationbar.getHeight();
    }

    public float getBottom_height() {
        return bottom_height;
    }

    @Override
    public void onTabSelected(int position) {
        if (position != 4){
            if (position == 1){
                layout_fragment_query.setVisibility(View.VISIBLE);
                layout_fragments.setVisibility(View.GONE);
                ChangeFragmentUtils.changeFragment(this,factory.createFragment(position),R.id.layout_fragment_query,fragments);
            }else{
                layout_fragment_query.setVisibility(View.GONE);
                layout_fragments.setVisibility(View.VISIBLE);
                ChangeFragmentUtils.changeFragment(this,factory.createFragment(position),R.id.layout_fragments,fragments);
            }
        }else {
            factory.createFragment(position);
        }
    }


    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void meClick() {
        drawerLayout.openDrawer(GravityCompat.END);
    }


}
