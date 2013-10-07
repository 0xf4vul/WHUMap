package com.whumap.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TabHost;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.whumap.fragment.ScheduleDetailOfDayFragment;
import com.whumap.fragment.ScheduleMapFragment;

public class DetailActivity extends SherlockFragmentActivity implements ActionBar.TabListener{

	/** 用来放Frament的ViewPager*/
	private ViewPager viewPager;
	/** 获取当前activity的ActionBar*/
	private ActionBar actionBar;
	/** ViewPager容器*/
	private MyViewPagerAdapter myAdapter;
	/** 从日历中传递来的参数,记录了当前选中活动在listView中位置*/
	private static int schedulePosition;
	private static ScheduleDetailOfDayFragment detailFragment ;
	private static ScheduleMapFragment mapFragment;
	private static int day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_frame);
		init();
		//添加两个Tab
		for (int i=0; i<myAdapter.getCount();i++) {
			ActionBar.Tab tab = actionBar.newTab();
			tab.setText(myAdapter.getPageTitle(i));
			tab.setTabListener(this);
			actionBar.addTab(tab);
		/*	
			actionBar.addTab(actionBar.newTab()
					.setText(myAdapter.getPageTitle(i))
					.setTabListener(this));
					*/
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
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		initSchedulePara();
	}
	/**
	 * 初始化从日历中传递过来的参数title , mapPosition
	 * @author kb
	 *
	 */
	private void initSchedulePara() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("XQDetail");
		schedulePosition = bundle.getInt("position");
		day = bundle.getInt("XiaoQing");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			Bundle args = new Bundle();
			args.putInt("position", schedulePosition);
			args.putInt("XiaoQing", day);
			switch (position){
			case 0:
				if(detailFragment == null) {
					detailFragment = new ScheduleDetailOfDayFragment();
				}
				detailFragment.setArguments(args);
				return detailFragment;
			case 1:
				if(mapFragment == null){
					
					mapFragment = new ScheduleMapFragment();
				}
				mapFragment.setArguments(args);
				return mapFragment;
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
