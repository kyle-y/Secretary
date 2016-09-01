package com.example.yxb.secretary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.LoginActivity;
import com.example.yxb.secretary.activity.wechat_acount.AcountSettingActivity;
import com.example.yxb.secretary.activity.wechat_acount.ChatSettingActivity;
import com.example.yxb.secretary.activity.wechat_acount.GetMoneySettingActivity;
import com.example.yxb.secretary.activity.wechat_acount.WalletSettingActivity;
import com.example.yxb.secretary.common.MyApplication;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class WechatFragment extends Fragment implements View.OnClickListener{
    private Button button_wallet,button_acount,button_getmoney,button_chat;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);

        button_wallet = (Button) view.findViewById(R.id.button_wallet);
        button_acount = (Button) view.findViewById(R.id.button_acount);
        button_getmoney = (Button) view.findViewById(R.id.button_getmoney);
        button_chat = (Button) view.findViewById(R.id.button_chat);

        button_wallet.setOnClickListener(this);
        button_acount.setOnClickListener(this);
        button_getmoney.setOnClickListener(this);
        button_chat.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (!MyApplication.isLoaded()){
            startActivity(new Intent(MyApplication.getContext(), LoginActivity.class));
        }else{
            switch (v.getId()){
                case R.id.button_wallet:
                    startActivity(new Intent(MyApplication.getContext(), WalletSettingActivity.class));
                    break;
                case R.id.button_acount:
                    startActivity(new Intent(MyApplication.getContext(), AcountSettingActivity.class));
                    break;
                case R.id.button_getmoney:
                    startActivity(new Intent(MyApplication.getContext(), GetMoneySettingActivity.class));
                    break;
                case R.id.button_chat:
                    startActivity(new Intent(MyApplication.getContext(), ChatSettingActivity.class));
                    break;
            }
        }

    }
}
