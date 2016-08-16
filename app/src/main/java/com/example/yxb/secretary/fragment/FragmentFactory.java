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
    private static HashMap<Integer, Fragment> fragmentHashMap = new HashMap<>();

    public static Fragment createFragment(int position){
        Fragment fragment = fragmentHashMap.get(position);
        if (fragment == null){
            switch (position){
                case 0:
                    fragment = new WeatherFragment();
                    break;
                case 1:
                    fragment = new WeatherFragment();
                    break;
                case 2:
                    fragment = new WeatherFragment();
                    break;
                case 3:
                    fragment = new WeatherFragment();
                    break;
                default:
                    break;
            }
            fragmentHashMap.put(position, fragment);
        }
        return fragment;
    }
}
