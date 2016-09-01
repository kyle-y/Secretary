package com.example.yxb.secretary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.fragment.FragmentFactory;
import com.example.yxb.secretary.interfaces.IchangeIcon;
import com.example.yxb.secretary.utils.ChangeFragmentUtils;
import com.example.yxb.secretary.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/17
 * MODIFY_BY:
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, FragmentFactory.MeClick, View.OnClickListener, IchangeIcon {
    public BottomNavigationBar bottomnavigationbar;
    private DrawerLayout drawerLayout;
    private FragmentFactory factory;
    private NavigationView navigationView;
    public FrameLayout layout_fragments;
    public FrameLayout layout_fragment_query;
    private List<Fragment> fragments;
    private float bottom_height;
    private ImageView imageView_icon;
    private TextView text_nickName;
    private MyUser user;
    private PersonalSettingActivity personalSettingActivity;
    private LoginActivity loginActivity;

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
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_home, "分类").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_qurey, "查询").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_wechat, "工具").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_news, "头条").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.addItem(new BottomNavigationItem(R.drawable.ic_me, "个人").setActiveColorResource(R.color.colorPrimary));
        bottomnavigationbar.initialise();
        bottomnavigationbar.setTabSelectedListener(this);

        factory = new FragmentFactory();
        factory.setMeClick(this);
        fragments = new ArrayList<>();

        onTabSelected(0);


        //对侧边栏的操作

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new MyNavigationItemListener());

        View view = navigationView.getHeaderView(0);
        imageView_icon = (ImageView) view.findViewById(R.id.imageView_icon);
        text_nickName = (TextView) view.findViewById(R.id.text_nickName);
        imageView_icon.setOnClickListener(this);
        //设置侧边栏的头像和昵称
        if (MyApplication.isLoaded()){
            if (!PreferencesUtils.getString(this,"iconUrl").isEmpty()){
                Picasso.with(this).load(PreferencesUtils.getString(this,"iconUrl")).into(imageView_icon);
            }else{
                imageView_icon.setImageResource(R.drawable.menu_4);
            }
            if (!PreferencesUtils.getString(this,"nickname").isEmpty()){
                text_nickName.setText(PreferencesUtils.getString(this,"nickname"));
            }else{
                text_nickName.setText("请完善个人资料");
            }

        }else{
            imageView_icon.setImageResource(R.drawable.menu_4);
            text_nickName.setText("请登录");
        }
        //实现接口回调，改变icon
        PersonalSettingActivity.setIchangeIcon(this);
        LoginActivity.setIchangeIcon(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_icon:
                if (MyApplication.isLoaded()) {
                    startActivity(new Intent(MyApplication.getContext(), PersonalSettingActivity.class));
                } else {
                    startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
                }
                break;
        }
    }



    class MyNavigationItemListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            item.setChecked(true);
            Log.i("aaa", item.getItemId() + "");
            switch (item.getItemId()) {
                case R.id.menu_bean:
                    if (MyApplication.isLoaded()){
                        startActivity(new Intent(MyApplication.getContext(), BeanManagerActivity.class));
                    }else{
                        Toast.makeText(MainActivity.this, "请登录后再查询！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menu_collect:
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    break;
                case R.id.menu_personal:
                    if (MyApplication.isLoaded()) {
                        startActivity(new Intent(MyApplication.getContext(), PersonalSettingActivity.class));
                    } else {
                        startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
                    }
                    break;

            }
            return false;
        }
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
        if (position != 4) {
            if (position == 1) {
                layout_fragment_query.setVisibility(View.VISIBLE);
                layout_fragments.setVisibility(View.GONE);
                ChangeFragmentUtils.changeFragment(this, factory.createFragment(position), R.id.layout_fragment_query, fragments);
            } else {
                layout_fragment_query.setVisibility(View.GONE);
                layout_fragments.setVisibility(View.VISIBLE);
                ChangeFragmentUtils.changeFragment(this, factory.createFragment(position), R.id.layout_fragments, fragments);
            }
        } else {
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
        drawerLayout.openDrawer(Gravity.RIGHT);
        changeIcon();
    }

    @Override
    public void changeIcon() {
        if (MyApplication.isLoaded()) {
            String nickName = PreferencesUtils.getString(MyApplication.getContext(),"nickname");
            String iconUrl = PreferencesUtils.getString(MyApplication.getContext(),"iconUrl");
            if (!iconUrl.isEmpty()){
                Picasso.with(MyApplication.getContext()).load(iconUrl).into(imageView_icon);
            }
            text_nickName.setText(nickName);
        } else {
            imageView_icon.setImageResource(R.drawable.menu_4);
            text_nickName.setText("请登录");
        }
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            if (drawerLayout.isDrawerOpen(navigationView)){
                drawerLayout.closeDrawer(Gravity.RIGHT);
                return true;
            }
            if (System.currentTimeMillis() - firstTime > 2000){
                Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }else{
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
