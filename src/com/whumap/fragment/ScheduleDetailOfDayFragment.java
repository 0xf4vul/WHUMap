package com.whumap.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whumap.activity.DetailActivity;
import com.whumap.activity.R;

public class ScheduleDetailOfDayFragment extends Fragment{

	private View view;
	private TextView text;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_detail_of_day, container, false);
		text = (TextView)view.findViewById(R.id.text);
		Bundle args = getArguments();
		String title = args.getString("title");
		text.setText(title);
		return view;
	}
}
