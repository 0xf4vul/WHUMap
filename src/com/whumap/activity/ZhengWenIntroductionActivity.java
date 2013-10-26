package com.whumap.activity;

import java.net.URLEncoder;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.whumap.util.ToastUtil;
import com.whumap.zhengwenutils.SetArticles;

public class ZhengWenIntroductionActivity extends SherlockActivity {

	private WebView webView;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zheng_wen_introduction);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		webView = (WebView)findViewById(R.id.zheng_wen_introduction_webview);
		progressBar = (ProgressBar)findViewById(R.id.zheng_wen_introduction_progressbar);
		new MyAsnycTask().execute();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()	) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.contribute:
			final Intent toEmail = new Intent(android.content.Intent.ACTION_SENDTO);
			String uriText2 = "mailto:wdxw@whu.edu.cn" +
					"?subject="+URLEncoder.encode("请填写正文类型");
			toEmail.setData(Uri.parse(uriText2));
			try {
				startActivity(toEmail);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_send_email);
			} 
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.contribute_menu, menu);
		return true;
	}
	
	private class MyAsnycTask extends AsyncTask<String, String,String>{

		@Override
		protected String doInBackground(String... urls) {
			String data = SetArticles.getZhengWentIntroduction();
			return data;
		}

		@Override
		protected void onPostExecute(String data) {
			progressBar.setVisibility(View.GONE);
			webView.loadDataWithBaseURL (null, data, "text/html", "utf-8",null);
		}
	}
}
