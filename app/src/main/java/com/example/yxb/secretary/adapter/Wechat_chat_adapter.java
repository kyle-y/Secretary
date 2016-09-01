package com.example.yxb.secretary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.bean.ChatItem;
import com.example.yxb.secretary.common.MyApplication;

import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/29
 * MODIFY_BY:
 */
public class Wechat_chat_adapter extends BaseAdapter{

    public static final int TOP_TIME = 0;
    public static final int LEFT_MSG = 1;
    public static final int RIGHT_MSG = 2;
    public static final int WALLET_LEFT_MSG = 3;
    public static final int BOTTOM_MSG = 4;

    private List<ChatItem> list;

    public Wechat_chat_adapter(List<ChatItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public ChatItem getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopViewHolder topViewHolder = null;
        LeftViewHolder leftViewHolder = null;
        RightViewHolder rightViewHolder = null;
        WalletViewHolder walletViewHolder = null;
        BottomViewHolder bottomViewHolder = null;

        int itemType = list.get(position).getType();
        if (convertView == null){
            switch (itemType){
                case TOP_TIME:
                    convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_mid, null);
                    topViewHolder = new TopViewHolder();
                    topViewHolder.text_chat_time = (TextView) convertView.findViewById(R.id.text_chat_time);
                    convertView.setTag(topViewHolder);
                    break;
                case LEFT_MSG:
                    convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_left, null);
                    leftViewHolder = new LeftViewHolder();
                    leftViewHolder.image_chat_left_icon = (ImageView) convertView.findViewById(R.id.image_chat_left_icon);
                    leftViewHolder.text_chat_left = (TextView) convertView.findViewById(R.id.text_chat_left);
                    convertView.setTag(leftViewHolder);
                    break;
                case RIGHT_MSG:
                    convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_right, null);
                    rightViewHolder = new RightViewHolder();
                    rightViewHolder.image_chat_right_icon = (ImageView) convertView.findViewById(R.id.image_chat_right_icon);
                    rightViewHolder.text_chat_right = (TextView) convertView.findViewById(R.id.text_chat_right);
                    convertView.setTag(rightViewHolder);
                    break;
                case WALLET_LEFT_MSG:
                    convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_left_wallet, null);
                    walletViewHolder = new WalletViewHolder();
                    walletViewHolder.image_chat_red_left_icon = (ImageView) convertView.findViewById(R.id.image_chat_red_left_icon);
                    convertView.setTag(walletViewHolder);
                    break;
                case BOTTOM_MSG:
                    convertView = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.item_get_wallet, null);
                    bottomViewHolder = new BottomViewHolder();
                    bottomViewHolder.text_wallet_come = (TextView) convertView.findViewById(R.id.text_wallet_come);
                    convertView.setTag(bottomViewHolder);
                    break;
            }
        }else{
            switch (itemType){
                case TOP_TIME:
                    topViewHolder = (TopViewHolder) convertView.getTag();
                    break;
                case LEFT_MSG:
                    leftViewHolder = (LeftViewHolder) convertView.getTag();
                    break;
                case RIGHT_MSG:
                    rightViewHolder = (RightViewHolder) convertView.getTag();
                    break;
                case WALLET_LEFT_MSG:
                    walletViewHolder = (WalletViewHolder) convertView.getTag();
                    break;
                case BOTTOM_MSG:
                    bottomViewHolder = (BottomViewHolder) convertView.getTag();
                    break;
            }
        }
        switch (itemType){
            case TOP_TIME:
                topViewHolder.text_chat_time.setText(list.get(position).getText());
                break;
            case LEFT_MSG:
                if (list.get(position).getBitmap() != null)
                leftViewHolder.image_chat_left_icon.setImageBitmap(list.get(position).getBitmap());
                leftViewHolder.text_chat_left.setText(list.get(position).getText());
                break;
            case RIGHT_MSG:
                if (list.get(position).getBitmap() != null)
                rightViewHolder.image_chat_right_icon.setImageBitmap(list.get(position).getBitmap());
                rightViewHolder.text_chat_right.setText(list.get(position).getText());
                break;
            case WALLET_LEFT_MSG:
                if (list.get(position).getBitmap() != null)
                walletViewHolder.image_chat_red_left_icon.setImageBitmap(list.get(position).getBitmap());
                break;
            case BOTTOM_MSG:
                bottomViewHolder.text_wallet_come.setText(list.get(position).getText());
                break;
        }

        return convertView;
    }

    class TopViewHolder {
        TextView text_chat_time;
    }

    class LeftViewHolder{
        ImageView image_chat_left_icon;
        TextView text_chat_left;
    }

    class RightViewHolder{
        ImageView image_chat_right_icon;
        TextView text_chat_right;
    }

    class WalletViewHolder{
        ImageView image_chat_red_left_icon;
    }

    class BottomViewHolder{
        TextView text_wallet_come;
    }
}
