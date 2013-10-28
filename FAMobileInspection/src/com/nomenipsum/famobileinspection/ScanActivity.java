package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ScanActivity extends Activity {
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	
	private DataReceiver dataScanner = new DataReceiver();
	
	private TextView tvScanProgress;
	private TextView tvItemData;
	
	private EditText etCode;
	
	private Button btnEquipment;
	
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
		tvItemData = (TextView)findViewById(R.id.tvItemData);
		etCode = (EditText)findViewById(R.id.etCode);
		etCode.setOnEditorActionListener(codeEntered);
		etCode.addTextChangedListener(textWatcher);
		btnEquipment = (Button)findViewById(R.id.btnEquipment);
	}
	
	OnEditorActionListener codeEntered = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (etCode.getText() != null)
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
	 * Searches Inspectiondata.xml for the equipment matching the ID 
	 * and display its attributes
	 * <p>
	 * FIXME: Create EquipmentActivity and start activity from when a valid ID is found
	 * @param id : The ID of the piece of equipment that was scanned or entered
	 */
	private void DisplayEquipmentAttributes(String id)	{
		try
		{
			String path = Environment.getExternalStorageDirectory().toString();

	    	File f = new File(path + "/savedReports/InspectionData.xml");
	    	InputStream is= new FileInputStream(f.getPath());
			
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(is);
		    
		    // Loop through all nodes of type Room
		    NodeList nodeList = document.getElementsByTagName("Room");
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        Node node = nodeList.item(i);
		        NodeList equipmentList = node.getChildNodes();
		        
		        // Loop through child nodes of Room
		        for (int j = 0; j < equipmentList.getLength(); j++)	{
		        	Node equipment = equipmentList.item(j);
		        	NamedNodeMap attributes = equipment.getAttributes();
		        	
		        	// Skip to next node if current node contains no attributes
		        	if (attributes == null)
		        		continue;
		        	
		        	// Check if equipment element matches id, if so display all of its attributes 
		        	if (attributes.getNamedItem("id").getTextContent().equals(id))	{
			        	tvItemData.setText(equipment.getNodeName() + "\n");
		        		for (int k = 0; k < attributes.getLength(); k++)
		        			tvItemData.append(attributes.item(k).getNodeName() + ": " + attributes.item(k).getTextContent() + "\n");
		        		
		        		// Show button that lets user modify equipment inspection
		        		final String message = id;
		        		btnEquipment.setText("Inspect " + equipment.getNodeName() + " " + message);
		        		btnEquipment.setOnClickListener(new OnClickListener()	{
		                	public void onClick(View v)	{
		                		 Intent intent = new Intent(getBaseContext(), EquipmentView.class);
		                         intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", message);
		                         try
		                         {
		                         startActivity(intent);
		                         }
		                         catch (Exception e)
		                         {
		                        	 System.out.println(e.toString());
		                         
		                         }
		            		}
		            	});
		        		btnEquipment.setVisibility(View.VISIBLE);
		    		    tvScanProgress.setText("Equipment with ID " + id + " found");
		    		    tvScanProgress.setTextColor(Color.GREEN);
		    		    
		        		// Quit method when equipment is found
		        		return;
		        	}
		        }
		    }
		    
		    // Notify user that equipment is not found
		    tvScanProgress.setText("Equipment with ID " + id + " not found");
		    tvScanProgress.setTextColor(Color.BLACK);
        	tvItemData.setText("");
        	btnEquipment.setVisibility(View.INVISIBLE);
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
	
	/**
	 * Overwrites an XML file with its modified attributes and elements
	 * 
	 * @param document : Document built from the InputStream containing the source XML file
	 */
	void WriteXML(Document document)
	{
		try	{
			// Save XML File to device
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
		    // Initialise StreamResult with File object to save to file
		    StreamResult result = new StreamResult(new StringWriter());
		    DOMSource source = new DOMSource(document);
		    transformer.transform(source, result);
		}
		catch (TransformerException e)	{
			System.out.println(e.toString());
		}
		
	}
}
