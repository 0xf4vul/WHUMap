package com.whumap.activity;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.whumap.fragment.CalendarActiveFragment;
import com.whumap.fragment.WHUHistoryFragment;
import com.whumap.fragment.WHUZhengWenFragment;
import com.whumap.map.MyMapFragment;
import com.whumap.util.CountDownDate;
import com.whumap.util.ToastUtil;

public class ContentFrameActivity extends SlidingFragmentActivity{

	private final static int SYSTEM_SETTINGS = 0;
	private SlidingMenu slidingMenu;
	private ListView leftMenu;
	private MyMapFragment myMapFragment ; 
	private Fragment currentFragment ;
	private TextView countDownDate;
	private LinearLayout ll;
	private RelativeLayout rl;
	private CalendarActiveFragment calendarFragment;
	private WHUHistoryFragment wHUHistoryFragment;
	private WHUZhengWenFragment whuZhengWenFragment;
	private List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
	/**功能导航名称*/
	private String[] functions;
	/** 用来存储从SharePreference中取得的值，当每次切换到MyMapFragment时将该参数传入MyMapFragment用来出初始化地图的基本设置*/
	private int[] imageIds = { R.drawable.ic_menu_map,R.drawable.ic_menu_calendar,
				R.drawable.ic_menu_contribute,R.drawable.ic_menu_view};
	
	private Bundle data = new Bundle();
	/** 设置界面中各种键通过SharePreference取得的值，获取后传入MyMapFragment*/
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBehindContentView(R.layout.menu_frame);
		setContentView(R.layout.content_frame);
		init();
		setListener();
	}
	
	/**
	 * 所有初始化内容，包括初始化slidingMenu,
	 * 初始化显示内容，以及初始化控件
	 */
	private void init() {
		
		initContent();
		initSlidingMenu();
		findViewById();
		initLeftMenu();
		initCountDownDay();
	}
	
	/**
	 * 初始化所需的所有控件
	 */
	private void findViewById() {
		leftMenu = (ListView)findViewById(R.id.menulist);
		countDownDate = (TextView)findViewById(R.id.count_down_days);
		ll = (LinearLayout)findViewById(R.id.menu_frame);
		rl = (RelativeLayout)findViewById(R.id.count_down_container);
	}
	
	/**
	 * 初始化刚进入时显示的内容
	 */
	private void initContent()  {
		
		myMapFragment = new MyMapFragment();
		myMapFragment.setArguments(data);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, myMapFragment);
		currentFragment = myMapFragment;
		ft.commit();
	}
	
	/**
	 * 初始化左侧菜单列表
	 */
	private void initLeftMenu()	 {
		
		for(int i=0;i<4;i++) {
			Map<String,Object> listItem = new HashMap<String, Object>();
			listItem.put("image", imageIds[i]);
			listItem.put("title", functions[i]);
			listItems.add(listItem);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.left_menu_layout,
				new String[] { "image" , "title" },
				new int[] {R.id.left_menu_image,R.id.left_menu_text});
		leftMenu.setAdapter(adapter);
	}
	
	/**
	 * 初始化slidingMenu
	 */
	private void initSlidingMenu() {
		
		functions = getResources().getStringArray(R.array.function_list);
		setTitle(functions[0]);
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	
	/**
	 * 计算倒计时日并显示
	 */
	private void initCountDownDay() {
		
		CountDownDate date = new CountDownDate(2013, 11, 29);
		int days = date.countDownDate();
		if(days != 0) {
			
			countDownDate.setText(days+"");
		} else {
		
			ll.removeView(rl);
		}
		
	}
	
	/**
	 * 设置监听
	 */
	private void setListener() {
		
		leftMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				flag = 0;
				setTitle(functions[arg2]);
				switch(arg2) {
				case 0:
					if(myMapFragment == null) {
						myMapFragment = new MyMapFragment();
					}
					mainMenu.removeItem(R.id.contribute);
					switchContent(myMapFragment);
					break;
				case 1:
					if(calendarFragment == null) {
						calendarFragment = new CalendarActiveFragment();
					}
					mainMenu.removeItem(R.id.contribute);
					switchContent(calendarFragment);
					break;
				case 2:
					if(whuZhengWenFragment == null) {
						whuZhengWenFragment = new WHUZhengWenFragment();
					}
					initMenu(mainMenu, R.menu.contribute_menu);
					switchContent(whuZhengWenFragment);
					break;
				case 3:
					if(wHUHistoryFragment == null) {
						wHUHistoryFragment = new WHUHistoryFragment();
					}
					mainMenu.removeItem(R.id.contribute);
					switchContent(wHUHistoryFragment);
					break;
				default :
						break;
				}
			}
			
		});
	}
	/**
	 * 切换fragment
	 * @param fragment
	 */
	public void switchContent(Fragment fragment) {
		if(currentFragment != fragment) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			if(!fragment.isAdded()) {
				ft.hide(currentFragment).add(R.id.content_frame, fragment).commit();
			} else  {
				ft.hide(currentFragment).show(fragment).commit();
			}
			currentFragment = fragment;
			data.clear();
		}
		getSlidingMenu().showContent();
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()) {
		case  android.R.id.home :
			toggle();
			return true;
		case R.id.about:
			break;
		case R.id.feedback:
			final Intent myEmail = new Intent(android.content.Intent.ACTION_SENDTO);
			String uriText = "mailto:wangtongkb24@gmail.com" +
					"?subject=" + URLEncoder.encode("意见反馈"); 
			myEmail.setData(Uri.parse(uriText));
			try {
				startActivity(myEmail);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_send_email);
			}
			break;
		case R.id.settings:
			Intent intent = new Intent(ContentFrameActivity.this,SettingsActivity.class);
			startActivityForResult(intent, SYSTEM_SETTINGS);
			break;
		case R.id.contribute:
			final Intent toEmail = new Intent(android.content.Intent.ACTION_SENDTO);
			String uriText2 = "mailto:wdxw@whu.edu.cn" +
					"?subject="+URLEncoder.encode("征文");
			toEmail.setData(Uri.parse(uriText2));
			try {
				startActivity(toEmail);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_send_email);
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private Menu mainMenu;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu = menu;
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	private void initMenu(Menu menu,int resource){
		
		getSupportMenuInflater().inflate(resource, menu);
		
	}
	/**用来记录返回按钮被按下的次数*/
	private int flag =0;
	@Override
	public void onBackPressed() {
		flag++;
		if(flag < 2) {
			
			slidingMenu.showMenu();
		} else {
			super.onBackPressed();
			flag = 0;
		}
	}
}
