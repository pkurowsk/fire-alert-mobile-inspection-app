package com.nomenipsum.famobileinspection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainMenuActivity extends Activity {
	
	TextView tvMainMenuTitle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
		tvMainMenuTitle = (TextView)findViewById(R.id.tvMainMenuTitle);
		tvMainMenuTitle.setText("Welcome, " + message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public void OnClickClients(View v)	{
		
		Intent intent = new Intent(this, ClientsPage.class);
	    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", "message");
	    startActivity(intent);
	
	}
	
	public void OnClickScan(View v)	{
		Intent intent = new Intent(this, ScanActivity.class);
	    startActivity(intent);
	}
	
	 public void OnClickManageAccounts(View v){
		     Intent intent = new Intent(this, ManageUserAccounts.class);
		     startActivity(intent);
		   } 
}
