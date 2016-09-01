package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.interfaces.IchangeIcon;
import com.example.yxb.secretary.utils.PreferencesUtils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/17
 * MODIFY_BY:
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    TextInputEditText text_username, text_password;
    ImageView imageView_user_icon, imageView_back3, other_wechat, other_weibo, other_qq;
    TextView textView_signin, textView_quick_login, textView_forget_password;
    Button button_login;
    MyUser user;
    private RegisterPage registerPage;
    static IchangeIcon ichangeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        text_username = (TextInputEditText) findViewById(R.id.text_username);
        text_password = (TextInputEditText) findViewById(R.id.text_password);
        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);

        textView_signin = (TextView) findViewById(R.id.textView_signin);
        textView_signin.setOnClickListener(this);

        textView_quick_login = (TextView) findViewById(R.id.textView_quick_login);
        textView_quick_login.setOnClickListener(this);
        SMSSDK.initSDK(this, "16342d53ae608", "8c0536e37905bb035da8623321a1b668");

        textView_forget_password = (TextView) findViewById(R.id.textView_forget_password);
        textView_forget_password.setOnClickListener(this);

        imageView_back3 = (ImageView) findViewById(R.id.imageView_back3);
        imageView_back3.setOnClickListener(this);

        //第一：默认初始化
        user = new MyUser();
        //提示网络
        if (!MyApplication.isOnNetWork()){
            Toast.makeText(LoginActivity.this, "网络无连接，无法登陆", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_quick_login:
                //短信验证打开注册页面
                registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {

                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            String phone = (String) phoneMap.get("phone");
                            user.setUsername(phone);
                            user.setNickName(phone);
                            user.setIconUrl("http://images.dayoo.com/www/roll/attachement/png/site1/20110608/8189507370770514074.png");
                            user.login(new SaveListener<MyUser>() {
                                @Override
                                public void done(MyUser bmobUser, BmobException e) {
                                    if (e == null) {
                                        MyApplication.setIsLoaded(true);
                                        PreferencesUtils.putBoolean(MyApplication.getContext(), "isLogin", true);
                                        String nickName = (String) BmobUser.getObjectByKey("nickName");
                                        String iconUrl = (String) BmobUser.getObjectByKey("iconUrl");
                                        PreferencesUtils.putString(MyApplication.getContext(), "nickname", nickName);
                                        PreferencesUtils.putString(MyApplication.getContext(), "iconUrl", iconUrl);
                                        if (ichangeIcon != null) {
                                            ichangeIcon.changeIcon();
                                        }
                                        LoginActivity.this.finish();
                                    }
                                }
                            });

                        }
                    }
                });
                registerPage.show(MyApplication.getContext());
                break;
            case R.id.textView_signin:
                startActivity(new Intent(MyApplication.getContext(), SignInActivity.class));
                break;
            case R.id.button_login:
                button_login.setText("登录中。。。");
                String userName = text_username.getText().toString();
                String password = text_password.getText().toString();
                if (!userName.isEmpty() && !password.isEmpty()) {
                    user.setUsername(userName);
                    user.setPassword(DigestUtils.md5Hex(password.getBytes()));
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser bmobUser, BmobException e) {
                            if (e == null) {
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                MyApplication.setIsLoaded(true);
                                PreferencesUtils.putBoolean(MyApplication.getContext(), "isLogin", true);
                                String nickName = (String) BmobUser.getObjectByKey("nickName");
                                String iconUrl = (String) BmobUser.getObjectByKey("iconUrl");
                                PreferencesUtils.putString(MyApplication.getContext(), "nickname", nickName);
                                PreferencesUtils.putString(MyApplication.getContext(), "iconUrl", iconUrl);
                                if (ichangeIcon != null) {
                                    ichangeIcon.changeIcon();
                                }
                                LoginActivity.this.finish();
                            }else{
                                button_login.setText("登录失败");
                                Toast.makeText(LoginActivity.this, "无此用户，请先注册！", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
                break;
            case R.id.imageView_back3:
                finish();
                break;
            case R.id.textView_forget_password:

                break;
        }
    }

    public static void setIchangeIcon(IchangeIcon ichangeIcon) {
        LoginActivity.ichangeIcon = ichangeIcon;
    }
}
