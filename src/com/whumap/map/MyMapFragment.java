package com.whumap.map;

import android.app.Fragment;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.whumap.activity.R;
import com.whumap.circlebutton.CircleButton;

public class MyMapFragment extends Fragment {
	private String title;
	private final int BASIC_CHILD_BUTTON_ID = 1000;// 初始化子菜单按钮Id
	private CircleButton circleButton;// 新建一个菜单按钮
	private AMap aMap;
	private MapView mapView;
	private MyLocation myLocation;
	private UiSettings mUiSettings;
	private LatLng CUR;
	// 定义功能按钮图片
	private int[] imgResId = { R.drawable.composer_camera,
			R.drawable.composer_music, R.drawable.composer_place,
			R.drawable.composer_sleep, R.drawable.composer_sun,
			R.drawable.composer_thought };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map_fragment, container, false);
		initCircleButton(v);
		initMapView(v, savedInstanceState);
		return v;
	}

	/**
	 * 初始化MapView
	 * 
	 * @param v
	 * @param savedInstanceState
	 */

	private void initMapView(View v, Bundle savedInstanceState) {

		mapView = (MapView) v.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		if (aMap == null) {
			aMap = mapView.getMap();
			DefaultUI();
			myLocation.setUpMap();
		}
	}

	/**
	 * 初始化菜单按钮
	 * 
	 */
	private void initCircleButton(View v) {

		circleButton = (CircleButton) v.findViewById(R.id.test);
		circleButton.init(imgResId, R.drawable.composer_button,
				R.drawable.composer_icn_plus, CircleButton.RIGHTCENTER, 180,
				300);
		// 为菜单的子按钮添加点击监听,将地图的每个功能写在对应的按钮Id中
		circleButton.setChildOnClickListener(new CircleChildButtonOnClick());

	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
		myLocation.deactivate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 当菜单按钮的子按钮被按下时触发 地图的所有功能在不同的Id中
	 * 
	 * @author kb
	 * 
	 */
	private class CircleChildButtonOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == BASIC_CHILD_BUTTON_ID + 0) {
				setLayer();
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 1) {
				aMap.moveCamera(CameraUpdateFactory.changeLatLng(CUR));
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 2) {

			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 3) {

			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 4) {

			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 5) {

			}

		}
	}

	private void setLayer() {
		if (aMap.getMapType() == AMap.MAP_TYPE_SATELLITE) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		} else if (aMap.getMapType() == AMap.MAP_TYPE_NORMAL) {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
		}
	}

	private void DefaultUI() {
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(true);
		mUiSettings.setAllGesturesEnabled(true);
		mUiSettings.setCompassEnabled(true);
		mUiSettings.setRotateGesturesEnabled(true);
		mUiSettings.setScrollGesturesEnabled(true);
		mUiSettings.setTiltGesturesEnabled(true);
		mUiSettings.setZoomGesturesEnabled(true);
		mUiSettings.setMyLocationButtonEnabled(false);
	}

	private class MyLocation implements AMapLocationListener, LocationSource {
		private OnLocationChangedListener mListener;
		private LocationManagerProxy mAMapLocationManager;

		private void setUpMap() {

			// 自定义系统定位小蓝点
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
			myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(Color.YELLOW);// 设置圆形的填充颜色
			// myLocationStyle.anchor(;//设置小蓝点的锚点
			myLocationStyle.strokeWidth(5);// 设置圆形的边框粗细
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setLocationSource(this);// 设置定位监听
			// aMap.getUiSettings().setMyLocationButtonEnabled(false);//
			// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		}

		public void onLocationChanged(Location alocation) {
			// TODO Auto-generated method stub

		}

		public void activate(OnLocationChangedListener listener) {
			// TODO Auto-generated method stub
			mListener = listener;
			if (mAMapLocationManager == null) {
				mAMapLocationManager = LocationManagerProxy
						.getInstance(getActivity());
				/*
				 * mAMapLocManager.setGpsEnable(false);
				 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
				 * Location API定位采用GPS和网络混合定位方式
				 * ，第一个参数是定位provider，第二个参数时间最短是5000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
				 */
				mAMapLocationManager.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 5000, 10, this);
			}
		}

		public void deactivate() {
			// TODO Auto-generated method stub
			mListener = null;
			if (mAMapLocationManager != null) {
				mAMapLocationManager.removeUpdates(this);
				mAMapLocationManager.destory();
			}
			mAMapLocationManager = null;
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(AMapLocation alocation) {
			// TODO Auto-generated method stub

			if (mListener != null) {
				mListener.onLocationChanged(alocation);// 显示系统小蓝点
				CUR = new LatLng(alocation.getLatitude(),
						alocation.getLongitude());
			}

		}
	}
}
