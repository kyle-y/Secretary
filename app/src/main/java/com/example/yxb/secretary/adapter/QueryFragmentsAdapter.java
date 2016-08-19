package com.example.yxb.secretary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yxb.secretary.fragment.FragmentFactory_tab;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.adapter
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class QueryFragmentsAdapter extends FragmentPagerAdapter{

    private String[] tittles = {"天气","快递","公交","地图"};

    public QueryFragmentsAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory_tab.createFragment(position);
    }

    @Override
    public int getCount() {
        return tittles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tittles[position];
    }
}
