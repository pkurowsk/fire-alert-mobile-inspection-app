package com.nomenipsum.famobileinspection;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ClientsPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clients_page);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
		
		LoadClientsToButtons((LinearLayout)findViewById(R.id.llClients));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clients_page, menu);
		return true;
	}
	
	/**
	 * Creates a line of buttons on the view for each client
	 */
	private void LoadClientsToButtons(LinearLayout ll)	{
		if (InspectionReportModel.getInstance().getDocument() == null)
			return;
		
	    NodeList clientContracts = InspectionReportModel.getInstance().getDocument().getElementsByTagName("clientContract");
	    
	    for (int i = 0; i < clientContracts.getLength(); i++) {
	        Node clientContract = clientContracts.item(i);
	        if (clientContract.getNodeType() == Node.ELEMENT_NODE) {
		        final String clientName = clientContract.getParentNode().getAttributes().getNamedItem("name").getTextContent();
		        	
	            final String contractId = clientContract.getAttributes().getNamedItem("id").getTextContent();
	            TextView clientText = new TextView(this);
	            clientText.setText(clientName);
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
	
}
