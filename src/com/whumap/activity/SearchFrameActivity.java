package com.whumap.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class SearchFrameActivity extends Activity {

	private EditText searchPosition; //输入的是要查找的内容
	private EditText searchMyPosition; //输入的是起始位置，默认为我的位置
	private EditText searchDePosition; //输入的是终点位置
	private Button searchButton; //搜位置
	private Button searchRoute; //查找路线
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_frame);
		findViewById(); //初始化搜索界面中的所有按钮
	}

	public void findViewById() {
		searchPosition = (EditText)findViewById(R.id.search_position_content);
		searchMyPosition = (EditText)findViewById(R.id.search_route_my_position);
		searchDePosition = (EditText)findViewById(R.id.search_route_destination_position);
		searchButton = (Button) findViewById(R.id.search_postition_button);
		searchRoute = (Button)findViewById(R.id.search_the_route);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_frame, menu);
		return true;
	}

}
