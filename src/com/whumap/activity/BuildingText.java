package com.whumap.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class BuildingText extends SherlockActivity {
	private int imageId;
	WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buildingtext);
		wv = (WebView) findViewById(R.id.webView1);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle id = intent.getExtras();
		imageId = id.getInt("name");
		wv.loadUrl("file:///android_asset/b" + imageId + ".html");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
