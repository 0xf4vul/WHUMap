package com.whumap.fragment;


import java.util.Calendar;
import java.util.Date;

import com.whumap.activity.R;
import com.whumap.calendar.CaldroidFragment;
import com.whumap.calendar.CaldroidListener;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarActiveFragment extends Fragment{

	/**标记校庆日期*/
	private final int XQDAYONE = 28;
	private final int XQDAYTWO = 29;
	private final int XQMONTH = 10;
	private final int XQYEAR = 2013;
	/** 日历视图*/
	private CaldroidFragment calendar;
	/** 布局文件*/
	private View view ; 
	/** 记录当前时间*/
	private Calendar currentDate ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.calendar_frame, container, false);
		currentDate = Calendar.getInstance();
		initCalendarView();
		initScheduleContainer(isXiaoQing(currentDate));
		return view; 
	}

	/**
	 * 将日历视图添加到fragment中，并且初始化
	 */
	private void initCalendarView() {
		
		calendar = new CaldroidFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.calendar_frame , calendar);
		ft.commit();
		setListener();
	}
	
	/**
	 * 初始化schedule容器中的日程
	 */
	private void initScheduleContainer(boolean flag) {
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(flag == true) {
			
			ft.replace(R.id.schedule_container, new ScheduleFragment());
		} else {
			ft.replace(R.id.schedule_container , new ScheduleEmptyFragment());
		}
		ft.commit();
	}
	
	/**
	 * 对当前日期与校庆日期对比，如果在校庆那两天则返回真，否则返回假
	 */
	private boolean isXiaoQing(Calendar cal) {
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		return year==XQYEAR&&month==XQMONTH&&day==XQDAYONE || year==XQYEAR&&month==XQMONTH&&day==XQDAYTWO ;
	}
	
	
	/**
	 * 添加日历视图的监听事件
	 */
	private void setListener() {
		
		calendar.setCaldroidListener(new MyCalendarListener());
			
	}
	
	
	/** 记录上次选中的时间点*/
	private View lastView = null;
	/**
	 * 设置calendar的监听事件
	 * @author kb
	 *
	 */
	private class MyCalendarListener extends CaldroidListener {

		@Override
		public void onSelectDate(Date date, View view) {
			selectDate(view);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			initScheduleContainer(isXiaoQing(cal));
			
		}

		@Override
		public void onLongClickDate(Date date, View view) {
			super.onLongClickDate(date, view);
		}

		@Override
		public void onChangeMonth(int month, int year) {
			super.onChangeMonth(month, year);
		}

		@Override
		public void onCaldroidViewCreated() {
			super.onCaldroidViewCreated();
		}
		
		/**
		 * 当点击一个新的日期时，取消上次点击留下的效果
		 */
		private void selectDate(View view) {
			
			if(lastView == null) {
				lastView = view;
				view.setBackgroundColor(Color.RED);
			} else {
				lastView.setBackgroundColor(Color.WHITE);
				view.setBackgroundColor(Color.RED);
				lastView = view;
			}
		}
		
	}
	
}
