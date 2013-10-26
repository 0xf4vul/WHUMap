package com.whumap.map;

import com.amap.api.maps.model.LatLng;

public class Constants {

	public static final int ERROR = 1001;// 网络异常
	public static final int ROUTE_START_SEARCH = 2000;
	public static final int ROUTE_END_SEARCH = 2001;
	public static final int ROUTE_BUS_RESULT = 2002;// 路径规划中公交模式
	public static final int ROUTE_DRIVING_RESULT = 2003;// 路径规划中驾车模式
	public static final int ROUTE_WALK_RESULT = 2004;// 路径规划中步行模式
	public static final int ROUTE_NO_RESULT = 2005;// 路径规划没有搜索到结果

	public static final int GEOCODER_RESULT = 3000;// 地理编码或者逆地理编码成功
	public static final int GEOCODER_NO_RESULT = 3001;// 地理编码或者逆地理编码没有数据

	public static final int POISEARCH = 4000;// poi搜索到结果
	public static final int POISEARCH_NO_RESULT = 4001;// poi没有搜索到结果
	public static final int POISEARCH_NEXT = 5000;// poi搜索下一页

	public static final int BUSLINE_LINE_RESULT = 6001;// 公交线路查询
	public static final int BUSLINE_id_RESULT = 6002;// 公交id查询
	public static final int BUSLINE_NO_RESULT = 6003;// 异常情况

	public static final LatLng WHU = new LatLng(30.540364,114.361774);//武汉大学
	public static final LatLng WHU1 = new LatLng(30.538289,114.361954);//宋卿体育馆
	public static final LatLng WHU2 = new LatLng(30.538488, 114.364754);//梅园小操场
	public static final LatLng WHU3 = new LatLng(30.540079,114.363741); //校史馆
	public static final LatLng WHU4 = new LatLng(30.532158,114.358993); //珞珈山庄
	public static final LatLng WHU5 = new LatLng(30.537628,114.366404); //行政楼
	public static final LatLng WHU6 = new LatLng(30.534024,114.358608); //牌楼
	public static final LatLng WHU7 = new LatLng(30.536926,114.361301); //六一纪念
	public static final LatLng WHU8 = new LatLng(30.539717,114.363951);//老斋舍
	public static final LatLng WHU9 = new LatLng(30.540225,114.363651);//老图书馆
	public static final LatLng WHU10 = new LatLng(30.539220,114.371052);//法学院
	public static final LatLng WHU11 = new LatLng(30.539929,114.366258);//理学院
	public static final LatLng WHU13 = new LatLng(30.535545, 114.366956); //半山庐
	public static final LatLng WHU12 = new LatLng(30.544734, 114.362192);//工学院
	public static final LatLng WHU14 = new LatLng(30.534182, 114.370871);//十八栋
	
	public static final LatLng WHUV1 = new LatLng(30.534098, 114.358598);
	public static final LatLng WHUV2 = new LatLng(30.538793, 114.361462);
	public static final LatLng WHUV3 = new LatLng(30.532768, 114.367411);
	public static final LatLng WHUV4 = new LatLng(30.543967, 114.360802);
	public static final LatLng WHUV5 = new LatLng(30.541380, 114.357997);
	public static final LatLng WHUV6 = new LatLng(30.541232, 114.370485);
	public static final LatLng WHUV7 = new LatLng(30.538337, 114.372244);
}