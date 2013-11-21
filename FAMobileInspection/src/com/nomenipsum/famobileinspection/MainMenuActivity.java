package com.nomenipsum.famobileinspection;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	
	TextView tvMainMenuTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
		tvMainMenuTitle = (TextView)findViewById(R.id.tvMainMenuTitle);
		tvMainMenuTitle.setText("Welcome, " + message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public void OnClickClients(View v)	{
		
		Intent intent = new Intent(this, ClientsPage.class);
	    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", "message");
	    startActivity(intent);
	
	}
	
	public void OnClickScan(View v)	{
		Intent intent = new Intent(this, ScanActivity.class);
	    startActivity(intent);
	}
	
	 public void OnClickManageAccounts(View v){
		     Intent intent = new Intent(this, ManageUserAccounts.class);
		     startActivity(intent);
		   } 
	 
	 public void OnClickSendResults(View v)	{
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

		 alert.setTitle("Send Report Data");
		 alert.setMessage("Enter IP Address and Port Number");

		 // Set an EditText view to get user input 
		 final EditText etIP = new EditText(this);
		 etIP.setHint("Server IP Address");
		 
		 final EditText etPort = new EditText(this);
		 etPort.setHint("Port Number");
		 
		 LinearLayout llAlert = new LinearLayout(this);
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
				
				tcpModel.RTSPSend("xml file");
				tcpModel.close();
				
				Toast.makeText(getBaseContext(), "Report Sent", Toast.LENGTH_SHORT).show();
				
			   } catch (IOException e) {
					e.printStackTrace();
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
