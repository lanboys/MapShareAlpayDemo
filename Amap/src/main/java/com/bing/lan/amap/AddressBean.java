package com.bing.lan.amap;

import java.io.Serializable;

/**
 * @author 蓝兵
 * @email lan_bing2013@163.com
 * @time 2017/4/16  15:38
 */
public class AddressBean  implements Serializable {
    private double longitude;//经度
    private double latitude;//纬度
    private String title;//信息标题
    private String text;//信息内容
    public AddressBean(double lon, double lat, String title, String text){
        this.longitude = lon;
        this.latitude = lat;
        this.title = title;
        this.text = text;
    }
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public String getTitle() {
        return title;
    }
    public String getText(){
        return text;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "经度=" + latitude +
                ", 纬度=" + longitude +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}