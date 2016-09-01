package com.example.yxb.secretary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.yxb.secretary.R;
import com.example.yxb.secretary.common.MyApplication;
import com.example.yxb.secretary.helpClass.BusLineOverlay;

import java.util.ArrayList;
import java.util.List;


/**
 * 此demo用来展示如何进行公交线路详情检索，并使用RouteOverlay在地图上绘制 同时展示如何浏览路线节点并弹出泡泡
 */
public class BusLineSearchActivity extends FragmentActivity implements
        OnGetPoiSearchResultListener, OnGetBusLineSearchResultListener,
        BaiduMap.OnMapClickListener {
    private Button mBtnPre = null; // 上一个节点
    private Button mBtnNext = null; // 下一个节点
    private int nodeIndex = -2; // 节点索引,供浏览节点时使用
    private BusLineResult route = null; // 保存驾车/步行路线数据的变量，供浏览节点时使用
    private List<String> busLineIDList = null;
    private int busLineIndex = 0;
    // 搜索相关
    private PoiSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BusLineSearch mBusLineSearch = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new Mylistener();
    private BusLineOverlay overlay; // 公交路线绘制对象
    private String cityName;

    private String city;
    private String line;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busline);
        CharSequence titleLable = "公交线路查询功能";
        setTitle(titleLable);
        mBtnPre = (Button) findViewById(R.id.pre);
        mBtnNext = (Button) findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        mBaiduMap = ((SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.bmapView)).getBaiduMap();
        mBaiduMap.setOnMapClickListener(this);
        mSearch = PoiSearch.newInstance();
        mSearch.setOnGetPoiSearchResultListener(this);
        mBusLineSearch = BusLineSearch.newInstance();
        mBusLineSearch.setOnGetBusLineSearchResultListener(this);
        busLineIDList = new ArrayList<String>();
        overlay = new BusLineOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overlay);

        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        line = intent.getStringExtra("line");

        mLocationClient = new LocationClient(MyApplication.getContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus){
            Log.i("aaa","onWindowFocusChanged");
            searchButtonProcess(city,line);
        }
    }

    /**
     * 发起检索
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        busLineIDList.clear();
        busLineIndex = 0;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        EditText editCity = (EditText) findViewById(R.id.city);
        EditText editSearchKey = (EditText) findViewById(R.id.searchkey);
        // 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
        mSearch.searchInCity((new PoiCitySearchOption()).city(
                editCity.getText().toString())
                        .keyword(editSearchKey.getText().toString()));
    }

    public void searchButtonProcess(String city, String line) {
        busLineIDList.clear();
        busLineIndex = 0;
        mBtnPre.setVisibility(View.INVISIBLE);
        mBtnNext.setVisibility(View.INVISIBLE);
        // 发起poi检索，从得到所有poi中找到公交线路类型的poi，再使用该poi的uid进行公交详情搜索
        mSearch.searchInCity((new PoiCitySearchOption()).city(city).keyword(line));
    }

    public void searchNextBusline(View v) {
        if (busLineIndex >= busLineIDList.size()) {
            busLineIndex = 0;
        }
        if (busLineIndex >= 0 && busLineIndex < busLineIDList.size()
                && busLineIDList.size() > 0) {
            mBusLineSearch.searchBusLine((new BusLineSearchOption()
                    .city(((EditText) findViewById(R.id.city)).getText()
                            .toString()).uid(busLineIDList.get(busLineIndex))));

            busLineIndex++;
        }

    }

    /**
     * 节点浏览示例
     *
     * @param v
     */
    public void nodeClick(View v) {

        if (nodeIndex < -1 || route == null
                || nodeIndex >= route.getStations().size()) {
            return;
        }
        TextView popupText = new TextView(this);
        popupText.setBackgroundResource(R.drawable.popup);
        popupText.setTextColor(0xff000000);
        // 上一个节点
        if (mBtnPre.equals(v) && nodeIndex > 0) {
            // 索引减
            nodeIndex--;
        }
        // 下一个节点
        if (mBtnNext.equals(v) && nodeIndex < (route.getStations().size() - 1)) {
            // 索引加
            nodeIndex++;
        }
        if (nodeIndex >= 0) {
            // 移动到指定索引的坐标
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(route
                    .getStations().get(nodeIndex).getLocation()));
            // 弹出泡泡
            popupText.setText(route.getStations().get(nodeIndex).getTitle());
            mBaiduMap.showInfoWindow(new InfoWindow(popupText, route.getStations()
                    .get(nodeIndex).getLocation(), 10));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        mBusLineSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetBusLineResult(BusLineResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mBaiduMap.clear();
        route = result;
        nodeIndex = -1;
        overlay.removeFromMap();
        overlay.setData(result);
        overlay.addToMap();
        overlay.zoomToSpan();
        mBtnPre.setVisibility(View.VISIBLE);
        mBtnNext.setVisibility(View.VISIBLE);
        Toast.makeText(BusLineSearchActivity.this, result.getBusLineName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetPoiResult(PoiResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BusLineSearchActivity.this, "抱歉，未找到结果",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // 遍历所有poi，找到类型为公交线路的poi
        busLineIDList.clear();
        for (PoiInfo poi : result.getAllPoi()) {
            if (poi.type == PoiInfo.POITYPE.BUS_LINE
                    || poi.type == PoiInfo.POITYPE.SUBWAY_LINE) {
                busLineIDList.add(poi.uid);
            }
        }
        searchNextBusline(null);
        route = null;
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onMapClick(LatLng point) {
        mBaiduMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocationClient.setLocOption(option);
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
            mLocationClient.stop();
        }
    }
}
