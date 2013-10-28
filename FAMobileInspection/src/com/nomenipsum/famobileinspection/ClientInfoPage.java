package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
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
import android.os.Environment;
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
  private TextView tvClientData, tvServiceAddress;
  private LinearLayout llFloorRooms;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_info_page);
    tvClientData = (TextView)findViewById(R.id.tvClientData);
    tvServiceAddress = (TextView)findViewById(R.id.tvServiceAddress);
    llFloorRooms = (LinearLayout)findViewById(R.id.llFloorRooms);
    
    Intent intent = getIntent();
    String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
    
    DisplayPageDetails(message);
  }
  
  private void DisplayPageDetails(String id)  {
    try
    {
    	String path = Environment.getExternalStorageDirectory().toString();

    	File f = new File(path + "/savedReports/InspectionData.xml");
    	InputStream is= new FileInputStream(f.getPath());
      
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(is);
        
        NodeList nodeList = document.getElementsByTagName("clientContract");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
              NamedNodeMap attributes = node.getAttributes();
              // Found matching contract
              if (attributes.getNamedItem("id").getTextContent().equals(id))  {
            	tvClientData.setText("");
                tvClientData.append("Contract ID: " + attributes.getNamedItem("id").getTextContent() + "\nNo. " +attributes.getNamedItem("No").getTextContent() +  "\n"); 
                tvClientData.append("Start date: " + attributes.getNamedItem("startDate").getTextContent() + "\n");
                tvClientData.append("End Date: " + attributes.getNamedItem("endDate").getTextContent() + "\n");
                
                // Get service address
                NodeList contractChildren = node.getChildNodes();
                Node serviceAddress = null;
                for (int j = 0; j < contractChildren.getLength(); j++)
                	if (contractChildren.item(j).getNodeType() == Node.ELEMENT_NODE)
                		serviceAddress = contractChildren.item(j);
                		
                NamedNodeMap addressAttributes = serviceAddress.getAttributes();
                
                tvServiceAddress.setText("Service Address\n");
                for (int j = 0; j < addressAttributes.getLength(); j++)
                	tvServiceAddress.append(addressAttributes.item(j).getNodeName() + ": " + addressAttributes.item(j).getTextContent() + "\n");
                
                NodeList floors = serviceAddress.getChildNodes();
                
                // Loop through floors
                for (int j = 0; j < floors.getLength(); j++)	{
                	if (floors.item(j).getNodeType() == Node.ELEMENT_NODE)	{
                		TextView tvFloorRooms = new TextView(this);
            			tvFloorRooms.setText("Floor: " + floors.item(j).getAttributes().getNamedItem("name").getTextContent() + "\n");
            			
            			// loop through rooms
            			NodeList rooms = floors.item(j).getChildNodes();
            			for (int k = 0; k < rooms.getLength(); k++)	{
                        	if (rooms.item(k).getNodeType() == Node.ELEMENT_NODE)	{
                        		tvFloorRooms.append("\tRoom: " + rooms.item(k).getAttributes().getNamedItem("id").getTextContent() + " " +
                        				rooms.item(k).getAttributes().getNamedItem("No").getTextContent());
                        		
                				llFloorRooms.addView(tvFloorRooms);
                        		
                        		// Loop through equipment
                        		NodeList equipmentList = rooms.item(k).getChildNodes();
                        		for (int h = 0; h < equipmentList.getLength(); h++)	{
                        			Node equipment = equipmentList.item(h);
                        			if (equipment.getNodeType() == Node.ELEMENT_NODE)	{
                        				Button btnEquipment = new Button(this);
                        				btnEquipment.setText(equipment.getNodeName() + " " + equipment.getAttributes().getNamedItem("id").getTextContent());
                        				
                        				final String message = equipment.getAttributes().getNamedItem("id").getTextContent();
                        				// When a button is pressed it sends the equipment 
                		                btnEquipment.setOnClickListener(new OnClickListener()	{
                		                	public void onClick(View v)	{
                		            			Intent intent = new Intent(getBaseContext(), EquipmentView.class);
                		            		    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", message);
                		            		    startActivity(intent);
                		            		}
                		            	});
                        				llFloorRooms.addView(btnEquipment);
                        			}
                        		}
                        	}
            			}
                	}
                }
                	
                
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
}