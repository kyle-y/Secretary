package com.example.yxb.secretary.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.CityChooseActivity;
import com.example.yxb.secretary.adapter.OtherDaysWeatherAdapter;
import com.example.yxb.secretary.adapter.WeatherDetailsAdapter;
import com.example.yxb.secretary.common.Codes;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.common.db.DBHelper;
import com.example.yxb.secretary.common.db.DataBaseManager;
import com.example.yxb.secretary.deroctions.DividerItemlistDraction;
import com.example.yxb.secretary.utils.FastJson;
import com.example.yxb.secretary.utils.PreferencesUtils;
import com.example.yxb.secretary.utils.StreamUtils;
import com.example.yxb.secretary.utils.UiUtils;
import com.example.yxb.secretary.utils.xml.XmlDataParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class WeatherFragment extends Fragment implements View.OnClickListener{
    private XmlDataParser xmlDataParser;
    private DBHelper dbHelper;
    private DataBaseManager dbManager;
    private String cityId;
    private String cityName,weatherTime,weather,wet,wind,windLevel,tem;
    private boolean isExpending = false;
    private LocationClient locationClient;

    private TextView text_choose_city, text_weather_city, text_weather_time, text_wind, text_wind_level, text_weather, text_tem, text_wet;
    private RecyclerView list_weather;
    private ListView list_other_weatherInfo;
    private ImageView image_weather_icon,image_expend,image_location_button;

    private OtherDaysWeatherAdapter otherDaysWeatherAdapter;
    private WeatherDetailsAdapter weatherDetailsAdapter;

    private List<Map<String, String>> otherCitiesInfos;
    private List<Map<String, Object>> detailsInfos;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_weather, null);

        dbHelper = new DBHelper(getActivity());

        text_weather_city = (TextView) view.findViewById(R.id.text_weather_city);
        text_weather_time = (TextView) view.findViewById(R.id.text_weather_time);
        text_wind = (TextView) view.findViewById(R.id.text_wind);
        text_wind_level = (TextView) view.findViewById(R.id.text_wind_level);
        text_weather = (TextView) view.findViewById(R.id.text_weather);
        text_tem = (TextView) view.findViewById(R.id.text_tem);
        text_wet = (TextView) view.findViewById(R.id.text_wet);

        image_weather_icon = (ImageView) view.findViewById(R.id.image_weather_icon);
        image_expend = (ImageView) view.findViewById(R.id.image_expend);
        image_expend.setOnClickListener(this);

        text_choose_city = (TextView) view.findViewById(R.id.text_choose_city);
        text_choose_city.setOnClickListener(this);

        otherCitiesInfos = new ArrayList<>();
        list_weather = (RecyclerView) view.findViewById(R.id.list_weather);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_weather.setLayoutManager(linearLayoutManager);
        otherDaysWeatherAdapter = new OtherDaysWeatherAdapter(otherCitiesInfos);
        DividerItemlistDraction dividerItemlistDraction = new DividerItemlistDraction(MyApplication.getContext(),LinearLayoutManager.HORIZONTAL);
        dividerItemlistDraction.setSize(10);
        list_weather.addItemDecoration(dividerItemlistDraction);
        list_weather.setAdapter(otherDaysWeatherAdapter);

        detailsInfos = new ArrayList<>();
        weatherDetailsAdapter = new WeatherDetailsAdapter(detailsInfos);
        list_other_weatherInfo = (ListView) view.findViewById(R.id.list_other_weatherInfo);
        list_other_weatherInfo.setAdapter(weatherDetailsAdapter);

        dbManager = DataBaseManager.getInstance(MyApplication.getContext());
        if (PreferencesUtils.getBoolean(MyApplication.getContext(),"IS_HAVE_WEATHER_DATA") == false){
            new DBTask().execute();
        }

        //进来默认获取获取郑州的数据
        cityId = 101180101 + "";
        getWeatherInfos(cityId);

        locationClient = new LocationClient(MyApplication.getContext());
        initLocation();
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Log.i("aaa",bdLocation.getLocType() + "");
                String addr = bdLocation.getAddrStr();
                int indexStart = addr.indexOf("省");
                int indexEnd = addr.indexOf("市");
                String city = addr.substring(indexStart + 1,indexEnd);
                try {
                    Cursor cursor = dbManager.queryData2Cursor("select * from " + DBHelper.WEATHER_CITY_TABLE + " where name = ?;",new String[]{city});
                    if (cursor.getCount() > 0){
                        cursor.moveToFirst();
                        cityId = cursor.getString(cursor.getColumnIndex("city_num"));
                        getWeatherInfos(cityId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                locationClient.stop();
            }
        });
        image_location_button = (ImageView) view.findViewById(R.id.image_location_button);
        image_location_button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_choose_city:
                Intent intent = new Intent(MyApplication.getContext(), CityChooseActivity.class);
                startActivityForResult(intent, Codes.REQUEST_CODE_WEATHER);
                break;
            case R.id.image_expend:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (!isExpending){
                    params.height = UiUtils.dip2px(200);
                    list_other_weatherInfo.setLayoutParams(params);
                    image_expend.setImageResource(R.drawable.arrow_up);
                    isExpending = true;
                }else{
                    params.height = UiUtils.dip2px(0);
                    list_other_weatherInfo.setLayoutParams(params);
                    image_expend.setImageResource(R.drawable.arrow_down);
                    isExpending = false;
                }
                break;
            case R.id.image_location_button:
                locationClient.start();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codes.REQUEST_CODE_WEATHER && resultCode == Codes.RESULT_CODE_WEATHER){
            cityId = data.getStringExtra("CITY_ID");
            getWeatherInfos(cityId);
        }
    }


    public void getWeatherInfos(String cityID){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url("http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId="
                +cityID
                +"&imei=e32c8a29d0e8633283737f5d9f381d47&device=HM2013023&miuiVersion=JHBCNBD16.0&modDevice=&source=miuiWeatherApp")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Map<String,Object> infoMap = FastJson.getMapObj(result);
                Map<String,Object> forecastMap = FastJson.getMapObj(infoMap.get("forecast").toString());
                cityName = forecastMap.get("city").toString();
                for (int i = 1; i <= 5; i++){
                    Map<String, String> day = new HashMap<String, String>();
                    if(i == 1) {
                        day.put("time", "明天");
                    }else if(i == 2){
                        day.put("time", "后天");
                    }else {
                        day.put("time",i-1 + "天后");
                    }
                    day.put("weather",forecastMap.get("weather"+ i).toString());
                    day.put("tem",forecastMap.get("temp" + i).toString());
                    day.put("wind",forecastMap.get("wind" + i).toString());
                    day.put("wind_level",forecastMap.get("fl" + i).toString());
                    otherCitiesInfos.add(day);
                }
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        otherDaysWeatherAdapter.updateData(otherCitiesInfos);//更新数据
                    }
                });
                detailsInfos = FastJson.getListMap(infoMap.get("index").toString());
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weatherDetailsAdapter.update(detailsInfos);//更新数据
                    }
                });
                Map<String, Object> realTimeMap = FastJson.getMapObj(infoMap.get("realtime").toString());
                weatherTime = realTimeMap.get("time").toString();
                wet = realTimeMap.get("SD").toString();
                wind = realTimeMap.get("WD").toString();
                windLevel = realTimeMap.get("WS").toString();
                tem = realTimeMap.get("temp").toString();
                weather = realTimeMap.get("weather").toString();
                //更新数据
                UiUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshUI();
                    }
                });
            }
        });
    }
    private String[] tempTimes = {"20","21","22","23","00","01","02","03","04","05","06"};
    private void refreshUI(){
        text_weather_city.setText(cityName);
        text_weather_time.setText(weatherTime);
        text_wind.setText(wind);
        text_wind_level.setText(windLevel);
        text_weather.setText(weather);
        text_tem.setText(tem);
        text_wet.setText(wet);
        for (int i = 0; i < tempTimes.length; i++){
            if (weatherTime.substring(0,2).equals(tempTimes[i])){
                if (weather.contains("晴"))
                    image_weather_icon.setImageResource(R.drawable.qing_night_1);
                if (weather.contains("多云"))
                    image_weather_icon.setImageResource(R.drawable.duoyun_night_1);
                break;
            }else {
                if (weather.contains("晴"))
                    image_weather_icon.setImageResource(R.drawable.qing_day_1);
                if (weather.contains("多云"))
                    image_weather_icon.setImageResource(R.drawable.duoyun_day_1);
            }
        }
        if (weather.contains("雷"))
            image_weather_icon.setImageResource(R.drawable.lei_1);
        if (weather.contains("雨"))
            image_weather_icon.setImageResource(R.drawable.yu_1 );
        if (weather.contains("雪"))
            image_weather_icon.setImageResource(R.drawable.xue_1);
        if (weather.contains("雾"))
            image_weather_icon.setImageResource(R.drawable.wu_1);
        if (weather.contains("阴"))
            image_weather_icon.setImageResource(R.drawable.yin_1);
    }

    class DBTask extends AsyncTask<Void, Float, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            List<HashMap<String, Object>> listProvinces = null;
            List<HashMap<String, Object>> listCities = null;
            int resultCode = 0;

            InputStream inputStreamProvinces = getResources().openRawResource(R.raw.weather_provinces);
            InputStream inputStreamCities = getResources().openRawResource(R.raw.weather_cities_without_dot);

            String provincesString = null;
            String citiesString = null;

            try {
                provincesString = StreamUtils.inputStream2String(inputStreamProvinces);
                citiesString = StreamUtils.inputStream2String(inputStreamCities);
            } catch (IOException e) {
                e.printStackTrace();
            }

            xmlDataParser = new XmlDataParser();
            if (provincesString != null) {
                xmlDataParser.putRawData(provincesString);
                listProvinces = xmlDataParser.getFormatList("RECORDS");
            }
            if (citiesString != null) {
                xmlDataParser.putRawData(citiesString);
                listCities = xmlDataParser.getFormatList("RECORDS");
            }
            boolean insertProvinceDataSuccessful = false;
            boolean insertCitiesDataSuccessful = false;
            try {
                long num1 = dbManager.insertBatchData2(DBHelper.WEATHER_PROVINCE_TABLE, listProvinces, new String[]{"name","province_id"});
                long num2 = dbManager.insertBatchData2(DBHelper.WEATHER_CITY_TABLE, listCities, new String[]{"province_id", "name", "city_num"});
                if (num1 > 0){
                    insertProvinceDataSuccessful = true;
                }
                if (num2 > 0){
                    insertCitiesDataSuccessful = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (insertProvinceDataSuccessful && insertCitiesDataSuccessful) {
                resultCode = 1;
            }
            return resultCode;
        }
        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 1) {
                PreferencesUtils.putBoolean(MyApplication.getContext(), "IS_HAVE_WEATHER_DATA", true);
            } else {
                PreferencesUtils.putBoolean(MyApplication.getContext(), "IS_HAVE_WEATHER_DATA", false);
            }
            super.onPostExecute(integer);
        }
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setScanSpan(60 * 1000);
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps

        locationClient.setLocOption(option);
//        LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//        locationClient.setLocOption(option);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
}
