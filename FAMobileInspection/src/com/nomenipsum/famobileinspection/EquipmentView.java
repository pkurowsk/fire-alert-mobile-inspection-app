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
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class EquipmentView extends Activity {
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment_view);
		//Button submitButton;
		TextView tv1 = new TextView(this);
		TextView tv2 = new TextView(this);
		CheckBox cb1 = new CheckBox(this);
		cb1.setText("Pass/Fail?");
		EditText et1 = new EditText(this);
		tv2.setText("Inspection Element Name");
		tv1 = (TextView) findViewById(R.id.textView1);
		
		DisplayEquipmentAttributes("33101");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.equipment_view, menu);
		return true;
	}
	
	private void DisplayEquipmentAttributes(String id)	{
		TextView tv1 = new TextView(this);
		
		tv1 = (TextView) findViewById(R.id.textView1);
		try
		{
			// Get InspectionData from assets
			InputStream is = getAssets().open("InspectionData.xml");
			
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
			        	tv1.setText(equipment.getNodeName() + "\n");
		        		for (int k = 0; k < attributes.getLength(); k++)
		        			tv1.append(attributes.item(k).getNodeName() + ": " + attributes.item(k).getTextContent() + "\n");
		        		NodeList inspectionElementList = equipment.getChildNodes();
		        		for (int h = 0; h < inspectionElementList.getLength(); h++)	{
				        	Node inspectionElement = inspectionElementList.item(h);
				        	NamedNodeMap attributes1 = inspectionElement.getAttributes();
				        	System.out.println(inspectionElement.getNodeName());
				        	
				        	if (attributes1 == null)
				        		continue;
				        	
				        	TextView tv2 = new TextView(this);
				    		CheckBox cb1 = new CheckBox(this);
				    		EditText et1 = new EditText(this);
			        		tv2.setText(attributes1.getNamedItem("name").getTextContent());
			        		LinearLayout linear = (LinearLayout)findViewById(R.id.linearLayout1);
			        		linear.addView(tv2);
			        		linear.addView(cb1);
			        		linear.addView(et1);
		        		}
		        		// Quit method when equipment is found
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
	public void OnClickRecord(View view){
		
	}

}
