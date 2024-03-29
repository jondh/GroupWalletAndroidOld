package com.whereone.groupWallet.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.jondh.groupWallet.DBhttpRequest;
import com.whereone.groupWallet.controllers.NewUser.NewUserListener;
import com.whereone.groupwalletmodules.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final DBhttpRequest httpRequest = new DBhttpRequest();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	NewUser newUser = new NewUser(httpRequest, "jondh", 0, "jondh@ymail.com", "lopol", "GroupWallet");
        	   	newUser.setNewUserListener(new NewUserListener(){
        	   		@Override
        	   		public void newUserComplete(String result){
        	   			if(result == "success"){
        	   				Log.i("New User", result);
        	   			}
        	   			else{
        	   				Log.i("New User", result);
        	   			}
        	   		}
        	   		@Override
        	   		public void newUserCancelled(){
        	   			Log.i("New User", "cancelled");
        	   		}
        	   	});
        	   	newUser.execute();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
