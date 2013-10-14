package com.whumap.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.whumap.activity.R;
import com.whumap.activity.WHUZhengWenActivity;
import com.whumap.util.ToastUtil;
import com.whumap.zhengwenutils.GetNewsListService;
import com.whumap.zhengwenutils.News;
import com.whumap.zhengwenutils.NewsListDataBaseService;
import com.whumap.zhengwenutils.ObjectListToHashMap;

public class WHUZhengWenFragment extends Fragment{

	
	private View view;
	private PullToRefreshListView refreshView;
	private ListView actuallListView;
	private List<Map<String , Object>> newsMaps = new ArrayList<Map<String,Object>>();
	private SimpleAdapter listAdapter;
	private int type = 0;
	/** 加载网页的第几页*/
	private int page_load = 1;
	/**在上一次加载征文时记录加载的文章数，当再次刷新第一页时与上一次加载的做判断，如果增加了再把它加进去*/
	private int lastPageSize = 0;
	private boolean firstInit = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.whu_zheng_wen, container,false);
		
		refreshView = (PullToRefreshListView)view.findViewById(R.id.whu_zheng_wen_list);
		refreshView.setMode(Mode.BOTH);
		actuallListView = refreshView.getRefreshableView();
		registerForContextMenu(actuallListView);
//		MyRefreshListViewListener refreshListener = new MyRefreshListViewListener();
//		refreshListener.onPullDownToRefresh(refreshView);
		new MyAsyncTask().execute(0);
		refreshView.demo();
		refreshView.setRefreshing(true);
		initListView();
		setListener();
		return view;
	}
	
	/**
	 * 初始化ListView
	 */
	private void initListView(){
		
		newsMaps.addAll(getNewsListByDatabase(type+""));
		listAdapter = new SimpleAdapter(getActivity(),newsMaps,
				R.layout.whu_zheng_wen_item,
				new String[] {"news_title" , "news_date" } ,
				new int[] { R.id.zheng_wen_title,R.id.zheng_wen_date});
		actuallListView.setAdapter(listAdapter);
		
	}
	
	private void setListener() {
		refreshView.setOnRefreshListener(new MyRefreshListViewListener());
		actuallListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				arg2--;    //多了headView
				Intent intent=new Intent(getActivity(),WHUZhengWenActivity.class);
				intent.putExtra("news_url", newsMaps.get(arg2).get("news_url").toString());
				intent.putExtra("news_title", newsMaps.get(arg2).get("news_title").toString());
				intent.putExtra("news_date", newsMaps.get(arg2).get("news_date").toString());
				startActivity(intent);				
			}
		});
	}
	
	
	/**
	 * 根据type来从数据库中读取已经存在的数据
	 * @param type
	 * @return
	 */
	public List<HashMap<String,Object>> getNewsListByDatabase(String type){
		List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
		List<News> newss = NewsListDataBaseService.getNewsList(getActivity(), type);
		list = ObjectListToHashMap.newsListToHashMapList(newss);
		if(list.size()>0){
			newsMaps.clear();
		}
		return list;
	}
	
	private class MyAsyncTask extends AsyncTask<Integer, String, List<News>> {

		@Override
		protected List<News> doInBackground(Integer... arg0) {
			List<News> list = GetNewsListService.getNewsList(page_load);
			if(page_load==1 && list.size()>0 ) {
				NewsListDataBaseService.addNewsList(list, getActivity(),arg0[0]+"");
			}
			return list;
		}
		
		@Override
		protected void onPostExecute(List<News> list) {
			if(list.size()<1){
				ToastUtil.showLong(getActivity(), R.string.contribute);
			}else{
				if(page_load == 1 && lastPageSize == list.size()) {
					ToastUtil.showLong(getActivity(), R.string.contribute);
				}
				if(page_load==1){
					newsMaps.clear();
				}
				newsMaps.addAll(ObjectListToHashMap.newsListToHashMapList(list));
				listAdapter.notifyDataSetChanged();
				lastPageSize = list.size();
			}
			refreshView.onRefreshComplete();
		}
	}
	
	
	private class MyRefreshListViewListener implements PullToRefreshBase.OnRefreshListener2<ListView> {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			String label = DateUtils.formatDateTime(getActivity(),
					System.currentTimeMillis(), 
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			// Update the LastUpdatedLabel
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			page_load = 1;
			new MyAsyncTask().execute(type);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			String label = DateUtils.formatDateTime(getActivity(),
					System.currentTimeMillis(), 
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
			// Update the LastUpdatedLabel
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			page_load++;
			new MyAsyncTask().execute(type);
		}
		
	}
}
