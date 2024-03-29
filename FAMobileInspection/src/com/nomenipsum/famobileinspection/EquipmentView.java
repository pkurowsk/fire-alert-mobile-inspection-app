package com.nomenipsum.famobileinspection;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EquipmentView extends Activity {
	String path = Environment.getExternalStorageDirectory().toString();

    private Node[] inspectionElements;
    private CheckBox[] testResults;
    private EditText[] testNotes;
    
    InspectionReportController _controller;
    
  protected void onCreate(Bundle savedInstanceState) {
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equipment_view);
    
    Intent intent = getIntent();
	String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
	
	_controller = new InspectionReportController(this);
	
	InspectionReportModel.getInstance().FindEquipment(message, true);

    DisplayEquipmentAttributes();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.equipment_view, menu);
    return true;
  }
  
  private void DisplayEquipmentAttributes()  {
    TextView tvAttributes = (TextView) findViewById(R.id.tvEqAttributes);
    
    Node equipment = InspectionReportModel.getInstance().currentNode;
    NamedNodeMap attributes = equipment.getAttributes();
    	
    //Format and display equipment attributes
    tvAttributes.setText(equipment.getNodeName() + "\n");
    for (int k = 0; k < attributes.getLength(); k++)
    	tvAttributes.append(attributes.item(k).getNodeName() + ": " + attributes.item(k).getTextContent() + "\n");
    NodeList inspectionElementList = equipment.getChildNodes();
	    
    inspectionElements = new Node[inspectionElementList.getLength()];
    testResults = new CheckBox[inspectionElementList.getLength()];
    testNotes = new EditText[inspectionElementList.getLength()];
	    
	// Loop through list of inspection elements
    for (int i = 0; i < inspectionElementList.getLength(); i++)  {
        Node inspectionElement = inspectionElementList.item(i);
        NamedNodeMap inspectionAttributes = inspectionElement.getAttributes();
   
        if (inspectionElement.getNodeType() != Node.ELEMENT_NODE)
        	continue;
              
      // Create fields for inspections
      TextView tvTestName = new TextView(this);
      CheckBox cbPassFail = new CheckBox(this);
      EditText etTestNotes = new EditText(this);
      
      // Set values from report
      tvTestName.setText(inspectionAttributes.getNamedItem("name").getTextContent() + " test passed:");
      cbPassFail.setChecked(inspectionAttributes.getNamedItem("testResult").getTextContent().equals("Yes"));
      etTestNotes.setText(inspectionAttributes.getNamedItem("testNote").getTextContent());
      
      // Store the values that will be saved
      inspectionElements[i] = inspectionElement;
      testResults[i] = cbPassFail;
      testNotes[i] = etTestNotes;
      
      // Add inspection fields to linear layout
      LinearLayout llInspectionElements = (LinearLayout)findViewById(R.id.llInspectionElements);
      llInspectionElements.addView(tvTestName);
      llInspectionElements.addView(cbPassFail);
      llInspectionElements.addView(etTestNotes);
    }
    
    ((Button)findViewById(R.id.btnRecord)).requestFocus();
	   
  }
  
  public void OnClickRecord(View view){
	  _controller.verifyReport(inspectionElements, testResults, testNotes);
  	}
  
}