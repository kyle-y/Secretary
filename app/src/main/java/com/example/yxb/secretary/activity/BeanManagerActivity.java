package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.adapter.GoldBeanRecordAdapter;
import com.example.yxb.secretary.bean.GoldBean;
import com.example.yxb.secretary.bean.MyUser;
import com.example.yxb.secretary.common.db.DBHelper2;
import com.example.yxb.secretary.common.db.DataBaseManager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.activity
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/24
 * MODIFY_BY:
 */
public class BeanManagerActivity extends Activity {
    @BindView(R.id.text_bean_num)
    TextView textBeanNum;
    @BindView(R.id.list_bean_record)
    ListView listBeanRecord;

    private MyUser user;
    private DataBaseManager2<?> dataBaseManager2;
    private List<Map<String, Object>> tempDataList;
    private List<Map<String, Object>> dataList;

    private GoldBeanRecordAdapter goldBeanRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_manager);
        ButterKnife.bind(this);

        user = BmobUser.getCurrentUser(MyUser.class);
        textBeanNum.setText((Integer) BmobUser.getObjectByKey("GoldBeanNum") + "");
        Log.i("aaa",(Integer) BmobUser.getObjectByKey("GoldBeanNum") + "");
        Log.i("aaa",user.getGoldBeanNum() + "");

        dataList = new ArrayList<>();
        goldBeanRecordAdapter = new GoldBeanRecordAdapter(dataList);
        listBeanRecord.setAdapter(goldBeanRecordAdapter);

        dataBaseManager2 = DataBaseManager2.getInstance(this);
        Executors.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    tempDataList = dataBaseManager2.query2ListMap("select * from " + DBHelper2.GOLD_BEAN_TABLE + " where name = ?;", new String[]{user.getUsername()}, new GoldBean());
                    //前后调换位置，倒着显示,最多显示50条
                    if (tempDataList.size() > 0){
                        if (tempDataList.size() <= 50){
                            for (int i = tempDataList.size() - 1; i >= 0; i--){
                                dataList.add(tempDataList.get(i));
                            }
                        }else{
                            for (int i = tempDataList.size() - 1; i >= tempDataList.size() - 49; i--){
                                dataList.add(tempDataList.get(i));
                            }
                        }
                        goldBeanRecordAdapter.updateData(dataList);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBaseManager2.close();
    }
}
