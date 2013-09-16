package com.whumap.activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.whumap.fragment.MenuListFragment;
import com.whumap.map.MapFragment;

import android.app.Fragment;
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
		if(savedInstanceState != null) {
			currentFragment = getFragmentManager().getFragment(savedInstanceState, "fragment");
		}
		if(currentFragment == null) {
			currentFragment = new MapFragment();
		}
		setContentView(R.layout.content_frame);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, currentFragment)
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
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
}
