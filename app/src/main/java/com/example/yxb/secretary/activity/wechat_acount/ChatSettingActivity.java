package com.example.yxb.secretary.activity.wechat_acount;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.BaseActivity;
import com.example.yxb.secretary.adapter.Wechat_chat_adapter;
import com.example.yxb.secretary.bean.ChatItem;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.common.db.DBHelper2;
import com.example.yxb.secretary.common.db.DataBaseManager2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class ChatSettingActivity extends BaseActivity {
    @BindView(R.id.text_chat_set_name)
    TextView textChatSetName;
    @BindView(R.id.text_chat_set_time)
    EditText textChatSetTime;
    @BindView(R.id.image_chat_set_from)
    ImageView imageChatSetFrom;
    @BindView(R.id.image_chat_set_to)
    ImageView imageChatSetTo;
    @BindView(R.id.text_chat_set_from)
    EditText textChatSetFrom;
    @BindView(R.id.button_add_from)
    Button buttonAddFrom;
    @BindView(R.id.text_chat_set_to)
    EditText textChatSetTo;
    @BindView(R.id.button_add_to)
    Button buttonAddTo;
    @BindView(R.id.button_add_wallet_from)
    Button buttonAddWalletFrom;
    @BindView(R.id.button_add_wallet_to)
    Button buttonAddWalletTo;
    @BindView(R.id.button_add_confirm)
    Button buttonAddConfirm;
    @BindView(R.id.button_add_time)
    Button buttonAddTime;
    @BindView(R.id.radioButton1)
    RadioButton radioButton1;
    @BindView(R.id.radioButton2)
    RadioButton radioButton2;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private ArrayList<ChatItem> dataList;
    private Bitmap fromBitmap, toBitmap;
    private boolean isShowHead = false;
    private DataBaseManager2<?> dataBaseManager2;
    private MyUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);
        ButterKnife.bind(this);
        dataList = new ArrayList<>();

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


    @OnClick({R.id.button_add_time, R.id.image_chat_set_from, R.id.image_chat_set_to, R.id.button_add_from, R.id.button_add_to, R.id.button_add_wallet_from, R.id.button_add_wallet_to, R.id.button_add_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_time:
                ChatItem chatItem1 = new ChatItem();
                chatItem1.setText(textChatSetTime.getText().toString());
                chatItem1.setType(Wechat_chat_adapter.TOP_TIME);
                dataList.add(chatItem1);
                break;
            case R.id.image_chat_set_from:
                crop(101);
                break;
            case R.id.image_chat_set_to:
                crop(102);
                break;
            case R.id.button_add_from:
                ChatItem chatItem2 = new ChatItem();
                if (fromBitmap != null) {
                    chatItem2.setBitmap(fromBitmap);
                }
                chatItem2.setText(textChatSetFrom.getText().toString());
                chatItem2.setType(Wechat_chat_adapter.LEFT_MSG);
                dataList.add(chatItem2);
                break;
            case R.id.button_add_to:
                ChatItem chatItem3 = new ChatItem();
                if (fromBitmap != null) {
                    chatItem3.setBitmap(toBitmap);
                }
                chatItem3.setText(textChatSetTo.getText().toString());
                chatItem3.setType(Wechat_chat_adapter.RIGHT_MSG);
                dataList.add(chatItem3);
                break;
            case R.id.button_add_wallet_from:
                ChatItem chatItem4 = new ChatItem();
                if (fromBitmap != null) {
                    chatItem4.setBitmap(fromBitmap);
                }
                chatItem4.setType(Wechat_chat_adapter.WALLET_LEFT_MSG);
                dataList.add(chatItem4);
                break;
            case R.id.button_add_wallet_to:
                ChatItem chatItem5 = new ChatItem();
                chatItem5.setText(textChatSetName.getText().toString());
                chatItem5.setType(Wechat_chat_adapter.BOTTOM_MSG);
                dataList.add(chatItem5);
                break;
            case R.id.button_add_confirm:
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = simpleDateFormat.format(date);

                ContentValues values = new ContentValues();
                values.put("name", user.getUsername());
                values.put("type", "微信聊天截图");
                values.put("num", 10);
                values.put("time", currentDate);
                try {
                    dataBaseManager2.insertData(DBHelper2.GOLD_BEAN_TABLE, values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyUser newUser = new MyUser();
                newUser.setGoldBeanNum(user.getGoldBeanNum() - 10);
                ChatSettingActivity.this.addSubscription(newUser.update(user.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                        }else{
                        }
                    }
                }));

                Intent intent3 = new Intent();
                intent3.putExtra("name", textChatSetName.getText().toString());
                intent3.putExtra("isShowHead", isShowHead);
                intent3.putParcelableArrayListExtra("list", dataList);
                intent3.setClass(MyApplication.getContext(), ChatActivity.class);
                startActivity(intent3);
                ChatSettingActivity.this.finish();
                break;
        }
    }

    /*
      * 剪切图片
      */
    private void crop(int code) {
        Intent popintent = new Intent(Intent.ACTION_GET_CONTENT);
        popintent.addCategory(Intent.CATEGORY_OPENABLE);
        popintent.setType("image/*");
        popintent.putExtra("crop", "true");
        popintent.putExtra("aspectX", 1);
        popintent.putExtra("aspectY", 1);
        popintent.putExtra("outputX", 80);
        popintent.putExtra("outputY", 80);
        popintent.putExtra("data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(popintent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                fromBitmap = bundle.getParcelable("data");
                imageChatSetFrom.setImageBitmap(fromBitmap);
            }
        }


        if (requestCode == 102) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                toBitmap = bundle.getParcelable("data");
                imageChatSetTo.setImageBitmap(toBitmap);

            }
        }
    }
}
