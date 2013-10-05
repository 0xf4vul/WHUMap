package com.whumap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.whumap.activity.R;

public class ScheduleEmptyFragment extends Fragment{

	private View view; 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_empty, container , false);
		return view;
	}
}
