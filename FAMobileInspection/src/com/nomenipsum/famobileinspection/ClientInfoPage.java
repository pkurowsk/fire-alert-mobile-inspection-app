package com.nomenipsum.famobileinspection;

import java.util.Dictionary;
import java.util.Hashtable;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ClientInfoPage extends Activity {
  private TextView tvServiceAddress;
  private Button btnTerms;
  private LinearLayout llFloorRooms;
  private Spinner spnSAddr;
  
  Node selectedServiceAddress;
  
  Dictionary<String, Node> d = new Hashtable<String, Node>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_client_info_page);
    
    tvServiceAddress = (TextView)findViewById(R.id.tvServiceAddress);
    btnTerms = (Button)findViewById(R.id.btnTerms);
    llFloorRooms = (LinearLayout)findViewById(R.id.llFloorRooms);
    spnSAddr = (Spinner)findViewById(R.id.spnSAddr);
    
    spnSAddr.setOnItemSelectedListener(new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedServiceAddress = d.get(spnSAddr.getSelectedItem().toString());
			UpdateServiceAddress();
			displayLocationElements();
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    });
    
    Intent intent = getIntent();
    String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
    
    DisplayPageDetails(message);
  }
  
  /**
   * Displays floors, rooms, and equipment specified in the contract
   * @param id : The id of the contract in the XML report
   */
  private void DisplayPageDetails(String id)  {
	  Node clientContract = InspectionReportModel.getInstance().Find("clientContract", id, true);
        
	  NamedNodeMap attributes = clientContract.getAttributes();
		  
	  // Show contract attributes
	  if (attributes.getNamedItem("id").getTextContent().equals(id))  {
		  btnTerms.setText("Contract " + attributes.getNamedItem("id").getTextContent() + " Terms");
		
		// Get service address
		NodeList contractChildren = clientContract.getChildNodes();
			
		ArrayAdapter <CharSequence> spnAdapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for (int i = 0; i < contractChildren.getLength(); i++)	{
			if (contractChildren.item(i).getNodeType() == Node.ELEMENT_NODE)	{
				if (selectedServiceAddress == null)
					selectedServiceAddress = contractChildren.item(i);
				d.put(contractChildren.item(i).getAttributes().getNamedItem("address").getTextContent(), contractChildren.item(i));
				spnAdapter.add(contractChildren.item(i).getAttributes().getNamedItem("address").getTextContent());
				
			}
		}
		spnSAddr.setAdapter(spnAdapter);
		
		UpdateServiceAddress();	
		displayLocationElements();
		
  		}
  }
  
  private void displayLocationElements()	{
	  llFloorRooms.removeAllViews();
	// Loop through floors
	NodeList floors = selectedServiceAddress.getChildNodes();
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
	    				
	    				// When a button is pressed it sends the equipment id as an intent message
	    				final String message = equipment.getAttributes().getNamedItem("id").getTextContent();
		                btnEquipment.setOnClickListener(new OnClickListener()	{
		                	public void onClick(View v)	{
			            			Intent intent = new Intent(getBaseContext(), EquipmentView.class);
			            		    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", message);
	    		            		startActivityForResult(intent, 1);
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
  
  public void OnTermsClicked(View v)	{
	  NamedNodeMap contractAttrs = InspectionReportModel.getInstance().currentNode.getAttributes();
		Notification.Show(this, 
				"Terms", 
				"No " + contractAttrs.getNamedItem("No").getTextContent() + "\n" + 
				contractAttrs.getNamedItem("startDate").getTextContent() + " - " +
				contractAttrs.getNamedItem("endDate").getTextContent() + "\n\n" + 
				contractAttrs.getNamedItem("terms").getTextContent(), 
				"Dismiss", 
				"null");
  }
  
  @Override 
  public void onActivityResult(int requestCode, int resultCode, Intent data) {     
	  super.onActivityResult(requestCode, resultCode, data); 
	  switch(requestCode) { 
		  case (1) : { 
			  if (resultCode == Activity.RESULT_OK) {
				  UpdateServiceAddress();
			  } 
			  break; 
		  } 
	  }
	  
	  // Find the client contract if the current node is not already a contract
	  while (!InspectionReportModel.getInstance().currentNode.getNodeName().equals("clientContract"))	{
		  InspectionReportModel.getInstance().TraverseUp();
	  }
  }
  
  /**
   * Replaces the text in the Service address info 
   * with the service address attributes
   */
  private void UpdateServiceAddress()	{
	  NamedNodeMap addressAttributes = selectedServiceAddress.getAttributes();
		
		tvServiceAddress.setText("Service Address " + addressAttributes.item(0).getTextContent() + "\n");
		for (int i = 1; i < addressAttributes.getLength(); i++)	{
			tvServiceAddress.append(addressAttributes.item(i).getTextContent());
			
			if (i == addressAttributes.getLength() - 3)
				tvServiceAddress.append("\n");
			else
				tvServiceAddress.append(", ");
			
		}
		
		
  }
  
}