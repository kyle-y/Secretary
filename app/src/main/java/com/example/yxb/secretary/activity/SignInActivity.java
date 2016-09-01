package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.MyUser;

import org.apache.commons.codec.digest.DigestUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/17
 * MODIFY_BY:
 */
public class SignInActivity extends Activity implements View.OnClickListener{
    private EditText editText_username,editText_password,editText_password_confirm;
    private Button button_signin;
    private String userName,password,password2;
    private MyUser user;
    private ImageView imageView_back4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        button_signin = (Button) findViewById(R.id.button_signin);
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);
        imageView_back4 = (ImageView) findViewById(R.id.imageView_back4);
        editText_password_confirm = (EditText) findViewById(R.id.editText_password_confirm);
        user = new MyUser();

        button_signin.setOnClickListener(this);
        imageView_back4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_signin:
                if (isOk()){
                    user.setUsername(userName);
                    String password_MD5 = DigestUtils.md5Hex(password.getBytes());
                    user.setPassword(password_MD5);
                    user.setGoldBeanNum(1000);
                    Log.i("aaa","密码：" + password_MD5);
                    user.signUp(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if (e == null){
                                Toast.makeText(SignInActivity.this, "注册成功,请返回登录界面登录！", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignInActivity.this, "没有成功！"+ e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.imageView_back4:
                SignInActivity.this.finish();
                break;
        }
    }

    public boolean isOk(){
        userName = editText_username.getText().toString();
        password = editText_password.getText().toString();
        password2 = editText_password_confirm.getText().toString();
        if (!password.isEmpty() && !password2.isEmpty()){
            if (!password.equals(password2)){
                Toast.makeText(SignInActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            }else{
                if (!userName.isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }
}
