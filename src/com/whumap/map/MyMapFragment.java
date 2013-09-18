package com.whumap.map;


import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapFragment;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.whumap.activity.R;
import com.whumap.circlebutton.CircleButton;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyMapFragment extends Fragment {

	private String title;
	private final int BASIC_CHILD_BUTTON_ID = 1000;//初始化子菜单按钮Id
	private  CircleButton circleButton ;//新建一个菜单按钮
	private MapFragment mapFragment ;
	private AMap aMap;
	private MapView mapView;
	private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	//定义功能按钮图片
	private int[] imgResId = {
			R.drawable.composer_camera,
			R.drawable.composer_music,
			R.drawable.composer_place,
			R.drawable.composer_sleep,
			R.drawable.composer_sun,
			R.drawable.composer_thought
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map_fragment, container , false);
		initCircleButton(v , savedInstanceState);
		return v;
	}
	

	/**
	 * 初始化菜单按钮
	 * 
	 */
	public void initCircleButton(View v , Bundle savedInstanceState) { 
		
		circleButton = (CircleButton)v.findViewById(R.id.test); circleButton.init(imgResId , R.drawable.composer_button ,R.drawable.composer_icn_plus,CircleButton.RIGHTCENTER,180,300) ;
		
		/*
		 * 为菜单的子按钮添加点击监听,将地图的每个功能写在对应的按钮Id中
		 */
		OnClickListener onClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(getId() == BASIC_CHILD_BUTTON_ID +0) {
				
				} else if(getId() == BASIC_CHILD_BUTTON_ID +1) {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +2) {
				
				} else if(getId() == BASIC_CHILD_BUTTON_ID +3) {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +4)  {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +5) {
					}
				
				circleButton.collapse();
				}
		};
		
		circleButton.setOnClickListener(onClick);
		
		//获得地图fragment的父容器，当菜单按钮被点击后，如果点击屏幕则将菜单收回
		RelativeLayout ll = (RelativeLayout) v.findViewById(R.id.map_container);
		ll.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(circleButton.getButtonState())
				circleButton.collapse();
			}
		});
		
		mapView = (MapView)v.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		if(aMap == null) {
			aMap = mapView.getMap();
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
		//deactivate();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setRetainInstance(true);
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 停止定位
	 */

}
