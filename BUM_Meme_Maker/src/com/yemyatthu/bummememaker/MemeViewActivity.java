package com.yemyatthu.bummememaker;



import java.util.ArrayList;

import com.yemyatthu.bummememaker.MemeListFragment.MemeAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

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
