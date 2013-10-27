package com.nomenipsum.famobileinspection;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ClientsPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clients_page);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
		
		LoadXML();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.clients_page, menu);
		return true;
	}
	
	/**
	 * Loads inspection data and creates a line of buttons on the view for each client
	 * <p>
	 * FIXME: Only prints the client name, should show all contracts for the client
	 */
	private void LoadXML()	{
		try
		{
			InputStream is = getAssets().open("InspectionData.xml");
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(is);
		    
		    NodeList nodeList = document.getElementsByTagName("Client");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	final String clientName = node.getAttributes().getNamedItem("name").getTextContent();
	            	Button clientButton = new Button(this);
	                clientButton.setText(clientName);
		                 
	                	// When a button is pressed it sends the client 
		                clientButton.setOnClickListener(new OnClickListener()	{
		                	public void onClick(View v)	{
		            			Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
		            		    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", clientName);
		            		    startActivity(intent);
		            		}
		            	});
		                 LinearLayout ll = (LinearLayout)findViewById(R.id.llClients);
		                 ll.addView(clientButton);
		            
		        }
		    }
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
