package com.XD.inverterterminal;


import com.XD.inverterterminal.model.LoginModel;
import com.XD.inverterterminal.model.SciModel;
import com.XD.inverterterminal.utils.SendUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private int time = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	

		inModel = LoginModel.getInstance(this);
		sModel = SciModel.getInstance(this);
		
		setTitle(R.string.login);
		setContentView(R.layout.activity_login);
	
		password = (EditText) findViewById(R.id.password_edit);
		
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
		sModel.setHandler(handler);
		sModel.getPara();
	}
	
	public void login(View v)
	{
		String pwd = password.getText().toString();
		
		if (pwd.matches("")) {
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_LONG).show();
			return;
		}
//		mLoading.show("正在登录...");
		inModel.login(pwd);
	}
	
	private BroadcastReceiver mBoradcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
//			mLoading.hide();
			String action = (String) intent.getAction();
			if (action.equals(LoginModel.LOGIN_FINISHED)) {
				
				Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
				Handler handler = new Handler(getMainLooper());
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(LoginActivity.this, SettingActivity.class);
						startActivity(intent);
						LoginActivity.this.finish();
						finish();
					}
				}, 800);
			} else if (action.equals(LoginModel.LOGIN_FAILED)) {
				Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();
			}
			else if (action.equals(LoginModel.WRONG_PASSWORD)) {
				Toast.makeText(context, "账号或密码不正确", Toast.LENGTH_LONG).show();
			}
		}

	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg)
		{
			if(sModel.isSciOpened()) {
				switch (msg.what)
				{
				case SciModel.Data_NAK:
					String daErr = "";
					if (msg.obj != null)
						daErr = (String)msg.obj;
					Toast.makeText(getApplicationContext(), "数据错误为：" + daErr, Toast.LENGTH_LONG).show();
					break;
				case SciModel.Conn_Error:
					if(++time == 10) {
						time = 0;				
						Toast.makeText(getApplicationContext(), "连接超时,检查连线", Toast.LENGTH_SHORT).show();
					}
					break;
				}
				sModel.closeParaThread();
			}
		}
	};
}
