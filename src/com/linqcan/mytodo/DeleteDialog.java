package com.linqcan.mytodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DeleteDialog{
	
	private Dialog dialog;
	private static Context cn;

	public DeleteDialog(Context context) {
		createDialog(context);
	}
	
	private void createDialog(Context context) {
		cn = context;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(R.string.delete_message).setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialogif, int which) {
				ViewTaskActivity ma = (ViewTaskActivity) cn;
				ma.dodelete();
				
			}
		}).setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialogif, int which) {
				
			}
		});
		dialog = builder.create();
	}
	
	public void show(){
		dialog.show();
	}
}
