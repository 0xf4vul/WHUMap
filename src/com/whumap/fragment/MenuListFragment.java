package com.whumap.fragment;


import com.whumap.activity.ContentFrameActivity;
import com.whumap.activity.R;
import com.whumap.map.MapFragment;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuListFragment extends ListFragment {


@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list, null);
	}
	
	@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
		String[] functions = getResources().getStringArray(R.array.function_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,android.R.id.text1,functions);
		setListAdapter(adapter);
		}
	@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			Fragment fragment = null;
			switch(position) {
			case 0:
				fragment = new MapFragment();
				break;
			case 1:
				fragment = new TextFragment();
				break;
			case 2:
				fragment = new MapFragment();
				break;
			case 3:
				fragment = new TextFragment();
				break;
			case 4:
				fragment = new MapFragment();
				break;
			case 5:
				fragment = new TextFragment();
				break;
				
			}
			if(fragment != null) {
				switchFragment(fragment);
			}
	}

	private void switchFragment(Fragment fragment) {
		
		if(getActivity() == null) {
			return;
		}
		ContentFrameActivity cfa = (ContentFrameActivity) getActivity();
		cfa.switchContent(fragment);
	}
		
}
