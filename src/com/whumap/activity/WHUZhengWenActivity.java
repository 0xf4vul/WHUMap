package com.whumap.activity;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.whumap.zhengwenutils.GetDetailArticle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class WHUZhengWenActivity extends SherlockActivity {

	ProgressBar progress;
	WebView container;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whu_zheng_wen_activity);
		progress = (ProgressBar)findViewById(R.id.article_details_progressbar);
		container = (WebView) findViewById(R.id.article_details_webview);
		String url = getIntent().getStringExtra("news_url");
		container.getSettings().getJavaScriptEnabled();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		new MyAsnycTask().execute(url,getIntent().getStringExtra("news_title"),getIntent().getStringExtra("news_date"));

	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class MyAsnycTask extends AsyncTask<String, String,String>{

		@Override
		protected String doInBackground(String... urls) {
			String data=GetDetailArticle.getNewsDetails(urls[0],urls[1],urls[2]);
			return data;
		}

		@Override
		protected void onPostExecute(String data) {
			progress.setVisibility(View.GONE);
			container.loadDataWithBaseURL (null, data, "text/html", "utf-8",null);
		}
	}
}
