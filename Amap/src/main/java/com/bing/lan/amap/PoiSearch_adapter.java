package com.bing.lan.amap;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * @author 蓝兵
 * @email lan_bing2013@163.com
 * @time 2017/4/16  14:42
 */
public class PoiSearch_adapter extends BaseAdapter {

    List<PoiItem> poiItems;
    Main2Activity main2Activity;

    public PoiSearch_adapter(Main2Activity main2Activity, List<PoiItem> poiItems) {

        this.poiItems = poiItems;
        this.main2Activity = main2Activity;
    }

    @Override
    public int getCount() {
        return poiItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View inflate = View.inflate(main2Activity, R.layout.item, null);
        TextView viewById = (TextView) inflate.findViewById(R.id.tv_title);
        TextView viewById1 = (TextView) inflate.findViewById(R.id.tv_detail);

        viewById.setText(poiItems.get(position).getTitle());
        viewById1.setText(poiItems.get(position).getCityName());


        return inflate;
    }
}
