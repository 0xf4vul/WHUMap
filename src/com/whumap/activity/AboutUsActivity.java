package com.whumap.activity;

import java.net.URLEncoder;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.whumap.util.ToastUtil;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.content.Intent;

public class AboutUsActivity extends SherlockPreferenceActivity implements OnPreferenceClickListener{

	private Preference memberOne,memberTwo,contact;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_us);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		memberOne = (Preference)findPreference("member_one");
		memberTwo = (Preference)findPreference("member_two");
		contact = (Preference)findPreference("email");
		memberOne.setOnPreferenceClickListener(this);
		memberTwo.setOnPreferenceClickListener(this);
		contact.setOnPreferenceClickListener(this);
	}
	@Override
	public boolean onPreferenceClick(Preference preference) {
		String key = preference.getKey();
		if("member_one".equals(key)) {
			Uri uriWeibo = Uri.parse("http://www.weibo.com/wtkb24");
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW,uriWeibo);
			try {
				startActivity(launchBrowser);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_launch_browser);
			}
			return true;
		} else if("member_two".equals(key)) {
			Uri github = Uri.parse("http://flsf.github.io/");
			Intent goToGithub = new Intent(Intent.ACTION_VIEW,github);
			try {
				startActivity(goToGithub);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_send_email);
			}
			return true;
		} else if("email".equals(key)) {
			
			final Intent myEmail = new Intent(android.content.Intent.ACTION_SENDTO);
			@SuppressWarnings("deprecation")
			String uriText = "mailto:wangtongkb24@gmail.com" +
					"?subject=" + URLEncoder.encode("求加入"); 
			myEmail.setData(Uri.parse(uriText));
			try {
				startActivity(myEmail);
			} catch (Exception e) {
				ToastUtil.showLong(this, R.string.fail_to_send_email);
			}

			return true;
		} else {
			return false;
		}
	
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
}
