package com.whereone.groupWallet.functions;

import java.util.ArrayList;

import com.jondh.groupWallet.User;

public class GroupWalletFunctions {
	ArrayList<User> userList;
	
	public GroupWalletFunctions(ArrayList<User> _userList){
		userList = _userList;
	}
	
	public String userIDtoName(Integer _userID){
		for(int j = 0; j < userList.size(); j++){
			if(userList.get(j).getUserID() == _userID){
				return userList.get(j).getName();
			}
		}
		return "";
	}
}
