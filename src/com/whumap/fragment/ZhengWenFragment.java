package com.whumap.fragment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whumap.activity.ArticleActivity;
import com.whumap.activity.R;
import com.whumap.util.ToastUtil;

public class ZhengWenFragment extends Fragment{

	private View view; 
	private PullToRefreshListView pullToRefreshListView;
	private ListView actualListView;
	private SimpleAdapter adapter;
	private LinkedList<Map<String , Object>> listItems;
	private int articleId;
	private String[] articleTitles = null;
	private String[] articleAuthors = null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.zheng_wen_frame, container, false);
		pullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.zheng_wen_list);
		initArticleData();
		initListView();
		setRefreshListener();
		return view; 
	}	
	
	private void initArticleData(){
		
		articleTitles = getActivity().getResources().getStringArray(R.array.article_titles); 
		articleAuthors = getActivity().getResources().getStringArray(R.array.article_authors);
	}
	/**
	 * 设置ListView
	 */
	private void initListView() {
		actualListView = pullToRefreshListView.getRefreshableView();
		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		pullToRefreshListView.setMode(Mode.BOTH);
		
	    listItems = new LinkedList<Map<String, Object>>();
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
				articleId = arg2-1;
				Bundle id = new Bundle();
				id.putInt("name", articleId);
				Intent intent = new Intent(getActivity(), ArticleActivity.class);
				intent.putExtras(id);
				startActivity(intent);
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
				new MyGetDataTask().execute();
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

	private class MyGetDataTask extends AsyncTask<Void, Integer, Map<String, Object>> {

		@Override
		protected Map<String, Object> doInBackground(Void... params) {
			//Map<String, Object> item = new HashMap<String, Object>();
			try {
			//	item.put("title", "新添加的");
			//	item.put("author", "暂无");
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Map<String, Object> result) {
		//	listItems.addFirst(result);
		//	adapter.notifyDataSetChanged();
			ToastUtil.showLong(getActivity(), R.string.contribute);
			pullToRefreshListView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
			
	
	private class MyAdapter extends BaseAdapter{

		private Context mContext;
		private List<? extends Map<String , ?>> data;
		private int resource;
		private String[] from;
		private int[] to;
		public MyAdapter(Context context,List<? extends Map<String, ?>> data,
				int resource,String[] from, int[] to){
			mContext = context;
			this.data = data;
			this.resource = resource;
			this.from = from;
			this.to = to;
		}
		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return null;
		}
		
	}
}
