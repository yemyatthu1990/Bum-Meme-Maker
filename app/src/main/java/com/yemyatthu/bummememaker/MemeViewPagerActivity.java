package com.yemyatthu.bummememaker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MemeViewPagerActivity extends FragmentActivity {
	
	private ViewPager mViewPager;
	public static final String TAB_TAG = "com.yemyatthu.bummememaker.TABTAG";
	private int tabPos;
	private ArrayList<Meme> mPagerMemes;
	private ArrayList<Meme> mMemes;
	private ArrayList<Meme> mMyanmarMemes;
	private ArrayList<Meme> mFavoriteMemes;
	private ArrayList<Meme> mCustomMemes;
	private String memeName;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		tabPos = getIntent().getIntExtra(TAB_TAG, 10);
		
		mMemes = MemeLab.get(this).getMemes();
		mMyanmarMemes = MemeLab.get(this).getMyanmarMemes();
		mFavoriteMemes = MemeLab.get(this).getFavoriteMemes();
		mCustomMemes = MemeLab.get(this).getCustomMemes();
		mPagerMemes = new ArrayList<Meme>();
		switch (tabPos) {
		case 0:
			mPagerMemes.addAll(mMemes);
			break;
		case 1:
			mPagerMemes.addAll(mMyanmarMemes);
			break;
		case 2:
			mPagerMemes.addAll(mFavoriteMemes);
			break;
		case 3:
			mPagerMemes.addAll(mCustomMemes);
			break;
		default:
			Meme meme = new Meme();
			meme.setName(getIntent().getStringExtra(MemeViewFragment.NAME_TAG));
			mPagerMemes.add(meme);
			break;
		}
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentPagerAdapter(fm){
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return mPagerMemes.size();
			}

			@Override
			public Fragment getItem(int position) {
				
				// TODO Auto-generated method stub
				Meme meme = mPagerMemes.get(position);
				return MemeViewFragment.getNewInstance(meme.getName());}

			
			
			
			
		});
		
		String memeName = getIntent().getStringExtra(MemeViewFragment.NAME_TAG);
		for(int i =0;i<mPagerMemes.size();i++){
			if(mPagerMemes.get(i).getName().equals(memeName)){
				mViewPager.setCurrentItem(i);
				setTitle(getMemeTitle(mPagerMemes.get(i)));
				break;
			}
		}
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				// TODO Auto-generated method stub
				Meme meme = mPagerMemes.get(position);
				if(getMemeTitle(meme)!= null){
					setTitle(getMemeTitle(meme));
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	
	}
	
	public String getMemeTitle(Meme meme){
	if(((MemeLab.get(this).getMemes()).contains(meme))||
			((MemeLab.get(this)).getMyanmarMemes()).contains(meme)){
		memeName = (meme.getName()).substring(0, 1).toUpperCase(Locale.ENGLISH);
	memeName += (meme.getName()).substring(1).replace("_", " ").toLowerCase(Locale.ENGLISH);}
	else{
		File file = new File(meme.getName());
		memeName = file.getName().substring(0,1).toUpperCase(Locale.ENGLISH);
		memeName += file.getName().substring(1).toLowerCase(Locale.ENGLISH);
		
	}
	
	return memeName;}
}
