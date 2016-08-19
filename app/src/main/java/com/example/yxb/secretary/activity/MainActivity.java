package com.example.yxb.secretary.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.fragment.FragmentFactory;

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
    private FrameLayout layout_fragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        bottomnavigationbar = (BottomNavigationBar) findViewById(R.id.bottomnavigationbar);
        layout_fragments = (FrameLayout) findViewById(R.id.layout_fragments);

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
    }

    @Override
    public void onTabSelected(int position) {
        if (position != 4){
            getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragments,factory.createFragment(position)).commit();
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
