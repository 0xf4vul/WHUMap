package com.whumap.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whumap.activity.R;

public class WHUHistoryFragment extends Fragment {

	private View view;
	private PullToRefreshListView refreshView;
	private ListView actualListView;
	private LinkedList<Map<String , Object>> listItems;
	private SimpleAdapter adapter;
	
	private int[] imageIds = { R.drawable.ic_launcher,R.drawable.ic_launcher};
	private String[] titles = { "第一幅图", "第二幅图" };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.whu_history_frame, container, false);
		refreshView = (PullToRefreshListView)view.findViewById(R.id.whu_history_list);
		initListView();
		setRefreshListener();
		return view;
	}
	
	private void initListView() {
	
		actualListView = refreshView.getRefreshableView();
		registerForContextMenu(actualListView);
		
		listItems = new LinkedList<Map<String,Object>>();
		for(int i=0 ; i< imageIds.length;i++) {
			Map<String, Object> listItem = new HashMap<String, Object>();
			listItem.put("image" , imageIds[i]);
			listItem.put("title" , titles[i]);
			listItems.add(listItem);
		}
		
		adapter = new SimpleAdapter(getActivity(),listItems,
				R.layout.whu_history_item,
				new String[] { "image" , "title" },
				new int[] { R.id.history_image, R.id.history_text});
		actualListView.setAdapter(adapter);
	}
	
	private void setRefreshListener(){
		
		refreshView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new MyGetDataTask().execute();
			}
			
		});
		
	}
	
	private class MyGetDataTask extends AsyncTask<Void, Integer, Map<String, Object>> {

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			refreshView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
}