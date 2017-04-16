package com.bing.lan.amap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

/**
 * @author 蓝兵
 * @email lan_bing2013@163.com
 * @time 2017/4/16  15:40
 */
public class InputTask implements PoiSearch.OnPoiSearchListener {

    private static InputTask mInstance;
    private SearchAdapter mAdapter;
    private PoiSearch mSearch;
    private Context mContext;

    private InputTask(Context context, SearchAdapter adapter) {
        this.mContext = context;
        this.mAdapter = adapter;
    }

    /**
     * 获取实例
     *
     * @param context 上下文
     * @param adapter 数据适配器
     * @return
     */
    public static InputTask getInstance(Context context, SearchAdapter adapter) {
        if (mInstance == null) {
            synchronized (InputTask.class) {
                if (mInstance == null) {
                    mInstance = new InputTask(context, adapter);
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置数据适配器
     *
     * @param adapter
     */
    public void setAdapter(SearchAdapter adapter) {
        this.mAdapter = adapter;
    }

    /**
     * POI搜索
     *
     * @param key  关键字
     * @param city 城市
     */
    public void onSearch(String key, String city) {
        //POI搜索条件
        PoiSearch.Query query = new PoiSearch.Query(key, "", city);
        mSearch = new PoiSearch(mContext, query);
        //设置异步监听
        mSearch.setOnPoiSearchListener(this);
        //查询POI异步接口
        mSearch.searchPOIAsyn();
    }

    /**
     * 异步搜索回调
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        if (rCode == 1000 && poiResult != null) {
            final ArrayList<AddressBean> data = new ArrayList<AddressBean>();
            ArrayList<PoiItem> items = poiResult.getPois();
            for (PoiItem item : items) {
                //获取经纬度对象
                LatLonPoint llp = item.getLatLonPoint();
                double lon = llp.getLongitude();
                double lat = llp.getLatitude();
                //获取标题
                String title = item.getTitle();
                //获取内容
                String text = item.getSnippet();
                data.add(new AddressBean(lon, lat, title, text));
            }

            Log.e("amap", "搜索结果数量: " + data.size());
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(data);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }
}
