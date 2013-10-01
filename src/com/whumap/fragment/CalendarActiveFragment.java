package com.whumap.fragment;


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

	/** 日历视图*/
	private CaldroidFragment calendar;
	/** 布局文件*/
	private View view ; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.calendar_frame, container, false);
		initCalendarView();
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
			
			if(lastView == null) {
				lastView = view;
				view.setBackgroundColor(Color.RED);
			} else {
				lastView.setBackgroundColor(Color.WHITE);
				view.setBackgroundColor(Color.RED);
				lastView = view;
			}
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
