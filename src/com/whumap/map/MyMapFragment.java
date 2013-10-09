package com.whumap.map;

import java.util.List;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.overlay.PoiOverlay;
import com.amap.api.services.overlay.WalkRouteOverlay;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.whumap.activity.R;
import com.whumap.circlebutton.CircleButton;
import com.whumap.map.RouteSearchPoiDialog.OnListItemClick;
import com.whumap.util.ToastUtil;
import com.whumap.activity.*;

public class MyMapFragment extends Fragment {

	private final int BASIC_CHILD_BUTTON_ID = 1000;// 初始化子菜单按钮Id
	private CircleButton circleButton;// 新建一个菜单按钮
	private AMap aMap;
	private MapView mapView;
	private MyLocation myLocation;// location
	private UiSettings mUiSettings;// uisetting
	private View v;
	// search
	private static final int SHOW_SUBACTIVITY = 1;
	private MySearchPoi mySearchPoi;
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
	private ProgressDialog progDialog = null;// 搜索时进度条
	private int currentPage = 0;// 当前页面，从0开始计数
	private String keyWord = "";// poi搜索关键字
	private PoiResult poiResult; // poi返回的结果
	private LatLng CUR;
	static final CameraPosition WHUS = new CameraPosition.Builder()
			.target(Constants.WHU).zoom(13).bearing(0).tilt(0).build();
	static final CameraPosition WHUM = new CameraPosition.Builder()
			.target(Constants.WHU).zoom(15).bearing(0).tilt(0).build();
	// route
	private MySearchRoute mySearchRoute;
	private String strStart = "";
	private String strEnd = "";
	private LatLonPoint startPoint = null;
	private LatLonPoint endPoint = null;
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private int routeType = 3;// 1代表公交模式，2代表驾车模式，3代表步行模式
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private int drivingMode = RouteSearch.DrivingDefault;// 驾车默认模式
	private int walkMode = RouteSearch.WalkDefault;// 步行默认模式
	private WalkRouteResult walkRouteResult;// 步行模式查询结果
	private RouteSearch routeSearch;
	private LatLonPoint CURP;

	private Marker mWHUQ;
	private Marker mWHUX;
	private Marker mWHUP;
	private Marker mWHUY;
	private Marker mWHUL;
	private Marker mWHUT;
	private Marker mWHUG;
	private Marker mWHUW;
	private Marker mWHUS;
	private Marker mWHUZ;
	private Marker mWHUB;
	private int MarkerS = 0;
	private BuildingMarker mBuildingMarker;
	private RadioGroup radioOption;
	private int imageId;
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
		v = inflater.inflate(R.layout.map_fragment, container, false);
		initCircleButton();
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
		myLocation = new MyLocation();
		mBuildingMarker = new BuildingMarker();
		if (aMap == null) {
			aMap = mapView.getMap();
			DefaultUI();
			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUS));
			// myLocation.setUpMap();
		}
	}

	/**
	 * 初始化菜单按钮
	 * 
	 */
	private void initCircleButton() {

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
				aMap.animateCamera(CameraUpdateFactory
						.changeLatLng(Constants.WHU));
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 2) {
				Intent intent = new Intent(getActivity(),
						SearchFrameActivity.class);
				startActivityForResult(intent, 0);
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 3) {
				if (MarkerS == 0) {
					mBuildingMarker.addBuildingMarker();
					aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUM));
				} else {
					aMap.clear();
					MarkerS = 0;
				}
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 4) {

			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 5) {

			}

		}
	}

	/**
	 * 用来处理从searchActivity中传回来的参数 首先需要判断传回来的Intent是否为空，如果为空，则不做处理
	 * 如果不判断Intent是否为空，在searchActivity里按下返回按钮时会出现NullPointerException
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent == null)
			return;
		if (requestCode == 0 && resultCode == 0) {
			Bundle dataBundle = intent.getExtras();
			keyWord = dataBundle.getString("key");
			strStart = dataBundle.getString("start");
			strEnd = dataBundle.getString("end");
			if (!("".equals(keyWord))) {
				mySearchPoi = new MySearchPoi();
				mySearchPoi.doSearchQuery();
			}
			if (!("".equals(strEnd))) {
				mySearchRoute = new MySearchRoute();
				if ("我的位置".equals(strStart)) {
					startPoint = CURP;
					mySearchRoute.endSearchResult();
				} else {
					mySearchRoute.startSearchResult();
				}
			}
		} else if (requestCode == 0 && resultCode == 1) {
			return;
		}
	}

	private void setLayer() {
		if (aMap.getMapType() == AMap.MAP_TYPE_SATELLITE) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		} else if (aMap.getMapType() == AMap.MAP_TYPE_NORMAL) {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
		}
	}

	/**
	 * 设置地图相关组件
	 */
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

	private class MyLocation implements AMapLocationListener, LocationSource {
		private OnLocationChangedListener mListener;
		private LocationManagerProxy mAMapLocationManager;

		public void setUpMap() {

			// 自定义系统定位小蓝点
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
			myLocationStyle.strokeColor(R.color.location_edge_background);// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(R.color.location_background);// 设置圆形的填充颜色
			// myLocationStyle.anchor(;//设置小蓝点的锚点
			myLocationStyle.strokeWidth(2);// 设置圆形的边框粗细
			aMap.setMyLocationStyle(myLocationStyle);
			aMap.setLocationSource(this);// 设置定位监听
			// aMap.getUiSettings().setMyLocationButtonEnabled(false);//
			// 设置默认定位按钮是否显示
			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		}

		public void onLocationChanged(Location alocation) {

		}

		public void activate(OnLocationChangedListener listener) {
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
			mListener = null;
			if (mAMapLocationManager != null) {
				mAMapLocationManager.removeUpdates(this);
				mAMapLocationManager.destory();
			}
			mAMapLocationManager = null;
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onLocationChanged(AMapLocation alocation) {

			if (mListener != null) {
				mListener.onLocationChanged(alocation);// 显示系统小蓝点
				CUR = new LatLng(alocation.getLatitude(),
						alocation.getLongitude());
				CURP = new LatLonPoint(alocation.getLatitude(),
						alocation.getLongitude());
			}

		}
	}

	/**
	 * searchpoi
	 * 
	 * @author flsf
	 * 
	 */
	private class MySearchPoi implements OnPoiSearchListener,
			OnMarkerClickListener, InfoWindowAdapter {

		protected void doSearchQuery() {

			aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
			aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
			showProgressDialog();// 显示进度框
			currentPage = 0;
			query = new PoiSearch.Query(keyWord, "", "027".toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
			query.setPageSize(10);// 设置每页最多返回多少条poiitem
			query.setPageNum(currentPage);// 设置查第一页

			poiSearch = new PoiSearch(getActivity(), query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();
		}

		private void showProgressDialog() {
			if (progDialog == null)
				progDialog = new ProgressDialog(getActivity());
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(false);
			progDialog.setMessage("正在搜索:\n" + keyWord);
			progDialog.show();
		}

		/**
		 * 隐藏进度框
		 */
		private void dissmissProgressDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}

		@Override
		public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

		}

		@Override
		public View getInfoContents(Marker arg0) {
			return null;
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			return null;
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			marker.showInfoWindow();
			return false;
		}

		@Override
		public void onPoiSearched(PoiResult result, int rCode) {
			dissmissProgressDialog();// 隐藏对话框
			if (rCode == 0) {
				if (result != null && result.getQuery() != null) {// 搜索poi的结果
					if (result.getQuery().equals(query)) {// 是否是同一条
						poiResult = result;
						// 取得搜索到的poiitems有多少页
						List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
						if (poiItems != null && poiItems.size() > 0) {
							aMap.clear();// 清理之前的图标
							PoiOverlay poiOverlay = new PoiOverlay(aMap,
									poiItems);
							poiOverlay.removeFromMap();
							poiOverlay.addToMap();
							poiOverlay.zoomToSpan();
						}
					}
				}
			}
		}
	}

	/**
	 * route
	 * 
	 * @author flsf
	 * 
	 */
	private class MySearchRoute implements OnRouteSearchListener,
			OnPoiSearchListener, InfoWindowAdapter, OnClickListener,
			OnInfoWindowClickListener, OnMarkerClickListener {

		@Override
		public void onBusRouteSearched(BusRouteResult arg0, int arg1) {
		}

		@Override
		public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
		}

		@Override
		public void onWalkRouteSearched(WalkRouteResult result, int rCode) {
			dissmissProgressDialog();

			if (rCode == 0) {
				if (result != null && result.getPaths() != null
						&& result.getPaths().size() > 0) {
					walkRouteResult = result;
					WalkPath walkPath = walkRouteResult.getPaths().get(0);
					aMap.clear();// 清理地图上的所有覆盖物
					WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
							getActivity(), aMap, walkPath,
							walkRouteResult.getStartPos(),
							walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
				} else {
					ToastUtil.showLong(getActivity(), R.string.no_result);
				}
			} else {
				ToastUtil.showLong(getActivity(), R.string.error_network);
			}
		}

		/**
		 * 显示进度框
		 */
		private void showProgressDialog() {
			if (progDialog == null)
				progDialog = new ProgressDialog(getActivity());
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setIndeterminate(false);
			progDialog.setCancelable(true);
			progDialog.setMessage("正在搜索");
			progDialog.show();
		}

		/**
		 * 隐藏进度框
		 */
		private void dissmissProgressDialog() {
			if (progDialog != null) {
				progDialog.dismiss();
			}
		}

		/**
		 * 查询路径规划起点
		 */
		public void startSearchResult() {

			if (startPoint != null && strStart.equals("地图上的点")) {
				endSearchResult();
			} else {
				showProgressDialog();
				startSearchQuery = new PoiSearch.Query(strStart, "", "027"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
				startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
				startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
				PoiSearch poiSearch = new PoiSearch(getActivity(),
						startSearchQuery);
				poiSearch.setOnPoiSearchListener(this);
				poiSearch.searchPOIAsyn();// 异步poi查询
			}
		}

		/**
		 * 查询路径规划终点
		 */
		public void endSearchResult() {
			if (endPoint != null && strEnd.equals("地图上的点")) {
				searchRouteResult(startPoint, endPoint);
			} else {
				showProgressDialog();
				endSearchQuery = new PoiSearch.Query(strEnd, "", "027"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
				endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
				endSearchQuery.setPageSize(20);// 设置每页返回多少条数据
				PoiSearch poiSearch = new PoiSearch(getActivity(),
						endSearchQuery);
				poiSearch.setOnPoiSearchListener(this);
				poiSearch.searchPOIAsyn(); // 异步poi查询
			}
		}

		/**
		 * 开始搜索路径规划方案
		 */
		public void searchRouteResult(LatLonPoint startPoint,
				LatLonPoint endPoint) {
			aMap.setOnMarkerClickListener(this);
			aMap.setOnInfoWindowClickListener(this);
			aMap.setInfoWindowAdapter(this);
			routeSearch = new RouteSearch(getActivity());
			routeSearch.setRouteSearchListener(this);
			showProgressDialog();
			final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
					startPoint, endPoint);
			if (routeType == 1) {// 公交路径规划
				BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode,
						"027", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
				routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
			} else if (routeType == 2) {// 驾车路径规划
				DriveRouteQuery query = new DriveRouteQuery(fromAndTo,
						drivingMode, null, null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
				routeSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
			} else if (routeType == 3) {// 步行路径规划
				WalkRouteQuery query = new WalkRouteQuery(fromAndTo, walkMode);
				routeSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
			}
		}

		/**
		 * POI搜索结果回调
		 */
		@Override
		public void onPoiSearched(PoiResult result, int rCode) {
			dissmissProgressDialog();
			if (rCode == 0) {// 返回成功
				if (result != null && result.getQuery() != null
						&& result.getPois() != null
						&& result.getPois().size() > 0) {// 搜索poi的结果
					if (!("我的位置".equals(strStart))
							&& result.getQuery().equals(startSearchQuery)) {
						List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
						RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
								getActivity(), poiItems);
						dialog.setTitle("您要找的起点是:");
						dialog.show();
						dialog.setOnListClickListener(new OnListItemClick() {
							@Override
							public void onListItemClick(
									RouteSearchPoiDialog dialog,
									PoiItem startpoiItem) {
								startPoint = startpoiItem.getLatLonPoint();
								strStart = startpoiItem.getTitle();
								endSearchResult();// 开始搜终点
							}

						});
					} else if (result.getQuery().equals(endSearchQuery)) {
						List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
						RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
								getActivity(), poiItems);
						dialog.setTitle("您要找的终点是:");
						dialog.show();
						dialog.setOnListClickListener(new OnListItemClick() {
							@Override
							public void onListItemClick(
									RouteSearchPoiDialog dialog,
									PoiItem endpoiItem) {
								endPoint = endpoiItem.getLatLonPoint();
								strEnd = endpoiItem.getTitle();
								searchRouteResult(startPoint, endPoint);// 进行路径规划搜索
							}

						});
					}
				} else {
					ToastUtil.showLong(getActivity(), R.string.no_result);
				}
			} else {
				ToastUtil.showLong(getActivity(), R.string.error_network);
			}
		}

		@Override
		public void onClick(View v) {

		}

		@Override
		public View getInfoContents(Marker arg0) {
			return null;
		}

		@Override
		public View getInfoWindow(Marker arg0) {
			return null;
		}

		@Override
		public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {

		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			return false;
		}

		@Override
		public void onInfoWindowClick(Marker arg0) {

		}
	}

	private class BuildingMarker implements OnMarkerClickListener,
			OnInfoWindowClickListener, InfoWindowAdapter {

		private void addBuildingMarker() {
			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			MarkerS = 1;
			mWHUQ = aMap.addMarker(new MarkerOptions().position(Constants.WHU1)
					.title("宋卿体育馆").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUX = aMap.addMarker(new MarkerOptions().position(Constants.WHU5)
					.title("行政楼").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUP = aMap.addMarker(new MarkerOptions().position(Constants.WHU6)
					.title("牌楼").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUY = aMap.addMarker(new MarkerOptions().position(Constants.WHU7)
					.title("六一纪念亭").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUZ = aMap.addMarker(new MarkerOptions().position(Constants.WHU8)
					.title("老斋舍").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUT = aMap.addMarker(new MarkerOptions().position(Constants.WHU9)
					.title("老图书馆").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUW = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU10).title("文法学院").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUL = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU11).title("理学院").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUG = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU12).title("工学院").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUB = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU13).title("半山庐").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
			mWHUS = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU14).title("十八栋").snippet("点击查看详情")
					.icon(BitmapDescriptorFactory.fromAsset("arrow.png"))
					.draggable(true));
		}

		@Override
		public void onInfoWindowClick(Marker marker) {
			Bundle id = new Bundle();
			id.putInt("name", imageId);
			Intent intent = new Intent(getActivity(), BuildingText.class);
			intent.putExtras(id);
			startActivity(intent);
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.equals(mWHUP))
				imageId = 0;
			else if (marker.equals(mWHUX))
				imageId = 1;
			else if (marker.equals(mWHUY))
				imageId = 2;
			else if (marker.equals(mWHUQ))
				imageId = 3;
			else if (marker.equals(mWHUZ))
				imageId = 4;
			else if (marker.equals(mWHUT))
				imageId = 5;
			else if (marker.equals(mWHUW))
				imageId = 6;
			else if (marker.equals(mWHUL))
				imageId = 7;
			else if (marker.equals(mWHUG))
				imageId = 8;
			else if (marker.equals(mWHUB))
				imageId = 9;
			else if (marker.equals(mWHUS))
				imageId = 10;
			return false;
		}

		@Override
		public View getInfoContents(Marker marker) {
			View infoContent = getLayoutInflater(getArguments()).inflate(
					R.layout.custom_info_contents, null);
			render(marker, infoContent);
			return infoContent;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			View infoWindow = getLayoutInflater(getArguments()).inflate(
					R.layout.custom_info_window, null);

			render(marker, infoWindow);
			return infoWindow;
		}

		/**
		 * 自定义infowinfow窗口
		 */
		public void render(Marker marker, View view) {
			String title = marker.getTitle();
			TextView titleUi = ((TextView) view.findViewById(R.id.title));
			if (title != null) {
				SpannableString titleText = new SpannableString(title);
				titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
						titleText.length(), 0);
				titleUi.setTextSize(13);
				titleUi.setText(titleText);

			} else {
				titleUi.setText("");
			}
			String snippet = marker.getSnippet();
			TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
			if (snippet != null) {
				SpannableString snippetText = new SpannableString(snippet);
				snippetText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
						snippetText.length(), 0);
				snippetUi.setTextSize(10);
				snippetUi.setText(snippetText);
			} else {
				snippetUi.setText("");
			}
		}

	}

}
