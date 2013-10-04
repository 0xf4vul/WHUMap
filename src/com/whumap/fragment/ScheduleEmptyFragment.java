package com.whumap.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.whumap.activity.DetailActivity;

import com.whumap.activity.R;

public class ScheduleEmptyFragment extends Fragment{

	private View view; 
	private Button button;	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.schedule_empty, container , false);
		button = (Button)view.findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("title", "珞珈山");
				bundle.putString("mapPosition","珞珈山");
				intent.putExtra("message", bundle);
				startActivity(intent);
			}
		});
		return view;
	}
}
