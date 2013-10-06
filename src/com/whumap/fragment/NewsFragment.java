package com.whumap.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whumap.activity.R;

public class NewsFragment extends Fragment{

	private View view; 
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private SimpleAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.news_frame, container, false);
		pullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.pull_refresh_list);
		initListView();
		setRefreshListener();
		return view; 
	}	
	
	/**
	 * 设置ListView
	 */
	private void initListView() {
		actualListView = pullToRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		
		List<Map<String , Object>> listItems = new ArrayList<Map<String, Object>>();
		for(int i=0; i<articleTitles.length ; i++ ) {
			Map<String , Object> listItem = new HashMap<String, Object>();
			listItem.put("title" , articleTitles[i]);
			listItem.put("author", articleAuthors[i]);
			listItems.add(listItem);
		}
		
		adapter = new SimpleAdapter(getActivity(), listItems,
				R.layout.schedule_xq_frame, 
				new String[] { "title" , "author"}, 
				new int[] { R.id.schedule_title , R.id.schedule_time});
		actualListView.setAdapter(adapter);
		
		actualListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getActivity(), arg2+"", Toast.LENGTH_LONG).show();
			}
		});
	}
	
	/**
	 * 为pullToRefresh添加事件监听
	 * @author kb
	 *
	 */
	private void setRefreshListener() {
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), 
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// Do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		
		
		// Add an end-of-list listener
		pullToRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "End of List!", Toast.LENGTH_SHORT).show();
			}
		});

	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return articleAuthors;
		}

		@Override
		protected void onPostExecute(String[] result) {
			adapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			pullToRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}


	
	private String[] articleTitles = { "别了，珞珈山"  ,"情意交融",
			"共同的根和魂","怀念朱景尧先生","肯教 能教 善教",
			"动人的故事","筑梦","登山览胜","晚来者语"
			,"学府之美","新梦","书卷多情似故人","最美的年华",
			"永是珞珈一少年","湖光山色两相宜" };	
	private String[] articleAuthors = { "胡珊" , "黄庭瑞" , "郭灵彦" ,"徐业勤",
			"王洪英","王廷恺","王廷恺","刘代高","杨凤芹",
			"贺芳琼","袁晓蒙","李清华","王伶鑫",
			"何五元","文铭",};
}
