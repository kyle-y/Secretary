package com.example.yxb.secretary.bean;

import cn.bmob.v3.BmobUser;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.bean
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/25
 * MODIFY_BY:
 */
public class MyUser extends BmobUser{
    private String iconUrl;
    private String nickName;
    private int GoldBeanNum;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getGoldBeanNum() {
        return GoldBeanNum;
    }

    public void setGoldBeanNum(int goldBeanNum) {
        GoldBeanNum = goldBeanNum;
    }
}
