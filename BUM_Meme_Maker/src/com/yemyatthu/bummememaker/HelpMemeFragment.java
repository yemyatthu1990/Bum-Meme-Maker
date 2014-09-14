package com.yemyatthu.bummememaker;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HelpMemeFragment extends Fragment {
	private TextView helpText;
	public static final String HELP_TAG = "com.yemyatthu.mememaker.HELP";
	
	public static HelpMemeFragment getNewInstance(String memeName){
		Bundle args = new Bundle();
		args.putSerializable(HELP_TAG, memeName);
		HelpMemeFragment hlpFragment = new HelpMemeFragment();
		hlpFragment.setArguments(args);
		return hlpFragment;
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	@Override 
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_help_meme, container,false);
		helpText = (TextView)v.findViewById(R.id.help_text_view);
		Meme meme = MemeLab.get(getActivity()).getMeme(getArguments().getString(HELP_TAG));
		try{
		helpText.setText(meme.getHelp());}
		catch(NullPointerException e){
			helpText.setText(R.string.help_custom_image);
		}
		return v;
	}
}
