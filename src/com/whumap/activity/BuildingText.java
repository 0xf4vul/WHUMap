package com.whumap.activity;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class BuildingText extends SherlockActivity {
	
	private int imageId;
	private WebView webView;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zheng_wen_introduction);
		webView = (WebView) findViewById(R.id.zheng_wen_introduction_webview);
		progressBar = (ProgressBar)findViewById(R.id.zheng_wen_introduction_progressbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		init();
	}

	private void init() {
		Intent intent = getIntent();
		Bundle id = intent.getExtras();
		imageId = id.getInt("name");
		new MyAsnycTask().execute();
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
	
	private class MyAsnycTask extends AsyncTask<String, String,String>{

		@Override
		protected String doInBackground(String... urls) {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String data) {
			progressBar.setVisibility(View.GONE);
			webView.loadUrl("file:///android_asset/b" + imageId + ".html");
		}
	}
}
