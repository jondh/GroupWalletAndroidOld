/**
 *  Author: Jonathan Harrison
 *  Date: 9/21/13
 *  Description: This class is used to get the money owe / owed from one user to another.
 * 				It uses AsyncTask so it will need to executed with the following list of parameters:
 * 				String userID, String userID, String walletID
 *  Input: An instance of DBhttpRequest
 *		   A User from the class User or Profile (The logged in user)
 *  	   A User from the class User (for scope "user")
 *  	   A Wallet from class Wallet (for scope "wallet")
 *  	   A String for the scope ("user", "wallet", "all")
 *  Output: Double amount with Double oweAmount and owedAmounts outputting in completed listener method
 *  Implementation:
 *  
	   	GetAmounts getAmounts = new GetAmounts(DBhttpRequest, User me, User other, Wallet current, String scope);
	   	getAmounts.setGetAmountsListener(new GetAmountsListener(){
	   		@Override
	   		public void getAmountsComplete(Double _amount, Double _amountOwe, Double _amountOwed);
	   		
	   		}
	   		@Override
	   		public void getAmountsCancelled(){
	   		
	   		}
	   	});
	   	getAmounts.execute();
 * 
 * 
 *  SERVER input (name value pairs): 
 *  								 "userID" -> String user ID
 * 									 "otherUID" -> the other users UID (for scope = "user")
 * 									 "walletID" -> String wallet ID (for scope = "wallet")
 * 									 "scope" -> whether to get amounts for "all", a "user", or a "wallet"
 * 
 *  SERVER output: returns a JSON array and expects the following name value pairs:
 *  				"amountOwe"  -> amount user owes (Double)
 *  				"amountOwed" -> amount user is owed (Double)
 */

package com.whereone.groupWallet.controllers;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.jondh.groupWallet.User;
import com.jondh.groupWallet.Wallet;

import android.os.AsyncTask;

public class GetAmounts extends AsyncTask<Void, Void, Double>{
	private DBhttpRequest httpRequest;
	private GetAmountsListener listener;
	private User user;
	private User otherUser;
	private Wallet wallet;
	private String scope;
	private Double amountOwe = 0.0, amountOwed = 0.0;
	
	GetAmounts(DBhttpRequest _httpRequest, User _user, User _otherUser, Wallet _wallet, String _scope){
		httpRequest = _httpRequest;
		user = _user;
		otherUser = _otherUser;
		wallet = _wallet;
		scope = _scope; 
	}
	
	public void setGetAmountsListener(GetAmountsListener _listener) {
        this.listener = _listener;
    }
	
	@Override
	protected Double doInBackground(Void... arg0) {
		String url = "http://jondh.com/GroupWallet/android/getAmounts.php";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userID",user.getUserID().toString()));
		if(scope == "wallet"){
			nameValuePairs.add(new BasicNameValuePair("walletID",wallet.getID().toString()));
			nameValuePairs.add(new BasicNameValuePair("scope","wallet")); 
		}
		else if(scope == "all"){
			nameValuePairs.add(new BasicNameValuePair("scope","all")); 
		}
		else if(scope == "user"){
			nameValuePairs.add(new BasicNameValuePair("otherUID",otherUser.getUserID().toString()));
			nameValuePairs.add(new BasicNameValuePair("scope","user")); 
		}
		String result = httpRequest.sendRequest(nameValuePairs, url);
		try {
			JSONObject jObj = new JSONObject(result);
			amountOwe = jObj.getDouble("amountOwe");
			amountOwed = jObj.getDouble("amountOwed");
			return amountOwed - amountOwe;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0.0;
		}
	}
	
	@Override
	protected void onPostExecute(final Double result) {
		listener.getAmountsComplete(result, amountOwe, amountOwed);
	}

	@Override
	protected void onCancelled() {
		listener.getAmountsCancelled();
	}
	
	public interface GetAmountsListener{
		public void getAmountsComplete(Double _amount, Double _amountOwe, Double _amountOwed);
		public void getAmountsCancelled();
	}
}

