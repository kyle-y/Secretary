package com.example.yxb.secretary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yxb.secretary.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/9/1
 * MODIFY_BY:
 */
public class NewsContentActivity extends BaseActivity {
    @BindView(R.id.image_back_personal_setting)
    ImageView imageBackPersonalSetting;
    @BindView(R.id.text_news_title)
    TextView textNewsTitle;
    @BindView(R.id.text_news_content)
    TextView textNewsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        textNewsTitle.setText(title);
        textNewsContent.setText(content);
    }

    @OnClick(R.id.image_back_personal_setting)
    public void onClick() {
        NewsContentActivity.this.finish();
    }
}
