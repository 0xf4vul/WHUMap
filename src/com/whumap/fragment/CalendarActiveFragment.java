package com.whumap.fragment;


import java.util.Date;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.whumap.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

	private void initCalendarView() {
		
		calendar = new CaldroidFragment();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.calendar_frame , calendar);
		ft.commit();
		setListener();
	}

	private void setListener() {
		
		calendar.setCaldroidListener(new MyCalendarListener());
			
	}
	
	private class MyCalendarListener extends CaldroidListener {

		@Override
		public void onSelectDate(Date date, View view) {
			
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
