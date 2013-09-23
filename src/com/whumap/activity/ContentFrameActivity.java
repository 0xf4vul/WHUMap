package com.whumap.activity;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.whumap.fragment.NewsFragment;
import com.whumap.fragment.TextFragment;
import com.whumap.map.MyMapFragment;
import com.whumap.util.CountDownDate;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
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
	/** 文字fragment*/
	private TextFragment textFragment ;
	/** 新闻fragment*/
	private NewsFragment newsFragment;
	/** 当前显示的fragment*/
	private Fragment currentFragment ;
	/** 倒计时日期*/
	private TextView countDownDate;
	/** 左侧菜单的父容器，用来修改倒计时按钮*/
	private LinearLayout ll;
	/** 倒计时容器*/
	private RelativeLayout rl;
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
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, myMapFragment);
		currentFragment = myMapFragment;
		ft.commit();
	}
	
	/**
	 * 初始化左侧菜单列表
	 */
	private void initLeftMenu()	 {
		
		String[] functions = getResources().getStringArray(R.array.function_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,android.R.id.text1,functions);
		leftMenu.setAdapter(adapter);
	}
	
	/**
	 * 初始化slidingmenu
	 */
	private void initSlidingMenu() {
		
		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
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
				
				switch(arg2) {
				case 0:
					if(myMapFragment == null) {
						myMapFragment = new MyMapFragment();
					}
					switchContent(myMapFragment);
					break;
				case 1:
					if(textFragment == null) {
						textFragment = new TextFragment();
					}
					switchContent(textFragment);
					break;
				case 2:
					if(newsFragment == null) {
						newsFragment = new NewsFragment();
					}
					switchContent(newsFragment);
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
			FragmentTransaction ft = getFragmentManager().beginTransaction();
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
}
