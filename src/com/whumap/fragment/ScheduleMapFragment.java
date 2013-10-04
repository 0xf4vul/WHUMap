package com.whumap.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whumap.activity.R;

public class ScheduleMapFragment extends Fragment{
	
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_map, container , false);
		Bundle arg = getArguments();
		String mapPosition = arg.getString("mapPosition");
		TextView text = new TextView(getActivity());
		view.setBackgroundColor(Color.RED);
		return view;
	}

}
