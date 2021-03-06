package com.whumap.map;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
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
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
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
import com.whumap.activity.SearchFrameActivity;
import com.whumap.circlebutton.CircleButton;
import com.whumap.map.RouteSearchPoiDialog.OnListItemClick;
import com.whumap.util.ToastUtil;

public class MyMapFragment extends Fragment {

	private final int BASIC_CHILD_BUTTON_ID = 1000;// 初始化子菜单按钮Id
	private CircleButton circleButton;// 新建一个菜单按钮
	private AMap aMap;
	private MapView mapView;
	private MyLocation myLocation;// location
	private UiSettings mUiSettings;// uisetting
	private View v;
	// search
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
	private LatLonPoint CURP = null;

	private Marker mWHUQ;
	private Marker mWHUX;
	private Marker mWHUP;
	private Marker mWHUY;
	private Marker mWHUL;
	private Marker mWHUT;
	private Marker mWHUW;
	private Marker mWHUS;
	private Marker mWHUZ;
	private Marker mWHUB;
	private Marker mCurPoint = null;
	private int MarkerS = 0;
	private int MarkerV = 0;
	private BuildingMarker mBuildingMarker;
	private VolunteerMarker mVolunteerMarker;

	private Marker mWHU1;
	private Marker mWHU2;
	private Marker mWHU3;
	private Marker mWHU4;
	private Marker mWHU5;
	private Marker mWHU6;
	private Marker mWHU7;

	private MyMapClick myMapClick;
	private String addressName;
	private GeocodeSearch geocoderSearch;
	private LatLng mpositionLatLng;

	// 定义功能按钮图片
	private int[] imgResId = { R.drawable.ic_circlebutton_layer,
			R.drawable.ic_circlebutton_position,
			R.drawable.ic_circlebutton_search, R.drawable.ic_circlebutton_view,
			R.drawable.ic_circlebutton_volunteer,
			R.drawable.ic_circlebutton_remove };

	private SharedPreferences settings;
	private Boolean suoFangValue;
	private Boolean biaoChiValue;
	private Boolean zhiNanZhenValue;
	private Boolean shouShiAllValue;
	private Boolean shouShiRotateValue;
	private Boolean shouShiScrollValue;
	private Boolean shouShiTiltValue;
	private Boolean shouShiSuoFangValue;

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
		mVolunteerMarker = new VolunteerMarker();
		myMapClick = new MyMapClick();
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUS));
			if (CURP == null) {
				showLocationDialog();
				myLocation.setUpMap();
			}
		}
	}

	private void showLocationDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(getActivity());
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在定位");
		progDialog.show();
	}

	private void dissmissLocationDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 初始化菜单按钮
	 * 
	 */
	private void initCircleButton() {

		circleButton = (CircleButton) v.findViewById(R.id.test);
		circleButton.init(imgResId, R.drawable.ic_circlebutton_bg,
				R.drawable.ic_circlebutton_foot, CircleButton.RIGHTCENTER, 180,
				360);
		// 为菜单的子按钮添加点击监听,将地图的每个功能写在对应的按钮Id中
		circleButton.setChildOnClickListener(new CircleChildButtonOnClick());
	}

	@Override
	public void onResume() {
		getSettingsKeys();
		SettingsUI();
		myMapClick.initMyMapClick();
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
				if (CURP != null) {
					aMap.animateCamera(CameraUpdateFactory.changeLatLng(CUR));
				} else {
					ToastUtil.showLong(getActivity(), R.string.no_location);
					myLocation.setUpMap();
				}
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 2) {
				if (CURP != null) {
					Intent intent = new Intent(getActivity(),
							SearchFrameActivity.class);
					startActivityForResult(intent, 0);
				} else {
					ToastUtil.showLong(getActivity(), R.string.no_location);
					myLocation.setUpMap();
				}
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 3) {
				if (MarkerS == 0) {
					if (MarkerV == 1) {
						mVolunteerMarker.removeVolunteerMarker();
						MarkerV = 0;
					}
					mBuildingMarker.addBuildingMarker();
					aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUM));
				} else {
					mBuildingMarker.removeBuildingMarker();
					MarkerS = 0;
				}
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 4) {
				if (MarkerV == 0) {
					if (MarkerS == 1) {
						mBuildingMarker.removeBuildingMarker();
						MarkerS = 0;
					}
					mVolunteerMarker.addVolunteerMarker();
					aMap.moveCamera(CameraUpdateFactory.newCameraPosition(WHUM));
				} else {
					mVolunteerMarker.removeVolunteerMarker();
					MarkerV = 0;
				}
			} else if (v.getId() == BASIC_CHILD_BUTTON_ID + 5) {
				aMap.clear();
				MarkerS = 0;
				MarkerV = 0;
				myLocation.setUpMap();
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
					if (CURP == null) {
						ToastUtil.showLong(getActivity(), R.string.no_location);
						myLocation.setUpMap();
					}
					startPoint = CURP;
					mySearchRoute.endSearchResult();
				} else {
					mySearchRoute.startSearchResult();
				}
			}
		} else if (requestCode == 0 && resultCode == 1) {
		}
	}

	private void setLayer() {
		if (aMap.getMapType() == AMap.MAP_TYPE_SATELLITE) {
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		} else if (aMap.getMapType() == AMap.MAP_TYPE_NORMAL) {
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
		}
	}

	private void SettingsUI() {
		mUiSettings = aMap.getUiSettings();
		mUiSettings.setScaleControlsEnabled(biaoChiValue);
		mUiSettings.setAllGesturesEnabled(shouShiAllValue);
		mUiSettings.setCompassEnabled(zhiNanZhenValue);
		mUiSettings.setRotateGesturesEnabled(shouShiRotateValue);
		mUiSettings.setScrollGesturesEnabled(shouShiScrollValue);
		mUiSettings.setTiltGesturesEnabled(shouShiTiltValue);
		mUiSettings.setZoomGesturesEnabled(shouShiSuoFangValue);
		mUiSettings.setZoomControlsEnabled(suoFangValue);
	}

	/**
	 * 获取从设置中读出的数据
	 */
	private void getSettingsKeys() {
		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		suoFangValue = settings.getBoolean("suo_fang", false);
		biaoChiValue = settings.getBoolean("biao_chi", true);
		zhiNanZhenValue = settings.getBoolean("zhi_nan_zhen", true);
		shouShiAllValue = settings.getBoolean("shou_shi_all", true);
		shouShiRotateValue = settings.getBoolean("shou_shi_rotate", true);
		shouShiScrollValue = settings.getBoolean("shou_shi_scroll", true);
		shouShiTiltValue = settings.getBoolean("shou_shi_tilt", true);
		shouShiSuoFangValue = settings.getBoolean("shou_shi_suo_fang", true);
	}

	private class MyLocation implements AMapLocationListener, LocationSource {
		private OnLocationChangedListener mListener;
		private LocationManagerProxy mAMapLocationManager;

		public void setMapMarker() {
			MyLocationStyle myLocationStyle = new MyLocationStyle();
			// 自定义系统定位小蓝点
			myLocationStyle.myLocationIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
			myLocationStyle.strokeColor(R.color.location_edge_background);// 设置圆形的边框颜色
			myLocationStyle.radiusFillColor(R.color.location_background);// 设置圆形的填充颜色
			// myLocationStyle.anchor(;//设置小蓝点的锚点
			myLocationStyle.strokeWidth(1);// 设置圆形的边框粗细
			aMap.setMyLocationStyle(myLocationStyle);
		}

		public void setUpMap() {
			setMapMarker();
			aMap.setLocationSource(this);// 设置定位监听
			aMap.getUiSettings().setMyLocationButtonEnabled(false);//
			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
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
				dissmissLocationDialog();
			}

		}

		@Override
		public void onLocationChanged(Location location) {

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
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			endPoint = AMapUtil.convertToLatLonPoint(marker.getPosition());
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
						List<SuggestionCity> suggestionCities = poiResult
								.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
						if (poiItems != null && poiItems.size() > 0) {
							PoiOverlay poiOverlay = new PoiOverlay(aMap,
									poiItems);
							poiOverlay.removeFromMap();
							poiOverlay.addToMap();
							poiOverlay.zoomToSpan();
						} else if (suggestionCities != null
								&& suggestionCities.size() > 0) {
							showSuggestCity(suggestionCities);
						} else {
							// ToastUtil.showLong(getActivity(),
							// R.string.no_result);
						}
					}
				} else {
					// ToastUtil.showLong(getActivity(), R.string.no_result);
				}
			} else {
				// ToastUtil.showLong(getActivity(), R.string.error_network);
			}
		}

		/**
		 * poi没有搜索到数据，返回一些推荐城市的信息
		 */
		private void showSuggestCity(List<SuggestionCity> cities) {
			String infomation = "推荐城市\n";
			for (int i = 0; i < cities.size(); i++) {
				infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
						+ cities.get(i).getCityCode() + "城市编码:"
						+ cities.get(i).getAdCode() + "\n";
			}
			ToastUtil.showLong(getActivity(), infomation);

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
				// ToastUtil.showLong(getActivity(), R.string.error_network);
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
				// ToastUtil.showLong(getActivity(), R.string.error_network);
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

		private void removeBuildingMarker() {
			mWHUQ.remove();
			mWHUX.remove();
			mWHUP.remove();
			mWHUY.remove();
			mWHUZ.remove();
			mWHUT.remove();
			mWHUW.remove();
			mWHUL.remove();
			mWHUB.remove();
			mWHUS.remove();
		}

		private void addBuildingMarker() {
			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			MarkerS = 1;
			mWHUQ = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU1)
					.title("宋卿体育馆")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUX = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU5)
					.title("行政楼")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUP = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU6)
					.title("牌楼")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUY = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU7)
					.title("六一纪念亭")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUZ = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU8)
					.title("老斋舍")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUT = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU9)
					.title("老图书馆")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUW = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU10)
					.title("文法学院")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUL = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU11)
					.title("理学院")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUB = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU13)
					.title("半山庐")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
			mWHUS = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHU14)
					.title("十八栋")
					.snippet("点击到那里去")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_view_orange))
					.draggable(true));
		}

		@Override
		public void onInfoWindowClick(Marker marker) {
			if (CURP == null) {
				ToastUtil.showLong(getActivity(), R.string.no_location);
				myLocation.setUpMap();
			}
			startPoint = CURP;
			mySearchRoute = new MySearchRoute();
			mySearchRoute.searchRouteResult(startPoint, endPoint);
			return;
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			if (marker.equals(mWHUQ)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUQ.getPosition());
			} else if (marker.equals(mWHUX)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUX.getPosition());
			} else if (marker.equals(mWHUP)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUP.getPosition());
			} else if (marker.equals(mWHUY)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUY.getPosition());
			} else if (marker.equals(mWHUZ)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUZ.getPosition());
			} else if (marker.equals(mWHUT)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUT.getPosition());
			} else if (marker.equals(mWHUW)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUW.getPosition());
			} else if (marker.equals(mWHUL)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUL.getPosition());
			} else if (marker.equals(mWHUB)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUB.getPosition());
			} else if (marker.equals(mWHUS)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHUS.getPosition());
			}
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

	private class MyMapClick implements OnMapLongClickListener,
			OnMarkerClickListener, OnInfoWindowClickListener,
			InfoWindowAdapter, OnGeocodeSearchListener, OnMapClickListener {

		private void initMyMapClick() {
			aMap.setOnMapLongClickListener(this);
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			geocoderSearch = new GeocodeSearch(getActivity());
			geocoderSearch.setOnGeocodeSearchListener(this);
		}

		@Override
		public void onMapLongClick(LatLng point) {
			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			if (CURP == null) {
				ToastUtil.showLong(getActivity(), R.string.no_location);
			}

			if (mCurPoint != null) {
				mCurPoint.remove();
			}
			// aMap.setOnMapClickListener(this);
			double lat = point.latitude;
			double lon = point.longitude;
			getAddress(new LatLonPoint(lat, lon));
			mpositionLatLng = point;
		}

		//
		// @Override
		// public void onMapClick(LatLng point) {
		// double lat = point.latitude;
		// double lon = point.longitude;
		// getBuilding(new LatLonPoint(lat, lon));
		// aMap.clear();
		// isclick = true;
		// }

		@Override
		public View getInfoContents(Marker marker) {
			View infoContent = getLayoutInflater(getArguments()).inflate(
					R.layout.mymapclick_info_window, null);
			render(marker, infoContent);
			return infoContent;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			View infoWindow = getLayoutInflater(getArguments()).inflate(
					R.layout.mymapclick_info_contents, null);

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

		@Override
		public void onInfoWindowClick(Marker marker) {
			if (CURP == null) {
				ToastUtil.showLong(getActivity(), R.string.no_location);
				myLocation.setUpMap();
			}
			startPoint = CURP;
			endPoint = AMapUtil.convertToLatLonPoint(mCurPoint.getPosition());
			mySearchRoute = new MySearchRoute();
			mySearchRoute.searchRouteResult(startPoint, endPoint);
			return;
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			if (marker.equals(mCurPoint)) {
				endPoint = AMapUtil.convertToLatLonPoint(mCurPoint
						.getPosition());
			}
			return false;
		}

		/**
		 * 响应逆地理编码
		 */
		public void getAddress(LatLonPoint latLonPoint) {
			RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
					GeocodeSearch.AMAP);
			geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
		}

		/**
		 * 逆地理编码回调
		 */
		@Override
		public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
			if (rCode == 0) {
				if (result != null
						&& result.getRegeocodeAddress() != null
						&& result.getRegeocodeAddress().getFormatAddress() != null) {
					addressName = result.getRegeocodeAddress()
							.getFormatAddress();
					mCurPoint = aMap.addMarker(new MarkerOptions()
							.position(mpositionLatLng)
							.title(addressName)
							.snippet("点击到那去~")
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.ic_search)));

				} else {
					ToastUtil.showLong(getActivity(), R.string.no_result);
				}
			} else {
				// ToastUtil.showLong(getActivity(), R.string.error_network);
			}
		}

		@Override
		public void onMapClick(LatLng arg0) {

		}

		@Override
		public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

		}

	}

	private class VolunteerMarker implements OnMarkerClickListener,
			OnInfoWindowClickListener, InfoWindowAdapter {

		private void removeVolunteerMarker() {
			mWHU1.remove();
			mWHU2.remove();
			mWHU3.remove();
			mWHU4.remove();
			mWHU5.remove();
			mWHU6.remove();
			mWHU7.remove();
		}

		private void addVolunteerMarker() {
			aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
			aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
			aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
			String volunteer = getResources().getString(R.string.volunteer);
			String clickAndGo = getResources().getString(R.string.click_and_go);
			MarkerV = 1;
			mWHU1 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV1)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU2 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV2)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU4 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV3)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU5 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV4)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU6 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV5)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU7 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV6)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));
			mWHU3 = aMap.addMarker(new MarkerOptions()
					.position(Constants.WHUV7)
					.title(volunteer)
					.snippet(clickAndGo)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_volunteer_one))
					.draggable(true));

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

		@Override
		public void onInfoWindowClick(Marker marker) {
			if (CURP == null) {
				ToastUtil.showLong(getActivity(), R.string.no_location);
				myLocation.setUpMap();
			}
			startPoint = CURP;
			mySearchRoute = new MySearchRoute();
			mySearchRoute.searchRouteResult(startPoint, endPoint);
			return;
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
			if (marker.isInfoWindowShown()) {
				marker.hideInfoWindow();
			} else {
				marker.showInfoWindow();
			}
			if (marker.equals(mWHU1)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU1.getPosition());
			} else if (marker.equals(mWHU2)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU2.getPosition());
			} else if (marker.equals(mWHU3)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU3.getPosition());
			} else if (marker.equals(mWHU4)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU4.getPosition());
			} else if (marker.equals(mWHU5)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU5.getPosition());
			} else if (marker.equals(mWHU6)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU6.getPosition());
			} else if (marker.equals(mWHU6)) {
				endPoint = AMapUtil.convertToLatLonPoint(mWHU7.getPosition());
			}
			return false;
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
