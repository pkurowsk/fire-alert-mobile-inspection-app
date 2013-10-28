package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

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

public class EquipmentView extends Activity {
	String path = Environment.getExternalStorageDirectory().toString();
    private Document document;
    private Node[] inspectionElements;
    private CheckBox[] testResults;
    private EditText[] testNotes;
    
  protected void onCreate(Bundle savedInstanceState) {
    
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_equipment_view);
    
    Intent intent = getIntent();
	String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
	
	DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder;
	try {
        
    	File f = new File(path + "/savedReports/InspectionData.xml");
    	InputStream is= new FileInputStream(f.getPath());
    	
		docBuilder = docBuilderFactory.newDocumentBuilder();
	
		document = docBuilder.parse(f);
		is.close();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
    DisplayEquipmentAttributes(message);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.equipment_view, menu);
    return true;
  }
  
  private void DisplayEquipmentAttributes(String id)  {
    TextView tvAttributes = new TextView(this);
    
    tvAttributes = (TextView) findViewById(R.id.tvAttributes);
    
    // Loop through all nodes of type Room
    NodeList nodeList = document.getElementsByTagName("Room");
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);
        NodeList equipmentList = node.getChildNodes();
        
        // Loop through child nodes of Room
        for (int j = 0; j < equipmentList.getLength(); j++)  {
          Node equipment = equipmentList.item(j);
          NamedNodeMap attributes = equipment.getAttributes();
          
          // Skip to next node if current node contains no attributes
          if (equipment.getNodeType() != Node.ELEMENT_NODE)
            continue;
          
          // Check if equipment element matches id, if so display all of its attributes 
          if (attributes.getNamedItem("id").getTextContent().equals(id))  {
            tvAttributes.setText(equipment.getNodeName() + "\n");
            for (int k = 0; k < attributes.getLength(); k++)
              tvAttributes.append(attributes.item(k).getNodeName() + ": " + attributes.item(k).getTextContent() + "\n");
            NodeList inspectionElementList = equipment.getChildNodes();
            
            inspectionElements = new Node[inspectionElementList.getLength()];
            testResults = new CheckBox[inspectionElementList.getLength()];
            testNotes = new EditText[inspectionElementList.getLength()];
            
            // Loop through list of inspection elements
            for (int h = 0; h < inspectionElementList.getLength(); h++)  {
              Node inspectionElement = inspectionElementList.item(h);
              NamedNodeMap inspectionAttributes = inspectionElement.getAttributes();
   
              if (inspectionElement.getNodeType() != Node.ELEMENT_NODE)
                continue;
              
              // Create fields for inspections
              TextView tvTestName = new TextView(this);
              CheckBox cbPassFail = new CheckBox(this);
              EditText etTestNotes = new EditText(this);
              
              tvTestName.setText(inspectionAttributes.getNamedItem("name").getTextContent() + " test passed:");
              
              cbPassFail.setChecked(inspectionAttributes.getNamedItem("testResult").getTextContent().equals("Yes"));
              etTestNotes.setText(inspectionAttributes.getNamedItem("testNote").getTextContent());
              
              inspectionElements[h] = inspectionElement;
              testResults[h] = cbPassFail;
              testNotes[h] = etTestNotes;
              
              // Add inspection fields to linear layout
              LinearLayout llInspectionElements = (LinearLayout)findViewById(R.id.llInspectionElements);
              llInspectionElements.addView(tvTestName);
              llInspectionElements.addView(cbPassFail);
              llInspectionElements.addView(etTestNotes);
            }
            ((Button)findViewById(R.id.btnRecord)).requestFocus();
            // Quit method when equipment is found
            return;
          }
        }
    }
    
  }
  public void OnClickRecord(View view){
	  String mistakes = "";
	  for (int i = 0; i < inspectionElements.length; i++)	{
		  if (inspectionElements[i] == null)
			  continue;
		  
		  if (!testResults[i].isChecked() && testNotes[i].getText().toString().equals(""))	{
			  mistakes += "- " + inspectionElements[i].getAttributes().getNamedItem("name").getTextContent() + " requires test notes\n";
		  }
		  else	{
			  String result = "";
			  if (testResults[i].isChecked())	{
				  result = "Yes";
				  System.out.println("checked");
			  }
			  else
				  result = "No";
			  
			  inspectionElements[i].getAttributes().getNamedItem("testResult").setTextContent(result);
			  inspectionElements[i].getAttributes().getNamedItem("testNote").setTextContent(testNotes[i].getText().toString());
			  
		  }
			  
		  
		  
		  
	  }
	  if (!mistakes.equals(""))	{
		  AlertBoxShow("Mistakes In Report", mistakes);
	  }
	  else	{
		  WriteXML();
		  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			// set dialog message
			alertDialogBuilder
			.setTitle("Report Saved!")
				.setMessage("This report is completed.")
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
							backToEquipment();
					}
				  });
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
	  	}
  	}
  
  	private void backToEquipment() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
  
  /**
	 * Overwrites an XML file with its modified attributes and elements
	 * 
	 * @param document : Document built from the InputStream containing the source XML file
	 */
	void WriteXML()
	{
		try	{
			// Save XML File to device
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	
		    // Initialise StreamResult with File object to save to file
		    StreamResult result = new StreamResult(new StringWriter());
		    DOMSource source = new DOMSource(document);
		    transformer.transform(source, result);
		    
		    // Write transformed xml file
		    String xmlReport = result.getWriter().toString();
		    FileWriter fileIO = new FileWriter(new File(path + "/savedReports/InspectionData.xml"));
		    fileIO.write(xmlReport);
            fileIO.close();
		}
		catch (TransformerException e)	{
			System.out.println(e.toString());
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void AlertBoxShow(String title, String message)	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		 
		// set title
		alertDialogBuilder.setTitle(title);
		// set dialog message
		alertDialogBuilder
			.setMessage(message)
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

}