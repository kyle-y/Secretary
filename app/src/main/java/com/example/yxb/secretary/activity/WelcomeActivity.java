package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/27
 * MODIFY_BY:
 */
public class WelcomeActivity extends Activity{
    private ImageView image_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        image_welcome = (ImageView) findViewById(R.id.image_welcome);
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(3000);
        image_welcome.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(MyApplication.getContext(), MainActivity.class));
                WelcomeActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
