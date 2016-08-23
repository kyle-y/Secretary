package com.example.yxb.secretary.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.utils
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/22
 * MODIFY_BY:
 */
public class ChangeFragmentUtils {

    public static void changeFragment(AppCompatActivity activity, Fragment fragment, int containerID, List<Fragment> fragments){
        FragmentTransaction transition = activity.getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()){
            transition.add(containerID,fragment);
            transition.addToBackStack(fragment.getClass().getName());
            fragments.add(fragment);
        }else{
            transition.show(fragment);
        }
        for (Fragment fragment1 : fragments){
            if (fragment == fragment1){
                continue;
            }
            transition.hide(fragment1);
        }
        transition.commit();
    }
}
