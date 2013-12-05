package com.nomenipsum.famobileinspection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;

public class AuthenticationControl {
	
	MainActivity _view;
	boolean checked;
	
	public AuthenticationControl(MainActivity v)	{
		_view = v;
		
		EditText etUsername = (EditText)_view.findViewById(R.id.etUsername);
		EditText etPassword = (EditText)_view.findViewById(R.id.etPassword);
		CheckBox saveUser = (CheckBox)_view.findViewById(R.id.saveUser);
		
		SharedPreferences prfs = _view.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);

		String Astatus = prfs.getString("Authentication_STATUS","");
		if (Astatus.equals("true")){
			String USER = prfs.getString("Authentication_USER","");
			etUsername.setText(USER);
			String PASS = prfs.getString("Authentication_PASS","");
			etPassword.setText(PASS);
			saveUser.setChecked(true);
		
			}
		else{
				saveUser.setChecked(false);
			}
	}
	
	public void login()	{
		EditText etUsername = (EditText)_view.findViewById(R.id.etUsername);
		EditText etPassword = (EditText)_view.findViewById(R.id.etPassword);
		
		String username = "", password = "";
		
		if (etUsername.getText() != null)
			username = etUsername.getText().toString();
		
		if (etPassword.getText() != null)
			password = etPassword.getText().toString();
		
		// Check for valid username password combination and show 
		// alert box if invalid
		if (!UserAcountModel.getInstance().verifyLogin(username, password))	{
		    AlertDialog alertDialog = new AlertDialog.Builder(_view).create();
		    alertDialog.setTitle("Invalid username and password combination");
		    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		         
		      } }); 
		    alertDialog.show();
			
		}
		else	{
			//check to see if they want to save
			SharedPreferences prfs = _view.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_WORLD_WRITEABLE);
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
				
			Intent intent = new Intent(_view, MainMenuActivity.class);
		    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", username);
		    _view.startActivity(intent);
		}
	}
	
	public void rememberLoginInfo()	{
		
	}
	
	public void check(boolean checked)	{
		this.checked = checked;
	}
	
}
