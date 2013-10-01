package com.whumap.activity;

import com.whumap.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ArticleActivity extends Activity {
	
	private int ArticleId;
	WebView wv;
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
	}
}
