package com.example.yxb.secretary.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class FragmentFactory {
    private HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();
    public interface MeClick{
        void meClick();
    }
    private MeClick meClick;

    public void setMeClick(MeClick meClick) {
        this.meClick = meClick;
    }

    public Fragment createFragment(int position){
        Fragment fragment = fragmentHashMap.get(position);
        if (fragment == null){
            switch (position){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new QueryFragment();
                    break;
                case 2:
                    fragment = new WechatFragment();
                    break;
                case 3:
                    fragment = new NewsFragment();
                    break;
                case 4:
                    meClick.meClick();
                    break;
                default:
                    break;
            }
            fragmentHashMap.put(position, fragment);
        }
        return fragment;
    }

}
