package com.whumap.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.whumap.activity.R;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends SherlockActivity{
	
	private int ArticleId;
	WebView wv;
	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		wv = (WebView) findViewById(R.id.webView1);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle id = intent.getExtras();
		ArticleId = id.getInt("name");
		wv.loadUrl("file:///android_asset/" + ArticleId + ".html");
		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
