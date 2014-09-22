package com.yemyatthu.bummememaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

public class SaveNameDialogFragment extends DialogFragment {
	private View view;
	public SaveNameDialogFragment(View v){
		view = v;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
	
		return new AlertDialog.Builder(getActivity()).setTitle(R.string.name).setView(view).setPositiveButton(R.string.ok_button,new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sendResult(Activity.RESULT_OK);
			}}).setNegativeButton(R.string.cancel_button,new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					sendResult(Activity.RESULT_CANCELED);
				}
		}).create();
	}
	
	public void sendResult(int resultCode){
		if(getTargetFragment() == null) return;
		Intent i = new Intent();
		getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
	}
		
}
