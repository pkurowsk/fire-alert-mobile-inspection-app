package com.nomenipsum.famobileinspection;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ClientInfoPage extends Activity {
	private TextView tvClientData;
	private TextView Title;
	private LinearLayout llContract;
	private String CName = "na", Ccontract = "na", Caddress = "na", CFloor = "na", Croom = "na";  //trying possible method, won't work
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_info_page);
		
		tvClientData = (TextView)findViewById(R.id.tvClientData);
		Title = (TextView)findViewById(R.id.textView1);
		llContract = (LinearLayout)findViewById(R.id.llContracts);
		
		Intent intent = getIntent();
		final String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
		
		Title.setText("Client details for " + message);
		DisplayClientDetails(message);
		Contracts(message);
	}
	
	private void DisplayClientDetails(String id)	{ //shows client's details in text box at top of page
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
		        	NamedNodeMap attributes = node.getAttributes();
		        	// Found matching equipment
		        	if (attributes.getNamedItem("name").getTextContent().equals(id))	{
		        		tvClientData.append("ID: " + attributes.getNamedItem("id").getTextContent() + "\n"); 
		        		tvClientData.append("Name: " + attributes.getNamedItem("name").getTextContent() + "\n");
		        		tvClientData.append("Address: " + attributes.getNamedItem("address").getTextContent() + "\n");        		
		        		return;
		        	}
		        	
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
	
	private void Contracts(String id)	{ //Shows buttons for contracts, leads back to same page, cannot figure out how to keep track of which page it is on
		try
		{
			InputStream is = getAssets().open("InspectionData.xml");
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(is);
		    
		    NodeList nodeList = document.getElementsByTagName("clientContract");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	final String clientName = node.getAttributes().getNamedItem("id").getTextContent();
	            	Button clientButton = new Button(this);
	                clientButton.setText(clientName);
		                 
	                	// When a button is pressed it sends the client 
		                clientButton.setOnClickListener(new OnClickListener()	{
		                	public void onClick(View v)	{
		             			System.out.println(clientName);
		            			Intent intent = new Intent(getBaseContext(), ClientInfoPage.class);
		            		    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", clientName);
		            		    startActivity(intent);
		            		}
		            	});
		                 LinearLayout ll = (LinearLayout)findViewById(R.id.llContracts);
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