package com.nomenipsum.famobileinspection;

import org.w3c.dom.Node;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class InspectionReportController 
{
	EquipmentView _view;
	
	public InspectionReportController(EquipmentView v)	{
		_view = v;
		
	}
	
	/**
	 * Verifies that all failed tests have inspection elements and
	 * displays an alert of mistakes if any.
	 * Navigates back a page if the verification was successful.
	 * 
	 * @param inspectionElements : Nodes containing inspection elements
	 * @param testResults : Checkboxes containing the values of a test
	 * @param testNotes : EditTexts containing possible notes of a test
	 */
	public void verifyReport(Node[] inspectionElements, CheckBox[] testResults, EditText[] testNotes)	{
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
			  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_view);
				 
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
			  currentSAddress.getAttributes().getNamedItem("InspectorID").setTextContent("ID" + Account.getInstance().id);
			  
			  // Save the inspection report
			  InspectionReportModel.getInstance().SaveReport();
			  Toast.makeText(_view.getBaseContext(), "Report Saved", Toast.LENGTH_SHORT).show();
			  
			  // Return to the previous page
			  Intent resultIntent = new Intent();
			  _view.setResult(Activity.RESULT_OK, resultIntent);
			  _view.finish();
		  }
	}
}
