/*
 * MainActivity.java
 * The first page of the application.
 * This page takes in a user's username and password,
 * then authorizes the user and continues to the main menu
 */

package com.nomenipsum.famobileinspection;


import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;



public class MainActivity extends Activity {
	Button btnLogin;					// The button that submits login credentials
	EditText etUsername, 				// The EditText that takes in the user's username
	etPassword;							// The EditText that takes in the user's password
	CheckBox saveUser;                  // The check box used to choose to save user and pass
	boolean checked;                    // boolean value indicating wether or not the checkbox is checked
	String username = "username";
	String password = "password";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
		
		// Obtain UI elements from the view by ID
		btnLogin = (Button) findViewById(R.id.btnLogin);
		etUsername = (EditText)findViewById(R.id.etUsername);
		etPassword = (EditText)findViewById(R.id.etPassword);
		saveUser = (CheckBox)findViewById(R.id.saveUser);
		
		//check to see if there is a username and password stored
		
		String Astatus = prfs.getString("Authentication_STATUS","");
		if (Astatus.equals("true")){
			String USER = prfs.getString("Authentication_USER","");
			etUsername.setText(USER);
			String PASS = prfs.getString("Authentication_PASS","");
			etPassword.setText(PASS);
			saveUser.setChecked(true);
		
			}else{
				saveUser.setChecked(false);
			}
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** 
	 * Called when login button is clicked is clicked
	 * Sends user credentials to FA and continues to next screen if approved
	 * @throws IOException 
	 */
	public void OnClickLogin(View v) throws IOException	{
		android.util.Log.d(v.toString(), "Login button pressed");
		
		
		if (etUsername.getText() != null)
			username = etUsername.getText().toString();
		
		if (etPassword.getText() != null)
			password = etPassword.getText().toString();
		
		
		
		//check to see if they want to save
		SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
		if (checked==true){
			
			SharedPreferences.Editor   editor = prfs.edit();
			editor.putString("Authentication_USER",etUsername.getText().toString());
			editor.putString("Authentication_PASS",etPassword.getText().toString());
			editor.putString("Authentication_STATUS", "true");
			editor.commit();
		}else if(checked==false){
			SharedPreferences.Editor   editor = prfs.edit();
			editor.putString("Authentication_USER","");
			editor.putString("Authentication_PASS","");
			editor.putString("Authentication_STATUS", "false");
			editor.commit();
		}
		
		
		// Check for valid username password combination and show 
		// alert box if invalid
		if (!(username.equals("user") && password.equals("pass")) && false)	{
		    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		    alertDialog.setTitle("Invalid username and password combination");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		         etUsername.setText("");
		         etPassword.setText("");
		   
		      } }); 
		    alertDialog.show();
			
		    return;
		}
				
		Intent intent = new Intent(this, MainMenuActivity.class);
	    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", username);
	    startActivity(intent);

	}
	

		//check to see if the check box is selected
			
			public void onCheckBoxClicked(View view){
			//is it checked?
			if(saveUser.isChecked()){
				checked = true;
			}else{
				checked = false;
			}
			}
			
		
	
}

