package com.example.yxb.secretary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.adapter.QueryFragmentsAdapter;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class QueryFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private TabLayout tablayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_query, null);
        tablayout = (TabLayout) rootView.findViewById(R.id.tablayout_query);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_query);

        QueryFragmentsAdapter adapter = new QueryFragmentsAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

        FragmentFactory.createFragment(0);
        viewPager.setOnPageChangeListener(this);
        return rootView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        FragmentFactory.createFragment(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
