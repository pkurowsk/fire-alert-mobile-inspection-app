package com.nomenipsum.famobileinspection;

import org.w3c.dom.Node;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class ScanActivity extends Activity {
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	
	private DataReceiver dataScanner = new DataReceiver();
	
	private TextView tvScanProgress;
	
	private EditText etCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scan, menu);
		return true;
	}
	

    @Override
	protected void onResume() {
    	registerScanner();
    	initialComponent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}
	
	private void initialComponent() {
		tvScanProgress = (TextView)findViewById(R.id.tvScanProgress);
		
		etCode = (EditText)findViewById(R.id.etCode);
		etCode.setOnEditorActionListener(codeEntered);
		etCode.addTextChangedListener(textWatcher);
	}
	
	OnEditorActionListener codeEntered = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (etCode.getText() != null)
				CheckEquipment(etCode.getText().toString().trim());
			return false;
		}
		
	};
	
	private void registerScanner() {
		dataScanner = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		registerReceiver(dataScanner, intentFilter);
	}
	
	private void unregisterReceiver() {
		if (dataScanner != null) unregisterReceiver(dataScanner);
	}
	
	private TextWatcher textWatcher =  new TextWatcher(){
        public void onTextChanged(CharSequence s, int start, int before, int count){}
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
    	public void afterTextChanged(Editable s){
    		//tv_getdata_from_edittext.setText("Get data from EditText : " + etCode.getText().toString());
    	}
    }; 
    
	private class DataReceiver extends BroadcastReceiver {
		String content = "";
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				Bundle bundle = new Bundle();
				bundle  = intent.getExtras();
				content = bundle.getString("CONTENT");
				tvScanProgress.setText("Got data from Scanner : " + content);
				
				CheckEquipment(content);
			}
				
		}
	}
	
	/**
	 * Searches Inspectiondata.xml for the equipment matching the ID 
	 * <p>
	 * @param id : The ID of the piece of equipment that was scanned or entered
	 */
	private void CheckEquipment(String id)	{
		
		Node equipment = InspectionReportModel.getInstance().FindEquipment(id, true);
		if (equipment == null)	{
			Toast.makeText(this, "Equipment " + id + " not found", Toast.LENGTH_SHORT).show();

			return;
		}
		
		Intent intent = new Intent(this, EquipmentView.class);
        intent.putExtra("com.nomenipsum.famobileinspection.MESSAGE", id);
        startActivity(intent);
		    
		
	}
	
}
