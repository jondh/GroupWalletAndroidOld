/** 
 *  Author: Jonathan Harrison
 *  Date: 9/3/2013
 *  Name: NewUser
 *  Description: Adds a new user in the master whereone database.
 *  			 The onPost and onCancelled functions can be implemented through the listener implementation.
 *  Input: An instance of DBhttpRequest
 *		   A String userName
 *  	   A String email
 *  	   A String password
 *  	   A String indicating what app this account is created for (ie "GroupWallet" or "Pace")
 *  Output: String result -> "success" for success or some other string indicating what kind of failure
 *  Implementation:
 *  
	   	NewUser newUser = new NewUser(DBhttpRequest, userName, email, password, app);
	   	newUser.setNewUserListener(new NewUserListener(){
	   		@Override
	   		public void newUserComplete(String result){
	   		
	   		}
	   		@Override
	   		public void newUserCancelled(){
	   		
	   		}
	   	});
	   	newUser.execute();
 * 
 *  SERVER input (name value pairs): "userName" -> String user name
 * 									 "fbID"     -> Integer facebook ID
 * 									 "email"    -> String email address
 * 									 "password" -> String password
 * 									 "app"      -> Which app this account is for "GroupWallet"
 * 
 *  SERVER output: String result -> "success" to indicate successful user creation, anything else means failure
 */

package com.whereone.groupwalletmodules;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

import com.jondh.groupWallet.DBhttpRequest;

public class NewUser extends AsyncTask<Void, Void, String>{
	private DBhttpRequest httpRequest;
	private NewUserListener listener;
	private String userName;
	private Integer fbID;
	private String email;
	private String password;
	private String app;
	
	//Constructor
	NewUser(DBhttpRequest _httpRequest, String _userName, Integer _fbID, String _email, String _password, String _app){
		httpRequest = _httpRequest;
		userName = _userName;
		fbID = _fbID;
		email = _email;
		password = _password;
		app = _app;
	}
	
	//Listener for completion
	public void setNewUserListener(NewUserListener _listener){
		listener = _listener;
	}

	//Task to run in background. Connects to GroupWallet Server to add a new user and returns data from it.
	@Override
	protected String doInBackground(Void... arg0) {
		String url = "http://jondh.com/GroupWallet/android/newUser.php";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userName", userName));
		nameValuePairs.add(new BasicNameValuePair("fbID", fbID.toString()));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("app", app));
		String result = httpRequest.sendRequest(nameValuePairs, url);
		
		return result;
	}
	
	//What happens when the httpRequest finishes successfully.
	@Override
	protected void onPostExecute(final String result) {
		listener.newUserComplete(result);
	}

	//What happens when the httpRequest is cancelled.
	@Override
	protected void onCancelled() {
		listener.newUserCancelled();
	}
	
	//Listener Interface to implement onPostExecute and onCancelled.
	public interface NewUserListener{
		public void newUserComplete(String result);
		public void newUserCancelled();
	}

}
