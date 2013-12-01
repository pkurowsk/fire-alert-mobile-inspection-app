package com.nomenipsum.famobileinspection;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EquipmentView extends Activity {
	String path = Environment.getExternalStorageDirectory().toString();

    private Node[] inspectionElements;
    private CheckBox[] testResults;
    private EditText[] testNotes;
    
  protected void onCreate(Bundle savedInstanceState) {
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equipment_view);
    
    Intent intent = getIntent();
	String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");

	InspectionReportModel.getInstance().FindEquipment(message, true);
	
    DisplayEquipmentAttributes(message);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.equipment_view, menu);
    return true;
  }
  
  private void DisplayEquipmentAttributes(String id)  {
    TextView tvAttributes = (TextView) findViewById(R.id.tvEqAttributes);
    
    Node equipment = InspectionReportModel.getInstance().currentNode;
    NamedNodeMap attributes = equipment.getAttributes();
    
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
	  // Store a check list of all the mistakes in the report
	  String mistakes = "";
	  
	  for (int i = 0; i < inspectionElements.length; i++)	{
		  if (inspectionElements[i] != null)	{
			  // Check if the Inspection Elements with a Fail have a reason
			  if (!testResults[i].isChecked() && testNotes[i].getText().toString().equals(""))
				  mistakes += "- " + inspectionElements[i].getAttributes().getNamedItem("name").getTextContent() + " requires test notes\n";
		  }
	  }
	  
	  if (!mistakes.equals(""))	{
		  
		  // Alert user of mistake
		  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			 
			// set title
			alertDialogBuilder.setTitle("Mistakes In Report");
			// set dialog message
			alertDialogBuilder
				.setMessage(mistakes)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
					}
				  });

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
	  }
	  else	{
		  
		  // Fill values into inspection report
		  for (int i = 0; i < inspectionElements.length; i++)	{
			  if (inspectionElements[i] != null)	{
				  String result = "";
				  if (testResults[i].isChecked())	{
					  result = "Yes";
				  }
				  else
					  result = "No";
				  
				  // Set Inspection Element values
				  inspectionElements[i].getAttributes().getNamedItem("testResult").setTextContent(result);
				  inspectionElements[i].getAttributes().getNamedItem("testNote").setTextContent(testNotes[i].getText().toString());
			  }
		  }
		  
		  // Timestamp the service address
		  Node currentSAddress = InspectionReportModel.getInstance().getCurrentNode();
		  while (!currentSAddress.getNodeName().equals("ServiceAddress"))	{
			  currentSAddress = currentSAddress.getParentNode();
		  }
		  currentSAddress.getAttributes().getNamedItem("testTimeStamp").setTextContent(java.util.GregorianCalendar.getInstance().getTime().toString());
		  
		  // Save the inspection report
		  InspectionReportModel.getInstance().SaveReport();
		  Toast.makeText(getBaseContext(), "Report Saved", Toast.LENGTH_SHORT).show();
		  
		  // Return to the previous page
		  Intent resultIntent = new Intent();
		  setResult(Activity.RESULT_OK, resultIntent);
		  finish();
	  }
		  
		  
  	}
  
}