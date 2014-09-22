package com.yemyatthu.bummememaker;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashScreenFragment extends Fragment {
	private final int SPLASH_LENGTH = 1000;
	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent mainIntent = new Intent(getActivity(),MemeListPagerActivity.class);
				getActivity().startActivity(mainIntent);
				getActivity().finish();
			}
			
		},SPLASH_LENGTH);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup root,Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_splash_screen, root,false);
		return v;
	}
}
