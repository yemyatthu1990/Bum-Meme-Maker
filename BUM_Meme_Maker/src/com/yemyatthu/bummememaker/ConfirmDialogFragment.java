package com.yemyatthu.bummememaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ConfirmDialogFragment extends DialogFragment{
	public final String SEND_RESULT = "OK";
	private int mMessage;
	private int mTitle;
	public ConfirmDialogFragment(int saveConfirm,int title)
	{	
	
		mMessage = saveConfirm;
		mTitle = title;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
			return new AlertDialog.Builder(getActivity()).setMessage(mMessage).setTitle(mTitle).setIcon(R.drawable.ic_dialog_alert_holo_light)
					.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sendResult(Activity.RESULT_OK);
					}
					
				})
				.setNegativeButton(R.string.cancel_button,new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						sendResult(Activity.RESULT_CANCELED);
					}
					
				} )
				.create();}
	
	
	
	public void sendResult(int resultCode){
		if(getTargetFragment() == null) return;
		Intent i = new Intent();
		getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,i);
	}
		
	
	

}
