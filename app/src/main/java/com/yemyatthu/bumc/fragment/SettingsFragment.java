package com.yemyatthu.bumc.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import com.yemyatthu.bumc.R;

public class SettingsFragment extends PreferenceFragment
    implements OnSharedPreferenceChangeListener {
  private SharedPreferences prefs;
  private String fontColorSummary = "fontColorSummary";
  private String borderColorSummary = "borderColorSummary";
  private String shadowColorSummary = "shadowColorSummary";
  private String waterMarkSummary = "waterMarkSummary";
  private String borderSizeSummary = "borderSizeSummary";
  private String waterMarkSizeSummary = "waterMarkSizeSummary";
  private String fontSummary = "fontSummary";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  public void onResume() {
    super.onResume();
    prefs.registerOnSharedPreferenceChangeListener(this);
    ListPreference fontColor = (ListPreference) findPreference("fontColor");
    if (fontColor.getSummary() != null) {
      fontColor.setSummary(prefs.getString(fontColorSummary, (String) fontColor.getSummary()));
    }
    ListPreference shadowColor = (ListPreference) findPreference("shadowColor");
    if (shadowColor.getSummary() != null) {
      shadowColor.setSummary(
          prefs.getString(shadowColorSummary, (String) shadowColor.getSummary()));
    }
    ListPreference borderColor = (ListPreference) findPreference("borderColor");
    if (borderColor.getSummary() != null) {
      borderColor.setSummary(
          prefs.getString(borderColorSummary, (String) borderColor.getSummary()));
    }
    ListPreference waterMarkSize = (ListPreference) findPreference("waterMarkSize");
    if (waterMarkSize.getSummary() != null) {
      waterMarkSize.setSummary(
          prefs.getString(waterMarkSizeSummary, (String) waterMarkSize.getSummary()));
    }
    ListPreference borderSize = (ListPreference) findPreference("borderSize");
    if (borderSize.getSummary() != null) {
      borderSize.setSummary(prefs.getString(borderSizeSummary, (String) borderSize.getSummary()));
    }
    ListPreference font = (ListPreference) findPreference("font");
    if (font.getSummary() != null) {
      font.setSummary(prefs.getString(fontSummary, (String) font.getSummary()));
    }
    EditTextPreference waterMark = (EditTextPreference) findPreference("waterMarkEditText");
    if (waterMark.getSummary() != null) {
      waterMark.setSummary(prefs.getString(waterMarkSummary, (String) waterMark.getSummary()));
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    prefs.unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // TODO Auto-generated method stub
    Preference pref = findPreference(key);
    if (key.equals("fontColor")) {
      switch (Integer.parseInt(sharedPreferences.getString("fontColor", "1"))) {
        case 1:
          pref.setSummary("White");

          break;
        case 2:
          pref.setSummary("Black");

          break;
        case 3:
          pref.setSummary("Red");

          break;
        case 4:
          pref.setSummary("Gray");

          break;
        case 5:
          pref.setSummary("Green");

          break;
        case 6:
          pref.setSummary("Yellow");

          break;
        case 7:
          pref.setSummary("Blue");

          break;
        default:
      }
      prefs.edit().putString(fontColorSummary, (String) pref.getSummary()).apply();
    }
    if (key.equals("borderColor")) {
      switch (Integer.parseInt(sharedPreferences.getString("borderColor", "1"))) {
        case 1:
          pref.setSummary("White");

          break;
        case 2:
          pref.setSummary("Black");

          break;
        case 3:
          pref.setSummary("Red");

          break;
        case 4:
          pref.setSummary("Gray");

          break;
        case 5:
          pref.setSummary("Green");

          break;
        case 6:
          pref.setSummary("Yellow");

          break;
        case 7:
          pref.setSummary("Blue");

          break;
        default:
      }
      prefs.edit().putString(borderColorSummary, (String) pref.getSummary()).apply();
    }
    if (key.equals("shadowColor")) {
      switch (Integer.parseInt(sharedPreferences.getString("shadowColor", "2"))) {
        case 1:
          pref.setSummary("White");

          break;
        case 2:
          pref.setSummary("Black");

          break;
        case 3:
          pref.setSummary("Red");

          break;
        case 4:
          pref.setSummary("Gray");

          break;
        case 5:
          pref.setSummary("Green");

          break;
        case 6:
          pref.setSummary("Yellow");

          break;
        case 7:
          pref.setSummary("Blue");

          break;
        default:
      }
      prefs.edit().putString(shadowColorSummary, (String) pref.getSummary()).apply();
    }
    if (key.equals("borderSize")) {
      switch (Integer.parseInt(sharedPreferences.getString(key, "60"))) {
        case 40:
          pref.setSummary("Small");
          break;
        case 60:
          pref.setSummary("Medium");
          break;
        case 80:
          pref.setSummary("Large");
          break;
        default:
          break;
      }
      prefs.edit().putString(borderSizeSummary, (String) pref.getSummary()).apply();
    }

    if (key.equals("waterMarkSize")) {
      switch (Integer.parseInt(sharedPreferences.getString(key, "40"))) {
        case 30:
          pref.setSummary("Small");
          break;
        case 50:
          pref.setSummary("Medium");
          break;
        case 70:
          pref.setSummary("Large");
          break;
        default:
          break;
      }
      prefs.edit().putString(waterMarkSizeSummary, (String) pref.getSummary()).apply();
    }
    if (key.equals("font")) {
      pref.setSummary(sharedPreferences.getString(key, "Impact"));
      prefs.edit().putString(fontSummary, (String) pref.getSummary()).apply();
    }
    if (key.equals("waterMarkEditText")) {
      pref.setSummary(sharedPreferences.getString("waterMarkEditText", "BUM"));
      prefs.edit().putString(waterMarkSummary, (String) pref.getSummary()).apply();
    }
  }
}
