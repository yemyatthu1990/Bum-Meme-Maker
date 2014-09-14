package com.yemyatthu.bummememaker;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class MemeViewActivity extends Activity 
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meme_view);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		FragmentManager fm = getFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		if(fragment == null){
			fragment = MemeViewFragment.getNewInstance(getIntent().getStringExtra(MemeViewFragment.NAME_TAG));
			fm.beginTransaction().add(R.id.fragmentContainer,fragment).commit();
		}
		
    }
   
}
