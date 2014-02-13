package com.nomenipsum.famobileinspection;


import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	
	TextView tvMainMenuTitle;
	
	SendResultsController _sendResultsController;
	ScanController _scanController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		tvMainMenuTitle = (TextView)findViewById(R.id.tvMainMenuTitle);
		tvMainMenuTitle.setText("Welcome, " + Account.getInstance().getFName());
		
		_sendResultsController = new SendResultsController(this);
		_scanController = new ScanController(this);
		
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
		{
			TextView tvNoFile = new TextView(this);
			tvNoFile.setText("No Inspection Report found");
			tvNoFile.setTextSize(20);
			ll.addView(tvNoFile);
			
			return;
		}

		
	    NodeList clientContracts = InspectionReportModel.getInstance().getDocument().getElementsByTagName("clientContract");
	    
	    for (int i = 0; i < clientContracts.getLength(); i++) {
	        Node clientContract = clientContracts.item(i);
	        if (clientContract.getNodeType() == Node.ELEMENT_NODE) {
	        	// Display client in textview and contract in button
	            final String contractId;
	            if (clientContract.getAttributes().getNamedItem("id") != null){
	               	contractId = clientContract.getAttributes().getNamedItem("id").getTextContent();
               }
               else
               {
	               	contractId = "Contract ID missing";
               }
	            
               TextView clientText = new TextView(this);
               if (clientContract.getParentNode().getAttributes().getNamedItem("name") != null)
            	   clientText.setText(" " + clientContract.getParentNode().getAttributes().getNamedItem("name").getTextContent());
               else if (clientContract.getParentNode().getAttributes().getNamedItem("id")!=null)
            	   clientText.setText("Client name missing, Client id: " + clientContract.getParentNode().getAttributes().getNamedItem("id").getTextContent());
               else
            	   clientText.setText("Client name and id missing");
               
               Button contractButton = new Button(this);
               contractButton.setText(contractId);
               if (contractButton.getText().equals("Contract ID missing"))
               {
                   contractButton.setClickable(false);
                   contractButton.setEnabled(false);
               }
	                  
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
		_scanController.startScan();
		/*
		Intent intent = new Intent(this, ScanActivity.class);
	    startActivity(intent);
	    */
	}
	
	 public void OnClickManageAccounts(View v){
		 if (!Account.getInstance().isAdmin)	{
			 Toast.makeText(this, "Only Admins can manage user accounts", Toast.LENGTH_SHORT).show();
			 return;
		 }
				
	     Intent intent = new Intent(this, SelectUser.class);
	     startActivity(intent);
	   } 
	 
	 public void OnClickSendResults(View v)	{
		 _sendResultsController.sendReport();
	 }
}
