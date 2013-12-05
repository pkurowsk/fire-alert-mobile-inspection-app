package com.nomenipsum.famobileinspection;

import android.content.Intent;

public class ClientInfoController {
	
	ClientInfoPage _view;
	
	public ClientInfoController(ClientInfoPage v)	{
		_view = v;
		
		Intent intent = _view.getIntent();
	    String message = intent.getStringExtra("com.nomenipsum.famobileinspection.MESSAGE");
	    
	    _view.DisplayPageDetails(message);
	}
	
	public void selectServiceAddress()	{
		_view.UpdateServiceAddress();
		_view.displayLocationElements();
	}
	
	public void openEquipmentPage(String message)	{
		Intent intent = new Intent(_view.getBaseContext(), EquipmentView.class);
	    intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", message);
		_view.startActivityForResult(intent, 1);
	}

}
