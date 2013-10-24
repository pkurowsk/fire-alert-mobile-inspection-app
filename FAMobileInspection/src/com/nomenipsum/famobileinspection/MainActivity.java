/*
 * MainActivity.java
 * The first page of the application.
 * This page takes in a user's username and password,
 * then authorizes the user and continues to the main menu
 */

package com.nomenipsum.famobileinspection;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	Button btnLogin;					// The button that submits login credentials
	EditText etUsername, 				// The EditText that takes in the user's username
	etPassword;							// The EditText that takes in the user's password
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Obtain UI elements from the view by ID
		btnLogin = (Button) findViewById(R.id.btnLogin);
		etUsername = (EditText)findViewById(R.id.etUsername);
		etPassword = (EditText)findViewById(R.id.etPassword);
		
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
	 */
	public void OnClickLogin(View v)	{
		android.util.Log.d(v.toString(), "Login button pressed");
		
		String username = "username";
		String password = "password";
		if (etUsername.getText() != null)
			username = etUsername.getText().toString();
		if (etPassword.getText() != null)
			password = etPassword.getText().toString();
		
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
	

}
