package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.Codes;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/25
 * MODIFY_BY:
 */
public class ChangeNikenameActivity extends Activity implements View.OnClickListener{
    private ImageView iamge_back_change_nickname;
    private TextView text_confirm_change_nickname;
    private EditText text_nickname_first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nickname);

        iamge_back_change_nickname = (ImageView) findViewById(R.id.iamge_back_change_nickname);
        text_confirm_change_nickname = (TextView) findViewById(R.id.text_confirm_change_nickname);
        text_nickname_first = (EditText) findViewById(R.id.text_nickname_first);

        iamge_back_change_nickname.setOnClickListener(this);
        text_confirm_change_nickname.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iamge_back_change_nickname:
                finish();
                break;
            case R.id.text_confirm_change_nickname:
                String nickName = text_nickname_first.getText().toString();
                if (nickName.isEmpty()){
                    Toast.makeText(ChangeNikenameActivity.this, "输入为空，请重新输入", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("nickname",nickName);
                    setResult(Codes.RESULT_CODE_WEATHER,intent);
                    Toast.makeText(ChangeNikenameActivity.this, "修改昵称成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }
    }
}
