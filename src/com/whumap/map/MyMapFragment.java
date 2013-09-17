package com.whumap.map;


import com.whumap.activity.R;
import com.whumap.circlebutton.CircleButton;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class MyMapFragment extends Fragment{

	private String title;
	private final int BASIC_CHILD_BUTTON_ID = 1000;
	private  CircleButton circleButton ;
	private int[] imgResId = {
			R.drawable.composer_camera,
			R.drawable.composer_music,
			R.drawable.composer_place,
			R.drawable.composer_sleep,
			R.drawable.composer_sun,
			R.drawable.composer_thought
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.map_fragment, container , false);
		initCircleButton(v);
		return v;
	}
	

	/**
	 * 初始化菜单按钮
	 * 
	 */
	public void initCircleButton(View v) {
		
	    circleButton = (CircleButton)v.findViewById(R.id.test);
		circleButton.init(imgResId , R.drawable.composer_button
				,R.drawable.composer_icn_plus,CircleButton.RIGHTCENTER,180,300) ;
		
		/*
		 * 为菜单的子按钮添加点击监听,将地图的每个功能写在对应的按钮Id中
		 */
		OnClickListener onClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(getId() == BASIC_CHILD_BUTTON_ID +0) {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +1) {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +2) {
				
				} else if(getId() == BASIC_CHILD_BUTTON_ID +3) {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +4)  {
					
				} else if(getId() == BASIC_CHILD_BUTTON_ID +5) {
					}
				}
		};
		
		circleButton.setOnClickListener(onClick);
		
		RelativeLayout ll = (RelativeLayout) v.findViewById(R.id.map_container);
		ll.setOnClickListener(new OnClickListener() { 
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(circleButton.getButtonState())
				circleButton.collapse();
			}
		});
		
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setRetainInstance(true);
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("fragment", title);
	}
	
}
