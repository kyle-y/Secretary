package com.example.yxb.secretary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.yxb.secretary.R;
import com.example.yxb.secretary.adapter.CityAdapter;
import com.example.yxb.secretary.adapter.ProvinceAdapter;
import com.example.yxb.secretary.bean.WeatherIDCityBean;
import com.example.yxb.secretary.bean.WeatherIDProvinceBean;
import com.example.yxb.secretary.common.Codes;
import com.example.yxb.secretary.common.db.DBHelper;
import com.example.yxb.secretary.common.db.DataBaseManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/6/14.
 */
public class CityChooseActivity extends Activity implements AdapterView.OnItemClickListener,View.OnClickListener,SearchView.OnQueryTextListener,TextWatcher{
    private Button button_back;
    private TextView textview_choosearea;
    private SearchView searchview_city;
    private EditText edittext_search;

    private RelativeLayout layout_listview;
    private RelativeLayout layout_gridview;
    private GridView gridview_id_dialog;
    private ListView listview_id_dialog;
    private ProvinceAdapter provinceAdapter;
    private CityAdapter cityAdapter;

    private List<Map<String, Object>> listItemProvince;
    private List<Map<String, Object>> listItemCity;
    private  String provinceID;
    private  String provinceName;
    private  String cityID;
    private  String cityName;

    private DataBaseManager<?> dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);

        initView();
        initData();
        initListener();

    }

    public void initView(){
        button_back = (Button) findViewById(R.id.button_back);
        textview_choosearea = (TextView) findViewById(R.id.textview_choosearea);
        layout_listview = (RelativeLayout) findViewById(R.id.layout_listview);
        layout_gridview = (RelativeLayout) findViewById(R.id.layout_gridview);
        gridview_id_dialog = (GridView) findViewById(R.id.gridview_id_dialog);
        listview_id_dialog = (ListView) findViewById(R.id.listview_id_dialog);
        searchview_city = (SearchView) findViewById(R.id.searchview_city);
        edittext_search = (EditText) findViewById(R.id.edittext_search);

        provinceAdapter = new ProvinceAdapter(getLayoutInflater());
        cityAdapter = new CityAdapter(getLayoutInflater());
        gridview_id_dialog.setAdapter(provinceAdapter);
        listview_id_dialog.setAdapter(cityAdapter);

        P_Cavilible(1);
    }

    public void initData(){
        dbManager = DataBaseManager.getInstance(this);

            Executors.newCachedThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        listItemProvince = dbManager.query2ListMap("select * from " + DBHelper.WEATHER_PROVINCE_TABLE  + ";",null, new WeatherIDProvinceBean());
                        Message msg = MyHandler.obtainMessage();
                        msg.what = 0;
                        MyHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


    }

    public void initListener(){
        gridview_id_dialog.setOnItemClickListener(this);
        listview_id_dialog.setOnItemClickListener(this);
        button_back.setOnClickListener(this);
        searchview_city.setOnQueryTextListener(this);
        edittext_search.addTextChangedListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.gridview_id_dialog:

                provinceID = provinceAdapter.getItem(position).get("province_id").toString();
                provinceName = provinceAdapter.getItem(position).get("name").toString();

                Executors.newCachedThreadPool().execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listItemCity = dbManager.query2ListMap("select * from " + DBHelper.WEATHER_CITY_TABLE + " where province_id=?;", new String[]{provinceID}, new WeatherIDCityBean());
                            Message msg = MyHandler.obtainMessage();
                            msg.what = 1;
                            MyHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                P_Cavilible(2);
                break;

            case R.id.listview_id_dialog:

                cityID = cityAdapter.getItem(position).get("city_num").toString();
                cityName = cityAdapter.getItem(position).get("name").toString();

                Intent intent = new Intent();
                intent.putExtra("PROVINCE_NAME",provinceName);
                intent.putExtra("PROVINCE_ID",provinceID);
                intent.putExtra("CITY_NAME",cityName);
                intent.putExtra("CITY_ID",cityID);
                setResult(Codes.RESULT_CODE_WEATHER,intent);

                finish();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finish_view();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back :
                finish_view();
                break;
        }
    }
    //省份和城市列表按返回不同效果
    private void finish_view(){
        if (layout_gridview.getVisibility() == View.VISIBLE){
            finish();
        }else{
            P_Cavilible(1);
        }
    }
    //省份和城市交替可见
    public void P_Cavilible(int code){
        switch (code){
            case 1:
                layout_gridview.setVisibility(View.VISIBLE);
                layout_listview.setVisibility(View.GONE);
                textview_choosearea.setText("选择省份");
                break;
            case 2:
                layout_gridview.setVisibility(View.GONE);
                layout_listview.setVisibility(View.VISIBLE);
                textview_choosearea.setText("选择城市");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            listItemCity = dbManager.query2ListMap("select * from " + DBHelper.WEATHER_CITY_TABLE + " where province_id = ? and name like ?",new String[]{provinceID,"%" + newText + "%"},new WeatherIDCityBean());
            cityAdapter.setListData(listItemCity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            listItemProvince = dbManager.query2ListMap("select * from " + DBHelper.WEATHER_PROVINCE_TABLE + " where  name like ?",new String[]{"%" + s.toString() + "%"},new WeatherIDProvinceBean());
        } catch (Exception e) {
            e.printStackTrace();
        }
        provinceAdapter.setListData(listItemProvince);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    //查询结束后，更新列表
    private Handler MyHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    provinceAdapter.setListData(listItemProvince);
                    break;
                case 1:
                    cityAdapter.setListData(listItemCity);
                    break;
            }
            return false;
        }
    });
}
