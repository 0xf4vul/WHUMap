package com.whumap.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.whumap.activity.R;
import com.whumap.util.ScheduleOfXQ;

public class ScheduleDetailOfDayFragment extends SherlockFragment{

	private final static String[] scheduleFristDayTitles = ScheduleOfXQ.SCHEDULE_OF_FIRST_DAY_XQ;
	private final static String[] scheduleFirstDayTimes = ScheduleOfXQ.DETAIL_TIME_OF_FIRST_DAY_XQ;
	private final static String[] scheduleFirstDayDetails = ScheduleOfXQ.DETAIL_SCHEDULE_OF_FIRST_DAY_XQ; 
	private final static String[] scheduleFirstDayHosts = ScheduleOfXQ.HOST_OF_FIRST_DAY_XQ; 
	private final static String[] scheduleSecondDayTitles = ScheduleOfXQ.SCHEDULE_OF_SECOND_DAY_XQ;
	private final static String[] scheduleSecondDayTimes = ScheduleOfXQ.DETAIL_TIME_OF_SECOND_DAY_XQ;
	private final static String[] scheduleSecondDayDetails = ScheduleOfXQ.DETAIL_SCHEDULE_OF_SECOND_DAY_XQ;
	private final static String[] scheduleSecondDayHosts = ScheduleOfXQ.HOST_OF_SECOND_DAY_XQ;
	private View view;
	/** 活动名称，活动信息，活动责任单位*/
	private TextView scheduleTitle,scheduleMessage,scheduleHost;
	private int position , day;
	private ActionBar actionBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_detail_of_day, container, false);
		init();
		return view;
	}
	private void init() {
		actionBar = getSherlockActivity().getSupportActionBar();
		findViewById();
		getPara();
		initSchedule();
	}
	/**
	 * 获取从ScheduleFragment中传入的参数
	 */
	private void getPara() {
		Bundle args = getArguments();
		position = args.getInt("position");
		day = args.getInt("XiaoQing");
	}
	
	private void findViewById() {
		
	    scheduleTitle = (TextView)view.findViewById(R.id.schedule_title);
		scheduleMessage = (TextView)view.findViewById(R.id.schedule_detail_message);
		scheduleHost = (TextView)view.findViewById(R.id.schedule_detail_host);
	}
	
	private void initSchedule() {
		
		if(day == ScheduleOfXQ.XIAOQINGFIRSTDAY){
			actionBar.setTitle("2013年11月28日"+scheduleFirstDayTimes[position]);
			scheduleTitle.setText(scheduleFristDayTitles[position]);
			scheduleMessage.setText(scheduleFirstDayDetails[position]);
			scheduleHost.setText(scheduleFirstDayHosts[position]);
		} else {
			actionBar.setTitle("2013年11月29日" + scheduleSecondDayTimes[position]);
			scheduleTitle.setText(scheduleSecondDayTitles[position]);
			scheduleMessage.setText(scheduleSecondDayDetails[position]);
			scheduleHost.setText(scheduleSecondDayHosts[position]);
		}
		
	}
}
