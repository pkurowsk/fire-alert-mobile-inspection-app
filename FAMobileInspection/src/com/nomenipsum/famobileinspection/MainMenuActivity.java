package com.nomenipsum.famobileinspection;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
		
		//LoadXML();
		new LoadClients().execute("");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	/**
	 * Creates a line of buttons on the view for each client
	 * 
	 * @param ll : A LinearLayout that will have Buttons and TextViews added to it
	 */
	private void LoadXML(LinearLayout ll)	{
		if (InspectionReportModel.getInstance().getDocument() == null)
			return;
		
	    NodeList clientContracts = InspectionReportModel.getInstance().getDocument().getElementsByTagName("clientContract");
	    
	    for (int i = 0; i < clientContracts.getLength(); i++) {
	        Node clientContract = clientContracts.item(i);
	        if (clientContract.getNodeType() == Node.ELEMENT_NODE) {
	        	// Display client in textview and contract in button
	            final String contractId = clientContract.getAttributes().getNamedItem("id").getTextContent();
	            TextView clientText = new TextView(this);
	            clientText.setText(" " + clientContract.getParentNode().getAttributes().getNamedItem("name").getTextContent());
	            Button contractButton = new Button(this);
	            contractButton.setText(contractId);
	                  
	            // When a button is pressed it sends the client 
		        contractButton.setOnClickListener(new OnClickListener()	{
			        public void onClick(View v)	{
				        Intent intent = new Intent(getBaseContext(), ClientInfoPage.class);
				        intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", contractId);
				        startActivity(intent);
			        }
		        });
		        
		        // Add text and buttons to layout
		        ll.addView(clientText);
		        ll.addView(contractButton);
	            
	        }
	    }
	}
	
	/**
	 * Asynchronous task that loads clients from XML into a linear layout
	 * @author Philip
	 *
	 */
	private class LoadClients extends AsyncTask<String, Void, String> {
		
		// Add text and buttons to layout
        LinearLayout ll = new LinearLayout(getBaseContext());
        
        @Override
        protected String doInBackground(String... params) {
        	ll.setOrientation(LinearLayout.VERTICAL);
            LoadXML(ll);
            return null;
        }        

        @Override
        protected void onPostExecute(String result) {      
        	LinearLayout ll = (LinearLayout)findViewById(R.id.llClients);
        	ll.addView(this.ll);
        	ll.removeView(findViewById(R.id.prgClientLoading));
        }

        @Override
        protected void onPreExecute() {
        	ProgressBar prg =  (ProgressBar)findViewById(R.id.prgClientLoading);
        	prg.setProgress(0);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
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
					
					tcpModel.RTSPSend(InspectionReportModel.getInstance().getReportAsString());
					tcpModel.close();
					
					Toast.makeText(getBaseContext(), "Report Sent", Toast.LENGTH_SHORT).show();
				
			   } catch (IOException e) {
					e.printStackTrace();
					Toast.makeText(getBaseContext(), "Report not sent: " + e.toString(), Toast.LENGTH_SHORT).show();

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
