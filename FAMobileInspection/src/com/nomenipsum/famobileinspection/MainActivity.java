/*
 * MainActivity.java
 * The first page of the application.
 * This page takes in a user's username and password,
 * then authorizes the user and continues to the main menu
 */

package com.nomenipsum.famobileinspection;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class MainActivity extends Activity {
	CheckBox saveUser;   
	
	AuthenticationControl _controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		saveUser = (CheckBox)findViewById(R.id.saveUser);
		
		_controller = new AuthenticationControl(this);
		
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
	public void OnClickLogin(View v)	{	
		_controller.login();

	}
	

	//check to see if the check box is selected
	public void onCheckBoxClicked(View view){
		_controller.check(saveUser.isChecked());
	}
			
		
	
}

