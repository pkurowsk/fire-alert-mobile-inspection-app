package com.nomenipsum.famobileinspection;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import android.widget.CheckBox;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.nomenipsum.famobileinspection.R.menu;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowUserAccounts extends Activity {
private TextView tvUserData, tvUserTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_user_accounts);
		tvUserData=(TextView)findViewById(R.id.tvUserData);
		tvUserTitle=(TextView)findViewById(R.id.tvUserTitle);
		
		
		DisplayUserDetails();
	}
	
	private void DisplayUserDetails(){		
		try
		{
		String path = Environment.getExternalStorageDirectory().toString();
		
		File f = new File(path + "/UserAccounts.xml");
		InputStream is;
		if(f.exists())
			is= new FileInputStream(f.getPath());
		else
			is = getAssets().open("UserAccounts.xml");
		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(is);
        
        NodeList nodeList = document.getElementsByTagName("user");
        tvUserTitle.setText("Users On Device\n");
        tvUserTitle.setTextSize(25.0f);
        for(int i = 0; i< nodeList.getLength(); i++){
        	Node node = nodeList.item(i);
        	 if (node.getNodeType() == Node.ELEMENT_NODE) {
                 NamedNodeMap attributes = node.getAttributes();
                  	
                	 tvUserData.append(attributes.getNamedItem("firstname").getTextContent() + " " + attributes.getNamedItem("lastname").getTextContent() + ": " + attributes.getNamedItem("username").getTextContent() + "\n");
                	 tvUserData.setTextSize(20.0f);
                 
        	 }
        }
        return;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.show_user_accounts, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_add:
			addNewUser();
		return true;
		case R.id.action_edit:
			selectUser();
		return true;
		case R.id.action_delete:
			
		return true;
		default:
		//return super.onOptionsItemsSelected(item);	
		}
		return true;
	}
	
	public void selectUser(){
		Intent intent = new Intent(this, SelectUser.class);
	     startActivity(intent);
	}
	
	public void addNewUser(){
		Intent intent = new Intent(this, ManageUserAccounts.class);
	     startActivity(intent);
	}
	
	public void editCurrentUser(){
		
	}
	
	public void deleteCurrentUser(){
		
	}

}
