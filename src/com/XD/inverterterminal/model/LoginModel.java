package com.XD.inverterterminal.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class LoginModel {

	private static final String password = "z";
	
	// login broadcast msgs
	public static final String LOGIN_FINISHED = "login_finished";
	public static final String LOGIN_FAILED = "login_failed";
	public static final String WRONG_PASSWORD = "wrong_password";
		
	public boolean isLogedin;

	private static LoginModel instance;
	

	private SharedPreferences.Editor editor;
	private static Context mContext;
	
	public static synchronized LoginModel getInstance(Context context) {
		// TODO Auto-generated method stub
		if(instance == null)  
		{
			instance = new LoginModel();
			mContext = context;
			instance.load();
		}
        return instance;
	}
		
	private void load() {
		isLogedin = false;
		
	}
	
	public void login(String pwd) {
		if (password == null) {
			mContext.sendBroadcast(new Intent(LOGIN_FAILED));
		} else if (pwd.equals(password)) {
			isLogedin = true;
			mContext.sendBroadcast(new Intent(LOGIN_FINISHED));
		} else mContext.sendBroadcast(new Intent(WRONG_PASSWORD));
		
	}
	

}
