/**
 *  Author: Jonathan Harrison
 *  Date: 9/21/13
 *  Description: This class is used to log in to the where one server
 *  Input: An instance of DBhttpRequest
 *		   A String for user name
 *		   A String for password
 *  Output: Integer amount for the logged in userID -> 0 or negative if log in failed
 *  Implementation:
 *  
	   	LogIn logIn = new LogIn(DBhttpRequest, String userName, String password);
	   	logIn.setLogInListener(new LogInListener(){
	   		@Override
	   		public void logInComplete(Integer _userID);
	   		
	   		}
	   		@Override
	   		public void logInCancelled(){
	   		
	   		}
	   	});
	   	logIn.execute();
 * 
 * 
 *  SERVER input (name value pairs): "userName" -> String for user name
 * 									 "password" -> String for password
 * 
 *  SERVER output: returns a JSON object with the following name value pair:
 *  				"userID" -> Integer for userID (0 or negative for failure)
 */

package com.whereone;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.whereone.groupWallet.controllers.DBhttpRequest;

public class LogIn extends AsyncTask<Void, Void, Integer> {
	private DBhttpRequest httpRequest;
	private LogInListener listener;
	private String userName;
	private String password;
	
	LogIn(DBhttpRequest _httpRequest, String _userName, String _password){
		httpRequest = _httpRequest;
		userName = _userName;
		password = _password;
	}
	
	public void setLogInListener(LogInListener _listener) {
        this.listener = _listener;
    }
	
	@Override
	protected Integer doInBackground(Void... arg0) {
		String url = "http://jondh.com/GroupWallet/android/login.php";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userName",userName));
		nameValuePairs.add(new BasicNameValuePair("password",password));
		String result = httpRequest.sendRequest(nameValuePairs, url);
		
		try {
			JSONObject jObj = new JSONObject(result);
			return jObj.getInt("userID");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		listener.logInComplete(result);
	}

	@Override
	protected void onCancelled() {
		listener.logInCancelled();
	}
	
	public interface LogInListener{
		public void logInComplete(Integer _userID);
		public void logInCancelled();
	}
}
