package com.nomenipsum.famobileinspection;

import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


public class SendResultsController {
	
	MainMenuActivity _view;
	
	public SendResultsController(MainMenuActivity v)	{
		_view = v;
		
	}
	
	public void sendReport()	{
		AlertDialog.Builder alert = new AlertDialog.Builder(_view);

		 alert.setTitle("Send Report Data");
		 alert.setMessage("Enter IP Address and Port Number");

		 // Set an EditText view to get user input 
		 final EditText etIP = new EditText(_view);
		 etIP.setHint("Server IP Address");
		 
		 final EditText etPort = new EditText(_view);
		 etPort.setHint("Port Number");
		 
		 LinearLayout llAlert = new LinearLayout(_view);
		 llAlert.setOrientation(1);
		 llAlert.addView(etIP);
		 llAlert.addView(etPort);
		 alert.setView(llAlert);
		 
		 alert.setView(llAlert);

		 alert.setPositiveButton("Send Report", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			   String ipAddress = etIP.getText().toString();
			   int portNo = Integer.parseInt(etPort.getText().toString());
			   
			   try {
					TCPModel tcpModel = new TCPModel(ipAddress, portNo);
					
					tcpModel.RTSPSend(InspectionReportModel.getInstance().getReportAsString());
					tcpModel.close();
					
					Toast.makeText(_view.getBaseContext(), "Report Sent", Toast.LENGTH_SHORT).show();
				
			   } catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(_view.getBaseContext(), "Report not sent: " + e.toString(), Toast.LENGTH_SHORT).show();

			   }
			 }
		 });

		 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
		     
		   }
		 });

		 alert.show();
	}
}
