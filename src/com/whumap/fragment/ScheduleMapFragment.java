package com.whumap.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.whumap.activity.R;
import com.whumap.map.Constants;

public class ScheduleMapFragment extends Fragment {

	private View view;
	private int position;
	private AMap aMap;
	private MapView mapView;
	private UiSettings mUiSettings;// uisetting
	private Marker mWHUQ;
	private Marker mWHUX;
	private Marker mWHUH;
	private Marker mWHUL;
	private int day;

	static final CameraPosition WHUS = new CameraPosition.Builder()
			.target(Constants.WHU).zoom(17).bearing(0).tilt(0).build();
	static final CameraPosition WHUQ = new CameraPosition.Builder()
			.target(Constants.WHU1).zoom(17).bearing(0).tilt(0).build();
	static final CameraPosition WHUX = new CameraPosition.Builder()
			.target(Constants.WHU2).zoom(17).bearing(0).tilt(0).build();
	static final CameraPosition WHUH = new CameraPosition.Builder()
			.target(Constants.WHU3).zoom(17).bearing(0).tilt(0).build();
	static final CameraPosition WHUL = new CameraPosition.Builder()
			.target(Constants.WHU4).zoom(17).bearing(0).tilt(0).build();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		position = args.getInt("position");
		day = args.getInt("XiaoQing");
		if (day == 28) {
			switch (position) {
			case 2:
			case 4:
			case 12:
			case 13:
				view = inflater
						.inflate(R.layout.schedule_map, container, false);
				initMapView(view, savedInstanceState);
				break;
			default:
				view = inflater.inflate(R.layout.schedule_text, container,
						false);
				initTextView(view, savedInstanceState);
				break;
			}
		} else if (day == 29) {
			switch (position) {
			case 1:
				view = inflater
						.inflate(R.layout.schedule_map, container, false);
				initMapView(view, savedInstanceState);
				break;
			default:
				view = inflater.inflate(R.layout.schedule_text, container,
						false);
				initTextView(view, savedInstanceState);
				break;
			}
		}
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mapView != null)
			mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mapView != null)
			mapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mapView != null)
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

	private void initTextView(View v, Bundle savedInstanceState) {
		v = view.findViewById(R.id.schedule_text);
	}

	private void initMapView(View v, Bundle savedInstanceState) {
		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();
		DefaultUI();
		aMap.clear();
		if (day == 28) {
			switch (position) {
			case 2:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUQ));
				addMarkersWHUQ();
				break;
			case 4:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUH));
				addMarkersWHUH();
				break;
			case 12:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUL));
				addMarkersWHUL();
				break;
			case 13:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUX));
				addMarkersWHUX();
				break;
			default:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUS));
				break;
			}
		} else if (day == 29) {
			switch (position) {
			case 1:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUX));
				addMarkersWHUX();
				break;
			default:
				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUS));
				break;
			}
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
		mUiSettings.setZoomControlsEnabled(false);
		mUiSettings.setMyLocationButtonEnabled(false);
	}

	private void addMarkersWHUX() {
		mWHUX = aMap.addMarker(new MarkerOptions()
				.position(Constants.WHU2)
				.title("梅园小操场")
				.snippet("梅园小操场")
				.icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(
						"地点", "梅园小操场")))).perspective(true)
				// .icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
				.draggable(true));// 设置远小近大效果,2.1.0版本新增
		mWHUX.showInfoWindow();// 设置默认显示一个infowinfow
	}

	private void addMarkersWHUQ() {
		mWHUQ = aMap.addMarker(new MarkerOptions().position(Constants.WHU1)
				.title("宋卿体育馆")
				.snippet("宋卿体育馆")
				 .icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(
				 "地点", "宋卿体育馆")))).perspective(true)
//				.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
				.draggable(true));// 设置远小近大效果,2.1.0版本新增
		mWHUQ.showInfoWindow();// 设置默认显示一个infowinfow
	}

	private void addMarkersWHUH() {
		mWHUH = aMap.addMarker(new MarkerOptions().position(Constants.WHU3)
				.title("校史馆")
				.snippet("校史馆")
				 .icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(
				 "地点", "校史馆")))).perspective(true)
//				.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
				.draggable(true));// 设置远小近大效果,2.1.0版本新增
		mWHUH.showInfoWindow();// 设置默认显示一个infowinfow
	}

	private void addMarkersWHUL() {
		mWHUL = aMap.addMarker(new MarkerOptions().position(Constants.WHU4)
				.title("珞珈山庄")
				.snippet("珞珈山庄")
				 .icon(BitmapDescriptorFactory.fromBitmap(getViewBitmap(getView(
				 "地点", "珞珈山庄")))).perspective(true)
//				.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
				.draggable(true));// 设置远小近大效果,2.1.0版本新增
		mWHUL.showInfoWindow();// 设置默认显示一个infowinfow
	}

	/**
	 * 把一个xml布局文件转化成view
	 */
	public View getView(String title, String text) {
		View view = getLayoutInflater(getArguments()).inflate(R.layout.marker,
				null);
		TextView text_title = (TextView) view.findViewById(R.id.marker_title);
		TextView text_text = (TextView) view.findViewById(R.id.marker_text);
		text_title.setText(title);
		text_text.setText(text);
		return view;
	}

	/**
	 * 把一个view转化成bitmap对象
	 */
	public static Bitmap getViewBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();
		return bitmap;
	}

}
