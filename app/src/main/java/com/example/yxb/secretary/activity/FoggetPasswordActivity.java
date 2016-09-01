package com.example.yxb.secretary.activity;

import android.content.Intent;
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
 * CREATE_TIME:2016/8/29
 * MODIFY_BY:
 */
public class FoggetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private EditText text_password_first, text_password_second;
    private ImageView image_back_change_password;
    private TextView text_confirm_change_password;
    private MyUser myUser;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        text_password_first = (EditText) findViewById(R.id.text_password_first);
        text_password_second = (EditText) findViewById(R.id.text_password_second);
        image_back_change_password = (ImageView) findViewById(R.id.image_back_change_password);
        text_confirm_change_password = (TextView) findViewById(R.id.text_confirm_change_password);

        image_back_change_password.setOnClickListener(this);
        text_confirm_change_password.setOnClickListener(this);
        myUser = BmobUser.getCurrentUser(MyUser.class);

        Intent intent = getIntent();
        name = intent.getStringExtra("userName");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_back_change_password:
                //更新服务器数据
                finish();
                break;
            case R.id.text_confirm_change_password:
                BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                query.addWhereEqualTo("username", name);
                query.findObjects(new FindListener<MyUser>() {
                    @Override
                    public void done(List<MyUser> list, BmobException e) {

                    }
                });
                String str1 = text_password_first.getText().toString();
                String str2 = text_password_second.getText().toString();
                if (str1.isEmpty() || str2.isEmpty()) {
                    Toast.makeText(FoggetPasswordActivity.this, "输入为空，请重新输入！", Toast.LENGTH_SHORT).show();
                } else {
                    if (str1.equals(str2)) {
                        //更新服务器数据
                        updateCurrentUserPwd(str1);
                        finish();
                    } else {
                        Toast.makeText(FoggetPasswordActivity.this, "两次输入不一致，请重新输入！", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

        }
    }

    //修改密码
    private void updateCurrentUserPwd(String password) {
        MyUser newUser =  new MyUser();
        newUser.setPassword(DigestUtils.md5Hex(password.getBytes()));
        if (myUser != null){
            addSubscription(newUser.update(myUser.getObjectId(),new UpdateListener() {

                @Override
                public void done(BmobException e) {
                    if(e==null){
                        toast("修改成功");
                    }else{

                    }
                }
            }));
        }else{
            toast("");
        }

    }

//    private void resetPasswordBySMS(String phone){
//        //请求短信验证码
//		BmobSMS.requestSMSCode(phone, "模板名称",new QueryListener<Integer>() {
//
//			@Override
//			public void done(Integer smsId,BmobException ex) {
//				if(ex==null){//验证码发送成功
//					log("短信id："+smsId);
//				}
//			}
//		});
//        String code = et_code.getText().toString();
//        //2、重置的是绑定了该手机号的账户的密码
//        addSubscription(BmobUser.resetPasswordBySMSCode(code,"1234567", new UpdateListener() {
//
//            @Override
//            public void done(BmobException e) {
//                if(e==null){
//                    toast("密码重置成功");
//                }else{
//                    toast("错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage());
//                }
//            }
//        }));
//    }
}
