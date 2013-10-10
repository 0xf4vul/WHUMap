package com.whumap.activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

public class SettingsActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener,
	OnPreferenceClickListener{

	private CheckBoxPreference suoFangBox,biaoChiBox,zhiNanZhenBox;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		suoFangBox = (CheckBoxPreference)findPreference("suo_fang");
		biaoChiBox = (CheckBoxPreference)findPreference("biao_chi");
		zhiNanZhenBox = (CheckBoxPreference)findPreference("zhi_nan_zhen");
		suoFangBox.setOnPreferenceChangeListener(this);
		suoFangBox.setOnPreferenceClickListener(this);
		biaoChiBox.setOnPreferenceChangeListener(this);
		biaoChiBox.setOnPreferenceClickListener(this);
		zhiNanZhenBox.setOnPreferenceChangeListener(this);
		zhiNanZhenBox.setOnPreferenceClickListener(this);
	}


	@Override
	public boolean onPreferenceClick(Preference preference) {
		return true;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		return true;
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
