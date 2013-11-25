package com.nomenipsum.famobileinspection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Notification {
	
	public static void Show(Context c, String title, String message, String btnOK, String btnCancel)	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
		 
		// set title
		alertDialogBuilder.setTitle(title);
		// set dialog message
		alertDialogBuilder
			.setMessage(message)
			.setCancelable(false);
			
		
		if (!btnOK.equals("null"))	{
			alertDialogBuilder.setPositiveButton(btnOK,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				}
			  });
		}
		
		if (!btnCancel.equals("null"))	{
			alertDialogBuilder.setNegativeButton(btnCancel,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
				}
			  });
		}
		
		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	}

}
