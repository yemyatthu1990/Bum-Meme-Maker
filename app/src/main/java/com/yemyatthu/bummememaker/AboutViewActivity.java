package com.yemyatthu.bummememaker;

import android.app.Activity;
import android.os.Bundle;

public class AboutViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meme_view);
		getFragmentManager().beginTransaction().add(R.id.fragmentContainer,new AboutViewFragment()).commit();
	}
}
