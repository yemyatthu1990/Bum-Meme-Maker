package com.yemyatthu.bumc.activity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.yemyatthu.bumc.R;
import com.yemyatthu.bumc.fragment.MemeListFragment;
import com.yemyatthu.bumc.widget.SlidingTabLayout;

public class MemeListPagerActivity extends FragmentActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    setContentView(R.layout.acitivty_memelist);

    ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
    SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

    slidingTabLayout.setSelectedIndicatorColors(Color.WHITE);

    final ActionBar actionBar = getActionBar();
    if (actionBar == null) {
      throw new AssertionError();
    }

    mViewPager.setAdapter(
        new SlidingTabAdapter(getSupportFragmentManager(), MemeListPagerActivity.this));
    slidingTabLayout.setViewPager(mViewPager);
  }

  public class SlidingTabAdapter extends FragmentPagerAdapter {

    Context mContext;

    public SlidingTabAdapter(FragmentManager fm, Context context) {
      super(fm);
      this.mContext = context;
    }

    @Override public int getCount() {
      return 4;
    }

    @Override public Fragment getItem(int position) {
      return MemeListFragment.getNewInstance(position);
    }

    @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return mContext.getString(R.string.meme_list_title);
        case 1:
          return mContext.getString(R.string.myanmar_meme_title);
        case 2:
          return mContext.getString(R.string.favorite_meme_title);
        case 3:
          return mContext.getString(R.string.custom_meme_title);
      }
      return null;
    }

    @Override public long getItemId(int position) {
      return super.getItemId(position);
    }
  }
}
