package com.example.yxb.secretary.activity.wechat_acount;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.BaseActivity;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.common.db.DBHelper2;
import com.example.yxb.secretary.common.db.DataBaseManager2;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity.wechat_acount
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/30
 * MODIFY_BY:
 */
public class WalletSettingActivity extends BaseActivity {
    @BindView(R.id.text_wallet_money)
    EditText textWalletMoney;
    @BindView(R.id.text_wallet_time)
    EditText textWalletTime;
    @BindView(R.id.text_wallet_number)
    EditText textWalletNumber;
    @BindView(R.id.button_wallet_confirm)
    Button buttonWalletConfirm;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private boolean isShowHead = false;
    private DataBaseManager2<?> dataBaseManager2;
    private MyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_setting);
        ButterKnife.bind(this);

        dataBaseManager2 = DataBaseManager2.getInstance(this);
        user = BmobUser.getCurrentUser(MyUser.class);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton1:
                        isShowHead = true;
                        break;
                    case R.id.radioButton2:
                        isShowHead = false;
                        break;
                }
            }
        });
    }

    @OnClick(R.id.button_wallet_confirm)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_wallet_confirm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = simpleDateFormat.format(date);

                ContentValues values = new ContentValues();
                values.put("name", user.getUsername());
                values.put("type", "微信红包截图");
                values.put("num", 10);
                values.put("time", currentDate);
                try {
                    dataBaseManager2.insertData(DBHelper2.GOLD_BEAN_TABLE, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MyUser newUser = new MyUser();
                newUser.setGoldBeanNum(user.getGoldBeanNum() - 10);
                WalletSettingActivity.this.addSubscription(newUser.update(user.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                        }else{
                        }
                    }
                }));

                Intent intent = new Intent();
                intent.putExtra("money", textWalletMoney.getText().toString());
                intent.putExtra("time", textWalletTime.getText().toString());
                intent.putExtra("number", textWalletNumber.getText().toString());
                intent.putExtra("isShowHead", isShowHead);
                intent.setClass(MyApplication.getContext(), WalletActivity.class);
                startActivity(intent);
                WalletSettingActivity.this.finish();
                break;
        }
    }
}
