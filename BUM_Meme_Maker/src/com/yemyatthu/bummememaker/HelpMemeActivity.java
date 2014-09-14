package com.yemyatthu.bummememaker;

import android.app.Activity;
import android.os.Bundle;

public class HelpMemeActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help_meme);
		getFragmentManager().beginTransaction().add(R.id.help_meme_container,HelpMemeFragment.getNewInstance(getIntent().getStringExtra(HelpMemeFragment.HELP_TAG))).commit();
	}
}
