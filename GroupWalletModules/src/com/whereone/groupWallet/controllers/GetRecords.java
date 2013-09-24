/**
 *  Author: Jonathan Harrison
 *  Date: 9/24/13
 *  Description: This class is used to get all the records that a user either owes or is owed
 * 				 for a specific wallet.
 * 				 It uses AsyncTask so it will need to executed with the following list of parameters:
 * 				 String userID, String walletID, String owe/owed
 *  Input: An instance of DBhttpRequest
 *		   An instance of GroupWalletFunctions
 *		   A User (the logged in user)
 *		   A Wallet (the current wallet)
 *		   A String type to indicate what type of records to get ("owe" or "owed")
 *
 *  Output: The onPostExecute output for this class is an ArrayList<Record> containing 
		    the resulting list of records for specified type.
 *  Implementation:
 *  
	   	GetRecords getRecords = new GetRecords(DBhttpRequest, GroupWalletFunctions, User, Wallet, String type);
	   	getRecords.setGetRecordsListener(new GetRecordsListener(){
	   		@Override
	   		public void getRecordsPreExecute(){
	   		
	   		}
	   		
	   		@Override
	   		public void getRecordsComplete(ArrayList<Record> result){
	   		
	   		}
	   		@Override
	   		public void getRecordsCancelled(){
	   		
	   		}
	   	});
	   	getRecords.execute();
 * 
 * 	SERVER input (name value pairs):
 * 					"userID"   -> String user ID
 * 					"walletID" -> String wallet ID (for scope = "wallet")
 * 					"type"     -> record type -> owe/owed (String)
 * 
 *  SERVER output: returns a JSON array and expects the following name value pairs:
 *  				"otherUID" -> user ID in the record (int)
 *  				"amount"   -> amount of the record (Double)
 *  				"comments" -> comments of the record (String)
 */

package com.whereone.groupWallet.controllers;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.jondh.groupWallet.Record;
import com.jondh.groupWallet.User;
import com.jondh.groupWallet.Wallet;
import com.whereone.groupWallet.functions.GroupWalletFunctions;

public class GetRecords extends AsyncTask<String, Void, ArrayList<Record>>{
	private DBhttpRequest httpRequest;
	private GroupWalletFunctions function;
	private GetRecordsListener listener;
	private User user;
	private Wallet wallet;
	private String type;
	
	GetRecords(DBhttpRequest _httpRequest, GroupWalletFunctions _function, User _user, Wallet _wallet, String _type){
		httpRequest = _httpRequest;
		function = _function;
		user = _user;
		wallet = _wallet;
		type = _type;
	}
	
	public void setRecordListener(GetRecordsListener _listener) {
        this.listener = _listener;
    }
	
	@Override
	protected void onPreExecute(){ 
		listener.getRecordsPreExecute();
	}
	
	@Override
	protected ArrayList<Record> doInBackground(String... arg0) {
		String url = "http://jondh.com/GroupWallet/android/getRecords.php";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("userID",user.getUserID().toString()));
		nameValuePairs.add(new BasicNameValuePair("walletID", wallet.getID().toString()));
		nameValuePairs.add(new BasicNameValuePair("type",type));
		String result = httpRequest.sendRequest(nameValuePairs, url);
		
		ArrayList<Record> records = new ArrayList<Record>();
		try {
			JSONArray jArr = new JSONArray(result);
			for(int i = 0; i < jArr.length(); i++){
				JSONObject jObj = jArr.getJSONObject(i);
				Record curRecord = new Record(function.userIDtoName(jObj.getInt("otherUID")),
						(int) jObj.getDouble("amount"),
						jObj.getString("comments"));
				records.add(curRecord);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return records;
	}
	
	@Override
	protected void onPostExecute(final ArrayList<Record> result) {
		listener.getRecordsComplete(result);
	}

	@Override
	protected void onCancelled() {
		listener.getRecordsCancelled();
	}
	
	public interface GetRecordsListener{
		public void getRecordsPreExecute();
		public void getRecordsComplete(ArrayList<Record> _records);
		public void getRecordsCancelled();
	}
}
