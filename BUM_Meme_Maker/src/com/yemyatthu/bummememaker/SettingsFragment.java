package com.yemyatthu.bummememaker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
	private SharedPreferences prefs;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		prefs = getPreferenceManager().getSharedPreferences();
		prefs.registerOnSharedPreferenceChangeListener(this);
		
		
		addPreferencesFromResource(R.xml.preferences);
		
		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		prefs.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		prefs.unregisterOnSharedPreferenceChangeListener(this);
	}
	

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		Preference pref = findPreference(key);
		if(key.equals("fontColor")){
			switch (Integer.parseInt(sharedPreferences.getString("fontColor", "1"))){
			case 1: pref.setSummary("White");
				break;
			case 2: pref.setSummary("Black");
				break;
			case 3: pref.setSummary("Red");
				break;
			case 4: pref.setSummary("Gray");
				break;
			case 5: pref.setSummary("Green");
				break;
			case 6: pref.setSummary("Yellow");
				break;
			case 7: pref.setSummary("Blue");
				break;
			default:
			}
			}
		if(key.equals("borderColor")){
			switch (Integer.parseInt(sharedPreferences.getString("borderColor", "1"))){
			case 1: pref.setSummary("White");
				break;
			case 2: pref.setSummary("Black");
				break;
			case 3: pref.setSummary("Red");
				break;
			case 4: pref.setSummary("Gray");
				break;
			case 5: pref.setSummary("Green");
				break;
			case 6: pref.setSummary("Yellow");
				break;
			case 7: pref.setSummary("Blue");
				break;
			default:
			}
		}
		if(key.equals("shadowColor")){
			switch (Integer.parseInt(sharedPreferences.getString("shadowColor", "2"))){
			case 1: pref.setSummary("White");
				break;
			case 2: pref.setSummary("Black");
				break;
			case 3: pref.setSummary("Red");
				break;
			case 4: pref.setSummary("Gray");
				break;
			case 5: pref.setSummary("Green");
				break;
			case 6: pref.setSummary("Yellow");
				break;
			case 7: pref.setSummary("Blue");
				break;
			default:
			}
		}
		if(key.equals("capCheckBox")){

		}
		prefs = sharedPreferences;
		
	}
}
