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

public class ScanActivity extends Activity {
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private DataReceiver dataScanner = new DataReceiver();
	private TextView tvScanProgress;
	private TextView tvItemData;
	private EditText etCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}
	

    @Override
	protected void onResume() {
    	registerScanner();
    	initialComponent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	
	private void initialComponent() {
		tvScanProgress = (TextView)findViewById(R.id.tvScanProgress);
		tvItemData = (TextView)findViewById(R.id.tvClientData);
		etCode = (EditText)findViewById(R.id.etCode);
		etCode.setOnEditorActionListener(codeEntered);
		etCode.addTextChangedListener(textWatcher);
	}
	
	OnEditorActionListener codeEntered = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			DisplayEquipmentAttributes(etCode.getText().toString().trim());
			return false;
		}
		
	};
	
	private void registerScanner() {
		dataScanner = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		registerReceiver(dataScanner, intentFilter);
	}
	
	private void unregisterReceiver() {
		if (dataScanner != null) unregisterReceiver(dataScanner);
	}
	
	private TextWatcher textWatcher =  new TextWatcher(){
        public void onTextChanged(CharSequence s, int start, int before, int count){}
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    	public void afterTextChanged(Editable s){
    		//tv_getdata_from_edittext.setText("Get data from EditText : " + etCode.getText().toString());
    	}
    }; 
    
	private class DataReceiver extends BroadcastReceiver {
		String content = "";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				tvScanProgress.setText("Got data from Scanner : " + content);
				
				DisplayEquipmentAttributes(content);
			}
				
		}
	}
	
	/**
	 * Search Inspectiondata.xml for the equipment matching the ID 
	 * and display its attributes
	 * <p>
	 * FIXME: Only finds IDs of Extinguishers, should find all equipment (All children of Room element?) 
	 * <p>
	 * FIXME: Doesn't display Inspection elements
	 * 
	 * @param id : The ID of the piece of equipment that was scanned or entered
	 */
	private void DisplayEquipmentAttributes(String id)	{
		try
		{
			InputStream is = getAssets().open("InspectionData.xml");
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(is);
		    
		    NodeList nodeList = document.getElementsByTagName("Extinguisher");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		        	NamedNodeMap attributes = node.getAttributes();
		        	// Found matching equipment
		        	if (attributes.getNamedItem("id").getTextContent().equals(id))	{
		        		tvItemData.append("ID: " + attributes.getNamedItem("id").getTextContent() + "\n"); 
		        		tvItemData.append("Location: " + attributes.getNamedItem("location").getTextContent() + "\n");
		        		tvItemData.append("Size: " + attributes.getNamedItem("size").getTextContent() + "\n"); 
		        		tvItemData.append("Type: " + attributes.getNamedItem("type").getTextContent() + "\n");
		        		tvItemData.append("Model: " + attributes.getNamedItem("model").getTextContent() + "\n");
		        		tvItemData.append("Serial No: " + attributes.getNamedItem("serialNo").getTextContent() + "\n");
		        		if (attributes.getNamedItem("manufacturingDate") != null)
		        			tvItemData.append("Manufacturing Date: " + attributes.getNamedItem("manufacturingDate").getTextContent());
		        		
		        		return;
		        	}
		        	
		        }
		    }
		    
		    tvScanProgress.setText("Equipment not found");
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
