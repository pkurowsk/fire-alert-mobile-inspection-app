package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
			String path = Environment.getExternalStorageDirectory().toString();

			File f = new File(path + "/savedReports/InspectionData.xml");
	    	InputStream is= new FileInputStream(f.getPath());
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(is);
		    
		    NodeList nodeList = document.getElementsByTagName("Client");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	final String clientName = node.getAttributes().getNamedItem("name").getTextContent();
	            	final String clientID = node.getAttributes().getNamedItem("id").getTextContent();
                               NodeList nodelist2 = document.getElementsByTagName("clientContract");
	                                Node node2 = nodelist2.item(i);
	                               final String contractId = node2.getAttributes().getNamedItem("id").getTextContent();
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
		                 LinearLayout ll = (LinearLayout)findViewById(R.id.llClients);
		                 ll.addView(clientText);
		                 ll.addView(contractButton);
		            
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
