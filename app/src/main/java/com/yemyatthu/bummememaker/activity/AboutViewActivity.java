package com.yemyatthu.bummememaker.activity;

import android.app.Activity;
import android.os.Bundle;
import com.yemyatthu.bummememaker.R;
import com.yemyatthu.bummememaker.fragment.AboutViewFragment;

public class AboutViewActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_meme_view);
    getFragmentManager().beginTransaction()
        .add(R.id.fragmentContainer, new AboutViewFragment())
        .commit();
  }
}
