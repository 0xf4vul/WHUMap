package com.whumap.activity;

import java.util.ArrayList;
import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.whumap.util.ToastUtil;

import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class SearchFrameActivity extends SherlockActivity implements TextWatcher,
	OnClickListener
	 {
	private EditText searchPosition; // 输入的是要查找的内容
	private EditText searchMyPosition; // 输入的是起始位置，默认为我的位置
	private EditText searchDePosition; // 输入的是终点位置
	private Button searchButton; // 搜位置
	private Button searchRoute; // 查找路线
	private String keyWord = "";// poi搜索关键字
	private String strStart = "";
	private String strEnd = "";
	private ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		findViewById(); // 初始化搜索界面中的所有按钮
		searchRoute.setOnClickListener(this);
		searchButton.setOnClickListener(this);
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void findViewById() {
		searchPosition = (AutoCompleteTextView) findViewById(R.id.search_position_content);
		searchPosition.addTextChangedListener(this);// 添加文本输入框监听事件
		searchMyPosition = (AutoCompleteTextView) findViewById(R.id.search_route_my_position);
		searchMyPosition.addTextChangedListener(this);// 添加文本输入框监听事件
		searchDePosition = (AutoCompleteTextView) findViewById(R.id.search_route_destination_position);
		searchDePosition.addTextChangedListener(this);// 添加文本输入框监听事件
		searchButton = (Button) findViewById(R.id.search_postition_button);
		searchRoute = (Button) findViewById(R.id.search_the_route);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.search_frame, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 点击搜索按钮开始Route搜索
	 */
	public void searchRoute() {
		strStart = searchMyPosition.getText().toString().trim();
		strEnd = searchDePosition.getText().toString().trim();
		if (strStart == null || strStart.length() == 0) {
			strStart = "我的位置";
		}
		if (strEnd == null || strEnd.length() == 0) {
			ToastUtil.showLong(SearchFrameActivity.this, "请选择终点");
			return;
		}
		if (strStart.equals(strEnd)) {
			ToastUtil.showLong(SearchFrameActivity.this, "不能选择相同地点");
			return;
		}
		
		Intent intent = getIntent();
		Bundle dataBundle = new Bundle();
		dataBundle.putString("start", strStart);
		dataBundle.putString("end", strEnd);
		intent.putExtras(dataBundle);
		SearchFrameActivity.this.setResult(0, intent);
		SearchFrameActivity.this.finish();
		// startSearchResult();// 开始搜终点
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = searchPosition.getText().toString().trim();
		if (keyWord == null || keyWord.length() == 0) {
			ToastUtil.showLong(SearchFrameActivity.this, "请选择要搜索的位置");
			return;
		}
		Intent intent = getIntent();
		Bundle dataBundle = new Bundle();
		dataBundle.putString("key", keyWord);
		intent.putExtras(dataBundle);
		SearchFrameActivity.this.setResult(0, intent);
		SearchFrameActivity.this.finish();
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(SearchFrameActivity.this,
				new InputtipsListener() {
					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {// 正确返回
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
								ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
										getApplicationContext(),
										R.layout.route_inputs, listString);
								((AutoCompleteTextView) searchPosition)
										.setAdapter(aAdapter);
								aAdapter.notifyDataSetChanged();
							}
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "027".toString());
		} catch (com.amap.api.services.core.AMapException e) {
			e.printStackTrace();
		}// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.search_postition_button:
			searchButton();
			break;
		case R.id.search_the_route:
			searchRoute();
			break;
		default:
			break;
		}
	}
	
}
