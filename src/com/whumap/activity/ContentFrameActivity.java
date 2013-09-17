package com.whumap.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.whumap.fragment.MenuListFragment;
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
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, currentFragment)
		.commit();
		
		setBehindContentView(R.layout.menu_frame);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new MenuListFragment()).commit();
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, "fragment", currentFragment);
	}
	
	public void switchContent(Fragment fragment) {
		currentFragment = fragment;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		ft.addToBackStack(null);
		ft.commit();
		getSlidingMenu().showContent();
	}
}
