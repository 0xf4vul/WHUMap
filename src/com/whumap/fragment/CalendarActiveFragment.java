package com.whumap.fragment;


import java.util.Calendar;
import java.util.Date;

import com.whumap.activity.R;
import com.whumap.calendar.CaldroidFragment;
import com.whumap.calendar.CaldroidListener;
import com.whumap.util.ScheduleOfXQ;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CalendarActiveFragment extends Fragment{

	/** 日历视图*/
	private CaldroidFragment calendarFragment;
	/** 布局文件*/
	private View view ; 
	/** 记录当前时间*/
	private Calendar currentDate ;
	/** 用来加载当前选中日期的schedule*/
	private Fragment fragment;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.calendar_frame, container, false);
		currentDate = Calendar.getInstance();
		initcalendarFragmentView();
		initScheduleContainer(currentDate);
		return view; 
	}

	/**
	 * 将日历视图添加到fragment中，并且初始化
	 */
	private void initcalendarFragmentView() {
		
		calendarFragment = new CaldroidFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.calendar_frame , calendarFragment);
		ft.commit();
		setListener();
	}
	
	/**
	 * 初始化schedule容器中的日程
	 */
	private void initScheduleContainer(Calendar cal) {
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		boolean flag = isXiaoQing(year,month,day);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Bundle args = new Bundle();
		if(flag == true) {
			args.putInt("XiaoQing", day);
			fragment = new ScheduleFragment();
			fragment.setArguments(args);
		} else {
			fragment = new ScheduleEmptyFragment();
		}
		ft.replace(R.id.schedule_container , fragment);
		ft.commit();
	}
	
	
	/**
	 * 对当前日期与校庆日期对比，如果在校庆那两天则返回真，否则返回假
	 */
	private boolean isXiaoQing(int year, int month, int day) {
		
		return year==ScheduleOfXQ.XIAOQINGYEAR&&month==ScheduleOfXQ.XIAOQINGMONTH&&day==ScheduleOfXQ.XIAOQINGFIRSTDAY 
				|| year==ScheduleOfXQ.XIAOQINGYEAR&&month==ScheduleOfXQ.XIAOQINGMONTH&&day==ScheduleOfXQ.XIAOQINGSECONDDAY;
	}
	
	
	/**
	 * 添加日历视图的监听事件
	 */
	private void setListener() {
		
		calendarFragment.setCaldroidListener(new MycalendarFragmentListener());
			
	}
	
	
	/** 记录上次选中的时间点的View*/
	private Date lastDate = null;
	/**
	 * 设置calendarFragment的监听事件
	 * @author kb
	 *
	 */
	private class MycalendarFragmentListener extends CaldroidListener {

		@Override
		public void onSelectDate(Date date, View view) {
			
			if(lastDate == null) {
				lastDate = date;
				calendarFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
				calendarFragment.refreshView();
			} else {
				calendarFragment.setBackgroundResourceForDate(R.color.caldroid_white, lastDate);
				calendarFragment.setBackgroundResourceForDate(R.color.caldroid_holo_blue_dark, date);
				calendarFragment.refreshView();
				lastDate = date;
			} 
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			initScheduleContainer(cal); 
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
		
		
	}
	
}
