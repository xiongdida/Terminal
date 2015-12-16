package com.XD.inverterterminal;


import com.XD.inverterterminal.model.LoginModel;
import com.XD.inverterterminal.model.SciModel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity{
	
	private LoginModel inModel;
	private SciModel sModel;
//	private LoadingView mLoading;
//	private EditText userName;
	private EditText password;
	private SharedPreferences.Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		inModel = LoginModel.getInstance(this);
		sModel = SciModel.getInstance(this);
		
		setTitle(R.string.login);
		setContentView(R.layout.activity_login);
	
		password = (EditText) findViewById(R.id.password_edit);
		
		sModel.getPara();
//		mLoading = new LoadingView(this);
	}
	
//	private void getPara() {
//		// TODO Auto-generated method stub
//		if(sModel.isSciOpened()) {
//			SharedPreferences sPreferences = getSharedPreferences("para_info", 0);
//			editor = sPreferences.edit();
//			
//			
//		}
//	}

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
