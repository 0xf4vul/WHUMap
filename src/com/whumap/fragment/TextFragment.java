package com.whumap.fragment;

import com.amap.api.mapcore.v;
import com.whumap.activity.ArticleActivity;
import com.whumap.activity.R;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TextFragment extends Fragment {
	private static final String[] strs = new String[] { "别了，珞珈山"
	,"情意交融"	,"共同的根和魂","怀念朱景尧先生","肯教 能教 善教","动人的故事","筑梦","登山览胜","晚来者语"
	,"学府之美","新梦","书卷多情似故人","最美的年华","永是珞珈一少年","湖光山色两相宜"
	};
	private int ArticleId; 
	ListView lv;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.text, container, false);
		lv = (ListView) v.findViewById(R.id.lv);
		lv.setAdapter(new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, strs));

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				ArticleId = arg2;
				Bundle id = new Bundle();
				id.putInt("name", ArticleId);
				Intent intent = new Intent(getActivity(), ArticleActivity.class);
				intent.putExtras(id);
				startActivity(intent);
			}
		});
		return v;
	}
}