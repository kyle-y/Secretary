package com.example.yxb.secretary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.utils.StringWriteToFile;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class ExpressFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_express, null);

        Parameters param = new Parameters();
//        param.put("page",page);
        ApiStoreSDK.execute("http://apis.baidu.com/netpopo/express/express2",ApiStoreSDK.GET,param,
                new ApiCallBack(){
                    @Override
                    public void onSuccess(int i, String s) {
                        super.onSuccess(i, s);
                        StringWriteToFile.writeIntoFile(s);
                    }
                });
        return view;
    }
}
