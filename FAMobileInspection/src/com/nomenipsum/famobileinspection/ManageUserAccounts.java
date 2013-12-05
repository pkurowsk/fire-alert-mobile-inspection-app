package com.nomenipsum.famobileinspection;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ManageUserAccounts extends Activity {
	Node currentNode=null;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getIntent().getExtras();
    final String identifier= bundle.getString("ID");
    setContentView(R.layout.activity_manage_user_accounts);
    
    
    if (UserAcountModel.getInstance().getDocument() == null)
		return;
	
    NodeList nodeList = UserAcountModel.getInstance().getDocument().getElementsByTagName("user");
    
    for(int i = 0; i< nodeList.getLength(); i++){
    	Node node = nodeList.item(i);
    	 if (node.getNodeType() == Node.ELEMENT_NODE) {
             NamedNodeMap attributes = node.getAttributes();
             String a = attributes.getNamedItem("id").getTextContent();
             String b = identifier;
             if(a.equals(b)){
            	   currentNode=node;
            	   NamedNodeMap attributes1 = currentNode.getAttributes();
            	   EditText firstname = (EditText) findViewById(R.id.FirstName);
            	   firstname.setText(attributes1.getNamedItem("firstname").getTextContent());
            	   EditText lastname = (EditText) findViewById(R.id.LastName);
            	   lastname.setText(attributes1.getNamedItem("lastname").getTextContent());
            	   EditText password = (EditText) findViewById(R.id.enterPassword);
            	   password.setText(attributes1.getNamedItem("pass").getTextContent());
            	   EditText password2 = (EditText) findViewById(R.id.enterPassword2);
            	   password2.setText(attributes1.getNamedItem("pass").getTextContent());
            	   EditText username = (EditText) findViewById(R.id.enterName);
            	   username.setText(attributes1.getNamedItem("username").getTextContent());
             }
            	 
             }
             
    	
   		

   		 
    	 }
    
    
    
    
    
    
 
    Button createAccount = (Button) findViewById(R.id.createAccount);
    
    createAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        EditText password = (EditText) findViewById(R.id.enterPassword);
        String enteredPassword = String.valueOf(password.getText().toString());
        EditText passwordComfirm = (EditText) findViewById(R.id.enterPassword2);
        String enteredPassword2 = String.valueOf(passwordComfirm.getText().toString());
       
        
        if (enteredPassword.equals(enteredPassword2)){
        	//this is where the save function will be
        	//need to make it so that it will append if new user 
        	//or edit if current user
        	//figure out a way to check for id thinking check if node is null
        	if(identifier.equals("")){
        		NodeList nodeList = UserAcountModel.getInstance().getDocument().getElementsByTagName("user");
        		Element root = UserAcountModel.getInstance().getRoot();
        		Element newUser = UserAcountModel.getInstance().getDocument().createElement("user");
        		Node node = nodeList.item(nodeList.getLength()-1);
        		NamedNodeMap attributes1 = node.getAttributes();
        		String ID = attributes1.getNamedItem("id").getTextContent();
        		int id = Integer.parseInt(ID) + 1;
        		String id2 = Integer.toString(id);
        		EditText first = (EditText) findViewById(R.id.FirstName);
                String First = String.valueOf(first.getText().toString());
                EditText last = (EditText) findViewById(R.id.LastName);
                String Last = String.valueOf(last.getText().toString());
                EditText user = (EditText) findViewById(R.id.enterName);
                String User = String.valueOf(user.getText().toString());
        		newUser.setAttribute("id",id2);
        		newUser.setAttribute("adminFlag","0");
        		newUser.setAttribute("username",User);
        		newUser.setAttribute("pass",enteredPassword);
        		newUser.setAttribute("firstname",First);
        		newUser.setAttribute("lastname",Last);
        		root.appendChild(newUser);
        		UserAcountModel.getInstance().SaveAccounts();
        		launchSuccessNote();
        		
        		}else{
        			Element update = (Element) currentNode;
        			EditText first = (EditText) findViewById(R.id.FirstName);
                    String First = String.valueOf(first.getText().toString());
                    EditText last = (EditText) findViewById(R.id.LastName);
                    String Last = String.valueOf(last.getText().toString());
                    EditText user = (EditText) findViewById(R.id.enterName);
                    String User = String.valueOf(user.getText().toString());
            		update.setAttribute("username",User);
            		update.setAttribute("pass",enteredPassword);
            		update.setAttribute("firstname",First);
            		update.setAttribute("lastname",Last);
            		UserAcountModel.getInstance().SaveAccounts();
            		launchSuccessNote();
            		
        		}
        	
          //errorMessage.setText("Passwords entered are the same");
          
        }
        else{
        	//change this to an alert
        	launchFailNote();
         
        }
        
        
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.manage_user_accounts, menu);
    return true;
  }
  
  public void close(){
	  Intent intent = new Intent(this, SelectUser.class);
	     startActivity(intent);
  }
  
  public void launchSuccessNote(){
	  AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	 alert.setTitle("User Data Saved");
		 LinearLayout llAlert = new LinearLayout(this);
		 llAlert.setOrientation(1); 
		 alert.setView(llAlert);
		 
		 alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
				 finish();
	         		close(); 
			 }
		 });
		alert.show();
  }
  
  public void launchFailNote(){
	  AlertDialog.Builder alert = new AlertDialog.Builder(this);
 	 alert.setTitle("The Passwords Entered Do Not Match");
		 LinearLayout llAlert = new LinearLayout(this);
		 llAlert.setOrientation(1); 
		 alert.setView(llAlert);
		 
		 alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			
			 }
		 });
		alert.show();
  }

} 