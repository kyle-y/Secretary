package com.example.yxb.secretary.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.activity.RoutePlanActivity;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.helpClass.NotifyLister;
import com.example.yxb.secretary.helpClass.PoiOverlay;

/**
 * PACKAGE_NAME:com.example.yxb.secretary.fragment
 * FUNCTIONAL_DESCRIPTION
 * CREATE_BY:xiaobo
 * CREATE_TIME:2016/8/9
 * MODIFY_BY:
 */
public class MapFragment extends Fragment implements View.OnClickListener,TextView.OnEditorActionListener{
    private TextureMapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new Mylistener();
    private NotifyLister mNotifyer;
    private PoiSearch mPoiSearch;
    private String cityName;

    private ImageView switch_tracffic,tracffic;
    private EditText text_search;

    private static final int NORMAL_STATE = 0;
    private static final int TRACFFIC_STATE = 1;
    private static final int HEAT_STATE = 2;
    private int trafficState = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_map, null);

        mapView = (TextureMapView) view.findViewById(R.id.bmapView);
        switch_tracffic = (ImageView) view.findViewById(R.id.switch_tracffic);
        tracffic = (ImageView) view.findViewById(R.id.tracffic);
        text_search = (EditText) view.findViewById(R.id.text_search);

        switch_tracffic.setOnClickListener(this);
        text_search.setOnClickListener(this);
        tracffic.setOnClickListener(this);
        text_search.setOnEditorActionListener(this);

        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
        mapView.setLogoPosition(LogoPosition.logoPostionleftBottom);//设置地图Logo
//        mBaiduMap.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);//可以设置地图边界区域，来避免UI遮挡


        mLocationClient = new LocationClient(MyApplication.getContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();    //start：启动定位SDK。 stop：关闭定位SDK。调用start之后只需要等待定位结果自动回调即可。

       /* //位置提醒相关代码
        mNotifyer = new NotifyLister();
        //注册位置提醒监听事件后，可以通过SetNotifyLocation 来修改位置提醒设置，修改后立刻生效。
        mNotifyer.SetNotifyLocation(42.03249652949337,113.3129895882556,3000,"gps");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)
        mLocationClient.registerNotify(mNotifyer);
        //取消位置提醒
        mLocationClient.removeNotifyEvent(mNotifyer);*/

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.switch_tracffic:
                if (trafficState == 0){
                    trafficState = 1;
                    changeState(trafficState);
                }else if (trafficState == 1){
                    trafficState = 2;
                    changeState(trafficState);
                }else if (trafficState == 2){
                    trafficState = 0;
                    changeState(trafficState);
                }
                break;
            case R.id.edittext_search:
                text_search.setFocusable(true);
                text_search.setFocusableInTouchMode(true);
                text_search.requestFocus();
                InputMethodManager inputManager =
                        (InputMethodManager)text_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(text_search, 0);
                break;
            case R.id.tracffic:
                Intent intent = new Intent();
                intent.setClass(MyApplication.getContext(), RoutePlanActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
       if (actionId == EditorInfo.IME_ACTION_SEARCH){
           //隐藏键盘
           ((InputMethodManager) text_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                   .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
           mPoiSearch = PoiSearch.newInstance();
           mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

           mPoiSearch.searchInCity((new PoiCitySearchOption())
                   .city(cityName)
                   .keyword(text_search.getText().toString())
                   .pageNum(10));
       }
        return false;
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MyApplication.getContext(), "没有搜索结果", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mBaiduMap.clear();
                MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                poiOverlay.setData(result);// 设置POI数据
                mBaiduMap.setOnMarkerClickListener(poiOverlay);
                poiOverlay.addToMap();// 将所有的overlay添加到地图上
                poiOverlay.zoomToSpan();
                //
                int totalPage = result.getTotalPageNum();// 获取总分页数
                Toast.makeText(
                        MyApplication.getContext(),
                        "总共查到" + result.getTotalPoiNum() + "个兴趣点, 分为"
                                + totalPage + "页", Toast.LENGTH_SHORT).show();

            }
        }


        public void onGetPoiDetailResult(PoiDetailResult result){
            Toast.makeText(MyApplication.getContext(), "搜索结果", Toast.LENGTH_SHORT).show();
            //获取Place详情页检索结果
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
            // TODO Auto-generated method stub

        }
    };


    class MyPoiOverlay extends PoiOverlay {
        public MyPoiOverlay(BaiduMap arg0) {
            super(arg0);
        }
        @Override
        public boolean onPoiClick(int arg0) {
            super.onPoiClick(arg0);
            PoiInfo poiInfo = getPoiResult().getAllPoi().get(arg0);
            // 检索poi详细信息
            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption()
                    .poiUid(poiInfo.uid));
            return true;
        }
    }

    class Mylistener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double lat = bdLocation.getLatitude();
            double lon = bdLocation.getLongitude();
            LatLng latLng = new LatLng(lat,lon);
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(mapStatusUpdate);

            String addr = bdLocation.getAddrStr();
            int indexStart = addr.indexOf("省");
            int indexEnd = addr.indexOf("市");
            cityName = addr.substring(indexStart + 1,indexEnd);
        }
    }

    private void changeState(int state){
        switch (state){
            case 0:
                mBaiduMap.setTrafficEnabled(false);
                mBaiduMap.setBaiduHeatMapEnabled(false);
                break;
            case 1:
                mBaiduMap.setTrafficEnabled(true);
                mBaiduMap.setBaiduHeatMapEnabled(false);
                break;
            case 2:
                mBaiduMap.setTrafficEnabled(false);
                mBaiduMap.setBaiduHeatMapEnabled(true);
                break;
        }
    }
}
