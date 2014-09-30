package com.yemyatthu.bumc.activity;

import android.app.Activity;
import android.os.Bundle;
import com.yemyatthu.bumc.fragment.SettingsFragment;

public class SettingsActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActionBar().setIcon(android.R.drawable.screen_background_light_transparent);
    getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new SettingsFragment())
        .commit();
  }
}
