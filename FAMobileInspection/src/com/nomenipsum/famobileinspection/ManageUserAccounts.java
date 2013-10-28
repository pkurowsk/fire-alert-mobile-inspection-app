package com.nomenipsum.famobileinspection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ManageUserAccounts extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_manage_user_accounts);
    
    Button createAccount = (Button) findViewById(R.id.createAccount);
    
    createAccount.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        EditText password = (EditText) findViewById(R.id.enterPassword);
        String enteredPassword = String.valueOf(password.getText().toString());
        EditText passwordComfirm = (EditText) findViewById(R.id.enterPassword2);
        String enteredPassword2 = String.valueOf(passwordComfirm.getText().toString());
        TextView errorMessage = (TextView) findViewById(R.id.errorNotification);
        
        if (enteredPassword.equals(enteredPassword2)){
          errorMessage.setText("Passwords entered are the same");
          
        }
        else{
          errorMessage.setText("Passwords entered are not the same, re-enter passwords");
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

} 