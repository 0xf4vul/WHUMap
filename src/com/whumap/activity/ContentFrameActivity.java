package com.whumap.activity;


import java.net.URLEncoder;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.whumap.fragment.CalendarActiveFragment;
import com.whumap.fragment.ZhengWenFragment;
import com.whumap.fragment.WHUHistoryFragment;
import com.whumap.map.MyMapFragment;
import com.whumap.util.CountDownDate;
import com.whumap.util.ToastUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentFrameActivity extends SlidingFragmentActivity{

	/** slidingMenu */
	private SlidingMenu slidingMenu;
	/** slidingMenu左侧菜单 */
	private ListView leftMenu;
	/** 地图fragment*/
	private MyMapFragment myMapFragment ; 
	/** 新闻fragment*/
	private ZhengWenFragment zhengWenFragment;
	/** 当前显示的fragment*/
	private Fragment currentFragment ;
	/** 倒计时日期*/
	private TextView countDownDate;
	/** 左侧菜单的父容器，用来修改倒计时按钮*/
	private LinearLayout ll;
	/** 倒计时容器*/
	private RelativeLayout rl;
	/** 日历活动视图*/
	private CalendarActiveFragment calendarFragment;
	/** 征文*/
	private WHUHistoryFragment wHUHistoryFragment;
	/**功能导航*/
	private String[] functions;
	
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
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, myMapFragment);
		currentFragment = myMapFragment;
		ft.commit();
	}
	
	/**
	 * 初始化左侧菜单列表
	 */
	private void initLeftMenu()	 {
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.left_menu_layout,R.id.left_menu_text,functions);
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
					if(zhengWenFragment == null) {
						zhengWenFragment = new ZhengWenFragment();
					}
					initMenu(mainMenu, R.menu.contribute_menu);
					switchContent(zhengWenFragment);
					break;
				case 3:
					if(wHUHistoryFragment == null) {
						wHUHistoryFragment = new WHUHistoryFragment();
					}
					mainMenu.removeItem(R.id.contribute);
					mainMenu.removeItem(R.id.contribute);
					switchContent(wHUHistoryFragment);
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
