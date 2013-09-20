package com.whumap.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.whumap.fragment.MenuListFragment;
import com.whumap.fragment.TextFragment;
import com.whumap.map.MyMapFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ContentFrameActivity extends BaseActivity{

	public ContentFrameActivity() {
		super(R.string.app_name);
	}

	private Fragment currentFragment ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Demo");
	/*	if(savedInstanceState != null) {
			currentFragment = getFragmentManager().getFragment(savedInstanceState, "fragment");
		}
		if(currentFragment == null) {
			currentFragment = new MyMapFragment();
		}*/
		currentFragment = new MyMapFragment();
		setContentView(R.layout.content_frame);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, currentFragment);
		ft.commit();
		setBehindContentView(R.layout.menu_frame);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuListFragment()).commit();
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}
	
	public void switchContent(Fragment fragment) {
		if(currentFragment != fragment) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			if(!fragment.isAdded()) {
				ft.hide(currentFragment).add(R.id.content_frame, fragment).commit();
			} else  {
				ft.hide(currentFragment).show(fragment).commit();
			}
			currentFragment = fragment;
		}
		/*
		ft.add(R.id.content_frame, fragment);
		ft.commit(); */
		getSlidingMenu().showContent();
	}
}
