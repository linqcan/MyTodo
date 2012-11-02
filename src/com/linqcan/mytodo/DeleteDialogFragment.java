package com.linqcan.mytodo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

public class DeleteDialogFragment extends DialogFragment{
	
	public interface DeleteDialogListener {
		
		public void onClickDeleteOK();

	}
	

	public static DeleteDialogFragment newInstance(int title){
		DeleteDialogFragment fragment = new DeleteDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");
		return new AlertDialog.Builder(getActivity())
			.setMessage(title)
			.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialogif, int which) {
				DeleteDialogListener dl = (DeleteDialogListener) getActivity();
				dl.onClickDeleteOK();
				
			}
		}).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {			
			public void onClick(DialogInterface dialogif, int which) {
				
			}
		}).create();
	}
}
