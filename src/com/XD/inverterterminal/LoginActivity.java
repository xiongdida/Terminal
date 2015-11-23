package com.XD.inverterterminal;


import com.XD.inverterterminal.model.LoginModel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	private LoginModel inModel;
//	private LoadingView mLoading;
//	private EditText userName;
	private EditText password;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

//		getSupportActionBar().setDisplayShowHomeEnabled(false);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		inModel = LoginModel.getInstance(this);
		
		
		setTitle(R.string.login);
		setContentView(R.layout.setting_login);
	
		password = (EditText) findViewById(R.id.password_edit);
		
//		mLoading = new LoadingView(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		try {
			IntentFilter filter = new IntentFilter();
			filter.addAction(LoginModel.LOGIN_FINISHED);
			filter.addAction(LoginModel.LOGIN_FAILED);
			filter.addAction(LoginModel.WRONG_PASSWORD);
			registerReceiver(mBoradcastReceiver, filter);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void login(View v)
	{
		String pwd = password.getText().toString();
		
		if (pwd.matches("")) {
			Toast.makeText(getApplicationContext(), "ÇëÊäÈëÃÜÂë", Toast.LENGTH_LONG).show();
			return;
		}
//		mLoading.show("ÕýÔÚµÇÂ¼...");
		inModel.login(pwd);
	}
	
	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			mLoading.hide();
			String action = (String) intent.getAction();
			if (action.equals(LoginModel.LOGIN_FINISHED)) {
				
				Toast.makeText(context, "µÇÂ¼³É¹¦", Toast.LENGTH_LONG).show();
				Handler handler = new Handler(getMainLooper());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
						finish();
					}
				}, 1000);
			} else if (action.equals(LoginModel.LOGIN_FAILED)) {
				Toast.makeText(context, "µÇÂ¼Ê§°Ü", Toast.LENGTH_LONG).show();
			}
			else if (action.equals(LoginModel.WRONG_PASSWORD)) {
				Toast.makeText(context, "ÕËºÅ»òÃÜÂë²»ÕýÈ·", Toast.LENGTH_LONG).show();
			}
		}

	};
}
