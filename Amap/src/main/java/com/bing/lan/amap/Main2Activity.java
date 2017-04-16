package com.bing.lan.amap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.List;


public class Main2Activity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener {

    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_DES = "des";
    MapView mapView;
    ListView mapList;
    private AMapLocationClient mLocationClient;
    private LocationSource.OnLocationChangedListener mListener;
    private LatLng latlng;
    private String city;
    private AMap aMap;
    private String deepType = "";// poi搜索类型
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;
    private PoiResult poiResult; // poi返回的结果
    private PoiOverlay poiOverlay;// poi图层
    private List<PoiItem> poiItems;// poi数据

    private PoiSearch_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // ButterKnife.bind(this);


        mapView= (MapView) findViewById(R.id.map_local);
        mapList = (ListView) findViewById(R.id.map_list);


        mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
            setUpMap();
        }

        deepType = "餐饮";//这里以餐饮为例
    }

    private void setUpMap() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getApplicationContext());
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.position));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
        int currentPage = 0;
        query = new PoiSearch.Query("", deepType, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        LatLonPoint lp = new LatLonPoint(latlng.latitude, latlng.longitude);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));
        // 设置搜索区域为以lp点为圆心，其周围2000米范围
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 显示我的位置
                mListener.onLocationChanged(aMapLocation);
                //设置第一次焦点中心
                latlng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14), 1000, null);
                city = aMapLocation.getProvince();
                doSearchQuery();
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        mLocationClient.startLocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
    }


    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        latlng = cameraPosition.target;
        aMap.clear();
        aMap.addMarker(new MarkerOptions().position(latlng));
        doSearchQuery();
    }


    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();
                    if (poiItems != null && poiItems.size() > 0) {
                        adapter = new PoiSearch_adapter(this, poiItems);
                        mapList.setAdapter(adapter);
                        mapList.setOnItemClickListener(new mOnItemClickListener());

                    }
                }
                else {
                   // Logger.d("无结果");
                }
            }
        } else {
            //Logg.e("无结果");
        }
    // } else if (rCode == 27) {
    //     Logger.e("error_network");
    // } else if (rCode == 32) {
    //     Logger.e("error_key");
    // } else {
    //     Logger.e("error_other：" + rCode);
    // }

}

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    //-------- 定位 End ------

    @Override
    protected void onResume() {
        super.onResume();
        mLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationClient.stopLocation();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.onDestroy();
        super.onDestroy();
    }

  class mOnItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(KEY_LAT, poiItems.get(position).getLatLonPoint().getLatitude());
        intent.putExtra(KEY_LNG, poiItems.get(position).getLatLonPoint().getLongitude());
        intent.putExtra(KEY_DES, poiItems.get(position).getTitle());
        setResult(RESULT_OK, intent);
        finish();
    }
}
}