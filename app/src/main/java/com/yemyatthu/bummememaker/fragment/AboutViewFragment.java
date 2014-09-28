package com.yemyatthu.bummememaker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yemyatthu.bummememaker.R;

public class AboutViewFragment extends Fragment {
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_about_view, container, false);
    TextView license = (TextView) v.findViewById(R.id.about_text_view);
    license.setMovementMethod(new ScrollingMovementMethod());
    return v;
  }
}
