package com.whumap.fragment;

import com.whumap.activity.DetailActivity;
import com.whumap.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ScheduleFragment extends Fragment{
	
	private View view;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_list, container, false);
		listView = (ListView)view.findViewById(R.id.schedule_list);
		initScheduleList();
		setListener();
		return view;
	}
	
	/**
	 * 初始化schedule中的内容
	 */
	private void initScheduleList()	 {
		String[] functions = getResources().getStringArray(R.array.function_list);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.left_menu_layout,R.id.left_menu_text,functions);
		listView.setAdapter(adapter);
	}
	
	private void setListener()	 {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				startActivity(intent);
				
			}
		});
	}
}
