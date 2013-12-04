package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectUser extends Activity {
	 private Button[] userSelect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_user);
		DisplayUserSelect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_user, menu);
		return true;
	}

	private void DisplayUserSelect(){
		
		if (UserAcountModel.getInstance().getDocument() == null)
			return;
		
	    NodeList nodeList = UserAcountModel.getInstance().getDocument().getElementsByTagName("user");
	    userSelect = new Button[nodeList.getLength()];
	    for(int i = 0; i< nodeList.getLength(); i++){
        	Node node = nodeList.item(i);
        	 if (node.getNodeType() == Node.ELEMENT_NODE) {
                 NamedNodeMap attributes = node.getAttributes();
                
                 Button select = new Button(this);
                 TextView text = new TextView(this);
                 select.setTag(attributes.getNamedItem("id").getTextContent());
                 text.setText(attributes.getNamedItem("firstname").getTextContent()+ " " + attributes.getNamedItem("lastname").getTextContent() + ": " + attributes.getNamedItem("username").getTextContent());
                 select.setText(attributes.getNamedItem("id").getTextContent() );
                 userSelect[i]=select;
                 LinearLayout llSelectUser = (LinearLayout)findViewById(R.id.llSelectUsers);
       	         llSelectUser.addView(text);
                 llSelectUser.addView(userSelect[i]);
       	         userSelect[i].setOnClickListener(handleOnClick(userSelect[i]));
       	         
        	 }
        }
	    
		
	}
	
	View.OnClickListener handleOnClick(final Button button){
		return new View.OnClickListener(){
			public void onClick(View v){
				doThis(button);
			}
		};
	}
	
	public void doThis(final Button button){
	   	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	 alert.setTitle("What do you want to do?");
		 LinearLayout llAlert = new LinearLayout(this);
		 llAlert.setOrientation(1); 
		 alert.setView(llAlert);
		 
		 alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
				edit(button);	 
			 }
		 });

		 alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
			   delete(button);
		   }
		 });

		 alert.show();
	 }
	
	public void edit(final Button button){
		Intent I = new Intent(this, ManageUserAccounts.class);
	    Bundle bundle = new Bundle();
	    
	    String ID=button.getText().toString();
	    bundle.putString("ID", ID);
	    I.putExtras(bundle);
	    finish();
	    startActivity(I);
	}
	
	public void delete(final Button button){
		AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

		 alert2.setTitle("Are You Sure?");
		 		 
		 LinearLayout llAlert2 = new LinearLayout(this);
		 llAlert2.setOrientation(1);
		 
		 alert2.setView(llAlert2);
		 alert2.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
				deleteNode(button);
					 
				
			 
			 }
		 });

		 alert2.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int whichButton) {
			   
			   
		   }
		 });
		 alert2.show(); 
			 
	}
	
	public void addUser(View v){
		Intent I = new Intent(this, ManageUserAccounts.class);
		 Bundle bundle = new Bundle(); 
		    bundle.putString("ID", "");
		    I.putExtras(bundle);
		    finish();
		startActivity(I);
	}
	
	public void deleteNode(final Button button){
		String identifier=button.getText().toString();
		
		if (UserAcountModel.getInstance().getDocument() == null)
			return;
		
	    NodeList nodeList = UserAcountModel.getInstance().getDocument().getElementsByTagName("user");
	    userSelect = new Button[nodeList.getLength()];
	    for(int i = 0; i< nodeList.getLength(); i++){
        	Node node = nodeList.item(i);
        	 if (node.getNodeType() == Node.ELEMENT_NODE) {
                 NamedNodeMap attributes = node.getAttributes();
                 String compare=attributes.getNamedItem("id").getTextContent();
                 
                 if(compare.equals(identifier)){
                	 UserAcountModel.getInstance().giveRoot().removeChild(node);
                	 UserAcountModel.getInstance().getDocument().normalize();
                	 UserAcountModel.getInstance().SaveAccounts();
                	 Intent intent=getIntent();
                	 finish();
                	 startActivity(intent);
                	 
                 }
                 
       	         
        	 }
        }
	    
		
		
		
	}
}
