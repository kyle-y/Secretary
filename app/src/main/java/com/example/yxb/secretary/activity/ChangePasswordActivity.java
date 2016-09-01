package com.example.yxb.secretary.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.MyUser;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/25
 * MODIFY_BY:
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{
    private EditText text_password_first,text_password_second,text_password_old;
    private ImageView image_back_change_password;
    private TextView text_confirm_change_password;
    private boolean isRight = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        text_password_first = (EditText) findViewById(R.id.text_password_first);
        text_password_second = (EditText) findViewById(R.id.text_password_second);
        text_password_old = (EditText) findViewById(R.id.text_password_old);
        image_back_change_password = (ImageView) findViewById(R.id.image_back_change_password);
        text_confirm_change_password = (TextView) findViewById(R.id.text_confirm_change_password);

        image_back_change_password.setOnClickListener(this);
        text_confirm_change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_back_change_password:
                //更新服务器数据
                finish();
                break;
            case R.id.text_confirm_change_password:
                String str = text_password_old.getText().toString();
                checkPassword(str);
                if (isRight){
                    String str1 = text_password_first.getText().toString();
                    String str2 = text_password_second.getText().toString();
                    if (str1.isEmpty() || str2.isEmpty()){
                        Toast.makeText(ChangePasswordActivity.this, "输入为空，请重新输入！", Toast.LENGTH_SHORT).show();
                    }else{
                        if (str1.equals(str2)){
                            //更新服务器数据
                            updateCurrentUserPwd(DigestUtils.md5Hex(str),DigestUtils.md5Hex(str1));
                            finish();
                        }else{
                            Toast.makeText(ChangePasswordActivity.this, "两次输入不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;

        }
    }
    //修改密码
    private void updateCurrentUserPwd(String oldPassword, String newPassword){
        addSubscription(BmobUser.updateCurrentUserPassword(oldPassword, newPassword, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("密码修改成功，可以用新密码进行登录");
                }else{

                }
            }
        }));
    }
    //验证旧密码
    private void checkPassword(String oldPassword) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        final MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        // 如果你传的密码是正确的，那么arg0.size()的大小是1，这个就代表你输入的旧密码是正确的，否则是失败的
        query.addWhereEqualTo("password", DigestUtils.md5(oldPassword));
        query.addWhereEqualTo("username", bmobUser.getUsername());
        addSubscription(query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> object, BmobException e) {
                if(e==null){
                    toast("查询密码成功:" + object.size());
                    isRight = true;
                }else{
                    toast("旧密码验证失败！");
                }
            }

        }));
    }
}
