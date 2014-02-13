package com.nomenipsum.famobileinspection;

import org.w3c.dom.Node;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ScanController {
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";

	MainMenuActivity _view;
	
	private DataReceiver dataScanner;
	
	public ScanController(MainMenuActivity v)	{
		_view = v;
		
	}
	
	/**
	 * Creates a dialog box with an edittext to accept equipment ID and
	 * registers the scanner
	 */
	public void startScan()	{
		registerScanner();
		AlertDialog.Builder alert = new AlertDialog.Builder(_view);

		 alert.setTitle("Scanning...");
		 alert.setMessage("or enter equipment ID below");

		 // Set an EditText view to get user input 
		 final EditText etEquipmentID = new EditText(_view);
		 etEquipmentID.setHint("Enter Equipment ID");
		 
		 LinearLayout llAlert = new LinearLayout(_view);
		 llAlert.setOrientation(1);
		 llAlert.addView(etEquipmentID);
		 alert.setView(llAlert);
		 
		 alert.setView(llAlert);

		 alert.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			   String eID = etEquipmentID.getText().toString();
			   CheckEquipment(eID);
			   
			 }
		 });

		 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		     unregisterReceiver();
		   }
		 });

		 alert.show();
	}
	
	
	private void registerScanner() {
		dataScanner = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		_view.registerReceiver(dataScanner, intentFilter);
	}
	
	private void unregisterReceiver() {
		if (dataScanner != null) _view.unregisterReceiver(dataScanner);
	}
	
	private class DataReceiver extends BroadcastReceiver {
		String content = "";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				
				CheckEquipment(content);
			}
				
		}
	}
	
	/**
	 * Searches Inspectiondata.xml for the equipment matching the ID 
	 * <p>
	 * @param id : The ID of the piece of equipment that was scanned or entered
	 */
	private void CheckEquipment(String id)	{
		
		Node equipment = InspectionReportModel.getInstance().FindEquipment(id, true);
		if (equipment == null)	{
			Toast.makeText(_view, "Equipment " + id + " not found", Toast.LENGTH_SHORT).show();

			return;
		}
		
		unregisterReceiver();
		
		Intent intent = new Intent(_view, EquipmentView.class);
        intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", id);
        _view.startActivity(intent);
		    
		
	}
}
