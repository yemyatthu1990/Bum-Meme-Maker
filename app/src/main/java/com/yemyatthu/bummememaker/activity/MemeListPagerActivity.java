package com.yemyatthu.bummememaker.activity;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.yemyatthu.bummememaker.R;
import com.yemyatthu.bummememaker.fragment.MemeListFragment;

public class MemeListPagerActivity extends FragmentActivity {

  private ViewPager mViewPager;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    mViewPager = new ViewPager(this);
    mViewPager.setId(R.id.listPager);
    mViewPager.setOffscreenPageLimit(3);
    setContentView(mViewPager);

    final ActionBar actionBar = getActionBar();
    if (actionBar == null) {
      throw new AssertionError();
    }
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {

      @Override
      public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        mViewPager.setCurrentItem(tab.getPosition());
      }

      @Override
      public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

      }
    };

    actionBar.addTab(
        actionBar.newTab().setText(R.string.meme_list_title).setTabListener(tabListener));
    actionBar.addTab(
        actionBar.newTab().setText(R.string.myanmar_meme_title).setTabListener(tabListener));
    actionBar.addTab(
        actionBar.newTab().setText(R.string.favorite_meme_title).setTabListener(tabListener));
    actionBar.addTab(
        actionBar.newTab().setText(R.string.custom_meme_title).setTabListener(tabListener));

    FragmentManager fm = getSupportFragmentManager();
    mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

      @Override
      public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return MemeListFragment.getNewInstance(position);
      }

      @Override
      public int getCount() {
        // TODO Auto-generated method stub
        return 4;
      }
    });
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

      public void onPageSelected(int position) {
        // When swiping between pages, select the
        // corresponding tab.
        getActionBar().setSelectedNavigationItem(position);
      }
    });
  }
}
