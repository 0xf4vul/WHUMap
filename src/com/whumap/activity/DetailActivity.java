package com.whumap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.whumap.fragment.ScheduleDetailOfDayFragment;
import com.whumap.fragment.ScheduleMapFragment;

public class DetailActivity extends SherlockFragmentActivity implements ActionBar.TabListener{

	/** 用来放Frament的ViewPager*/
	private ViewPager viewPager;
	/** 获取当前activity的ActionBar*/
	private ActionBar actionBar;
	/** ViewPager容器*/
	private MyViewPagerAdapter myAdapter;
	/** 从日历中传递来的参数title,将其作为参数再传给ScheduleDetailOfDayFragment,用来明确选中活动的详细信息*/
	private static String title;
	/** 从日历中传递来的参数mapPosition,将其作为参数再传给ScheduleMapFragment,用来确定地图定位的位置*/
	private static String mapPosition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_frame);
		init();
		//添加两个Tab
		for (int i=0; i<myAdapter.getCount();i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(myAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		viewPager.setAdapter(myAdapter);
		viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}	
		});
	}
	
	/**
	 * 初始化基本视图
	 */
	private void init() {
		actionBar = getSupportActionBar();
		viewPager = (ViewPager)findViewById(R.id.pager);
		myAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
		
		//设置ActionBar使用Tab导航
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		initSchedulePara();
	}
	/**
	 * 初始化从日历中传递过来的参数title , mapPosition
	 * @author kb
	 *
	 */
	private void initSchedulePara() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("message");
		title = bundle.getString("title");
		mapPosition = bundle.getString("mapPosition");
	}
	
	/**
	 * 自定义一个ViewPager的adapter
	 * @author kb
	 *
	 */
	private static class MyViewPagerAdapter extends FragmentPagerAdapter {

		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position){
			case 0:
				Fragment fragment1 = new ScheduleDetailOfDayFragment();
				Bundle args = new Bundle();
				args.putString("title", title);
				fragment1.setArguments(args);
				return fragment1;
			case 1:
				Fragment fragment = new ScheduleMapFragment();
				Bundle arg = new Bundle();
				arg.putString("mapPosition", mapPosition);
				fragment.setArguments(arg);
				return fragment;
			}
			
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch(position){
			case 0:
				return "活动";
			case 1:
				return "地图";
			}
			return null;
		}
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}
}
