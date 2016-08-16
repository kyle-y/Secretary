package com.example.yxb.secretary.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.fragment.HomeFragment;
import com.example.yxb.secretary.fragment.NewsFragment;
import com.example.yxb.secretary.fragment.QueryFragment;
import com.example.yxb.secretary.fragment.WechatFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener{

    private ImageView button_ic_home,button_ic_query,button_ic_wechat,button_ic_other,button_ic_slide;
    private LinearLayout layout_buttons;
    private FrameLayout layout_fragments;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_ic_home = (ImageView) findViewById(R.id.button_ic_home);
        button_ic_query = (ImageView) findViewById(R.id.button_ic_query);
        button_ic_wechat = (ImageView) findViewById(R.id.button_ic_wechat);
        button_ic_other = (ImageView) findViewById(R.id.button_ic_other);
        button_ic_slide = (ImageView) findViewById(R.id.button_ic_slide);

        layout_buttons = (LinearLayout) findViewById(R.id.layout_buttons);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        layout_fragments = (FrameLayout) findViewById(R.id.layout_fragments);
        layout_fragments.setOnTouchListener(this);


        button_ic_home.setOnClickListener(this);
        button_ic_query.setOnClickListener(this);
        button_ic_wechat.setOnClickListener(this);
        button_ic_other.setOnClickListener(this);
        button_ic_slide.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_ic_home:
                getFragment(new HomeFragment());
                break;
            case R.id.button_ic_query:
                getFragment(new QueryFragment());
                break;
            case R.id.button_ic_wechat:
                getFragment(new WechatFragment());
                break;
            case R.id.button_ic_other:
                getFragment(new NewsFragment());
                break;
            case R.id.button_ic_slide:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
    }

    private void getFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragments,fragment).commit();
    }


    private float startY, disY, buttonLayoutHeight;
    private boolean isUp, isHide;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        buttonLayoutHeight = layout_buttons.getHeight();
        Log.i("aaa","控件高度：" + buttonLayoutHeight);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("aaa","进入onTouch" );
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                Log.i("aaa","开始坐标：" + startY);
                break;
            case MotionEvent.ACTION_MOVE:
                disY = event.getY() - startY;
                Log.i("aaa","滑动距离：" + disY);
                if (Math.abs(disY) > buttonLayoutHeight){
                    isUp = disY < 0;//判断滑动方向
                    if (isUp){
                        if (isHide){//判断是否处于隐藏状态
                            showMenu();
                        }
                    }else{
                        if (!isHide){
                            hideMenu();
                        }
                    }
                }
                break;

        }
        return true;
    }

    private void hideMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_buttons,"y",layout_buttons.getY(),layout_buttons.getY()+ buttonLayoutHeight);
        animator.setDuration(500);
        animator.start();
        isHide = true;
    }

    private void showMenu() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout_buttons,"y",layout_buttons.getY(),layout_buttons.getY()- buttonLayoutHeight);
        animator.setDuration(500);
        animator.start();
        isHide = false;
    }
}
