package com.yemyatthu.bumc.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yemyatthu.bumc.BUMC;
import com.yemyatthu.bumc.BUMC.TrackerName;
import com.yemyatthu.bumc.R;

public class ConfirmDialogFragment extends DialogFragment {
  public static final String TITLE_TAG = "com.yemyatthu.bummememaker.TITLE";
  public static final String MESSAGE_TAG = "com.yemyatthu.bummememaker.MESSAGE";

  public static ConfirmDialogFragment getNewInstance(int message, int title) {
    Bundle args = new Bundle();
    args.putInt(TITLE_TAG, title);
    args.putInt(MESSAGE_TAG, message);
    ConfirmDialogFragment fragment = new ConfirmDialogFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    return new AlertDialog.Builder(getActivity()).setMessage(getArguments().getInt(MESSAGE_TAG))
        .setTitle(getArguments().getInt(TITLE_TAG))
        .setIcon(R.drawable.ic_dialog_alert_holo_light)
        .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            Tracker t =
                ((BUMC) getActivity().getApplication()).getTracker(TrackerName.GLOBAL_TRACKER);
            // Build and send an Event.
            t.send(new HitBuilders.EventBuilder().setCategory("New Meme")
                .setAction("Change Name and Save Meme")
                .build());
            sendResult(Activity.RESULT_OK);
          }
        })
        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            sendResult(Activity.RESULT_CANCELED);
          }
        })
        .create();
  }

  public void sendResult(int resultCode) {
    if (getTargetFragment() == null) return;
    Intent i = new Intent();
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
  }
}
