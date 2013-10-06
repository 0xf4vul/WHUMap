package com.whumap.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.whumap.activity.DetailActivity;
import com.whumap.activity.R;
import com.whumap.util.ScheduleOfXQ;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ScheduleFragment extends Fragment{
	
	private View view;
	private ListView listView;
	/** 第一天活动名称*/
	private final static String[] scheduleFristDayTitles = ScheduleOfXQ.SCHEDULE_OF_FIRST_DAY_XQ;
	/** 第一天活动时间*/
	private final static String[] scheduleFirstDayTimes = ScheduleOfXQ.DETAIL_TIME_OF_FIRST_DAY_XQ;
	/** 第二天活动名称*/
	private final static String[] scheduleSecondDayTitles = ScheduleOfXQ.SCHEDULE_OF_SECOND_DAY_XQ;
	/** 第二天活动时间*/
	private final static String[] scheduleSecondDayTimes = ScheduleOfXQ.DETAIL_TIME_OF_SECOND_DAY_XQ;
	/** 读取选中的校庆日期,28代表第一天，29代表第二天*/
	private int day;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_list, container, false);
		listView = (ListView)view.findViewById(R.id.schedule_list);
		Bundle args = getArguments();
		day = args.getInt("XiaoQing");
		initScheduleList();
		setListener();
		return view;
	}
	
	/**
	 * 初始化第一天schedule中的内容
	 */
	private void initScheduleList()	 {

		List<Map<String, Object>> firstDayListItems = new ArrayList<Map<String,Object>>();
		for(int i=0; i<scheduleFristDayTitles.length;i++){
			Map<String, Object> firstDayListItem = new HashMap<String, Object>();	
			firstDayListItem.put("title", scheduleFristDayTitles[i]);
			firstDayListItem.put("time", scheduleFirstDayTimes[i]);
			firstDayListItems.add(firstDayListItem);
		}
		
		List<Map<String, Object>> secondDayListItems = new ArrayList<Map<String,Object>>();
		for(int i=0; i<scheduleSecondDayTitles.length;i++){
			Map<String, Object> secondDayListItem = new HashMap<String, Object>();	
			secondDayListItem.put("title", scheduleSecondDayTitles[i]);
			secondDayListItem.put("time", scheduleSecondDayTimes[i]);
			secondDayListItems.add(secondDayListItem);
		}
		
		SimpleAdapter firstDayAdapter = new SimpleAdapter(getActivity(),
					firstDayListItems, R.layout.schedule_xq_frame,
					new String[] { "title" , "time" } ,
					new int[] { R.id.schedule_title, R.id.schedule_time });
		SimpleAdapter secondDayAdapter = new SimpleAdapter(getActivity(),
				secondDayListItems, R.layout.schedule_xq_frame,
				new String[] { "title" , "time" } ,
				new int[] { R.id.schedule_title, R.id.schedule_time });
		
		if( day == ScheduleOfXQ.XIAOQINGFIRSTDAY) {
			listView.setAdapter(firstDayAdapter);
		} else {
			listView.setAdapter(secondDayAdapter);
		}
	}
	
	/**
	 * 为listView添加监听，并且传入相应的参数
	 */
	private void setListener()	 {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", arg2);
				bundle.putInt("XiaoQing", day);
				intent.putExtra("XQDetail", bundle);
				startActivity(intent);
				
			}
		});
	}
}
