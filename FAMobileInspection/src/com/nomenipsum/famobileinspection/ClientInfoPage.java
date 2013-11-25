package com.nomenipsum.famobileinspection;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        Node clientContract = InspectionReportModel.getInstance().Find("clientContract", id, true);
        
		  NamedNodeMap attributes = clientContract.getAttributes();
		  
		  // Show contract attributes
		  if (attributes.getNamedItem("id").getTextContent().equals(id))  {
			tvClientData.setText("");
			tvClientData.append("Contract ID: " + attributes.getNamedItem("id").getTextContent() + "\nNo. " +attributes.getNamedItem("No").getTextContent() +  "\n"); 
			tvClientData.append("Start date: " + attributes.getNamedItem("startDate").getTextContent() + "\n");
			tvClientData.append("End Date: " + attributes.getNamedItem("endDate").getTextContent() + "\n");
		
		// Get service address
		NodeList contractChildren = clientContract.getChildNodes();
		Node serviceAddress = null;
		for (int i = 0; i < contractChildren.getLength(); i++)
			if (contractChildren.item(i).getNodeType() == Node.ELEMENT_NODE)
				serviceAddress = contractChildren.item(i);
				
		NamedNodeMap addressAttributes = serviceAddress.getAttributes();
		
		tvServiceAddress.setText("Service Address\n");
		for (int i = 0; i < addressAttributes.getLength(); i++)
			tvServiceAddress.append(addressAttributes.item(i).getNodeName() + ": " + addressAttributes.item(i).getTextContent() + "\n");
		
		// Loop through floors
		NodeList floors = serviceAddress.getChildNodes();
		for (int i = 0; i < floors.getLength(); i++)	{
			if (floors.item(i).getNodeType() == Node.ELEMENT_NODE)	{
				TextView tvFloorRooms = new TextView(this);
				tvFloorRooms.setText("Floor: " + floors.item(i).getAttributes().getNamedItem("name").getTextContent() + "\n");
			
			// Loop through rooms
			NodeList rooms = floors.item(i).getChildNodes();
			for (int j = 0; j < rooms.getLength(); j++)	{
		    	if (rooms.item(j).getNodeType() == Node.ELEMENT_NODE)	{
		    		tvFloorRooms.append("\tRoom: " + rooms.item(j).getAttributes().getNamedItem("id").getTextContent() + " " +
		    				rooms.item(j).getAttributes().getNamedItem("No").getTextContent());
		    		
					llFloorRooms.addView(tvFloorRooms);
		    		
		    		// Loop through equipment
		    		NodeList equipmentList = rooms.item(j).getChildNodes();
		    		for (int k = 0; k < equipmentList.getLength(); k++)	{
		    			Node equipment = equipmentList.item(k);
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
  
  public void OnTermsClicked(View v)	{
		Notification.Show(this, 
				"Terms", 
				InspectionReportModel.getInstance().currentNode.getAttributes().getNamedItem("terms").getTextContent(), 
				"Dismiss", 
				"null");
  }
  
}