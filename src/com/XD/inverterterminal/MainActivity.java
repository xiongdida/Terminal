package com.XD.inverterterminal;


import com.XD.inverterterminal.model.SciModel;
import com.XD.inverterterminal.utils.RecvUtils;
import com.XD.inverterterminal.utils.SendUtils;
import com.XD.inverterterminal.view.AlarmView;
import com.XD.inverterterminal.view.LoadingView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button ButtonSciInit;
	Button ButtonSciClose;
	Button mButtonReset;
	Button ButtonRun;
	Button ButtonReverse;
	Button ButtonStop;
	
	TextView TextStatus;
	TextView TextVelocity;
	TextView TextCurrent;
	
	Button ButtonSetfrqRam;
	Button ButtonGetfrqRam;
	Button ButtonSetfrqProm;
	Button ButtonGetfrqProm;
	
	EditText mEdtextRam;
	EditText mEdtextProm;
	
	TextView mTextRam;
	TextView mTextProm;
	
	private AlarmView mAlarmView;
	private SciModel sciModel;
	
	byte[] sciRevBuf;							// 串口接收数据
	
	private LoadingView mLoading;

//	/*
//	 * 与串口子线程通信函数
//	 */
//	@SuppressLint("HandlerLeak")
//	private Handler sciHandler = new Handler()
//	{
//		@Override
//		public void handleMessage(Message msg)
//		{
//			if (msg.what == 0x55)				// 等于0x55说明串口权限不对
//			{
//				Toast.makeText(MainActivity.this, "尚未获取串口权限",
//						Toast.LENGTH_SHORT).show();
//				sciOpened = false;
//			}
//			else if (msg.obj != null)			// 正常接收数据
//			{
//				Toast.makeText(MainActivity.this,
//						getResources().getString(R.string.openSCI_sucssess),
//						Toast.LENGTH_SHORT).show();
//				sciRevBuf = ((String) msg.obj).getBytes();
//				recv(sciRevBuf);
//			}
//			else
//			{
//				Toast.makeText(MainActivity.this, "本地串口不存在", Toast.LENGTH_SHORT)
//						.show();
//				sciOpened = false;
//			}
//		}
//	};

//	protected void recv(byte[] input) {
//		// TODO Auto-generated method stub
//		if(input[0] == RecvUtils.ACK)
//			Toast.makeText(this, "数据写入成功", Toast.LENGTH_SHORT).show();
//		else if (input[0] == RecvUtils.NAK)
//			Toast.makeText(this, RecvUtils.getDataErr(input), Toast.LENGTH_SHORT).show();
//		else if (input[0] == RecvUtils.STX)
//			s = RecvUtils.getStatus(input);
//	}
//	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButtonSciInit = (Button)findViewById(R.id.Sci_init);
		ButtonSciClose = (Button)findViewById(R.id.Sci_close);
		mButtonReset = (Button)findViewById(R.id.btn_reset);
		
		ButtonRun = (Button)findViewById(R.id.button_run);
		ButtonReverse = (Button)findViewById(R.id.button_reverse);
		ButtonStop = (Button)findViewById(R.id.button_stop);
		
		TextStatus = (TextView)findViewById(R.id.text_status);
		TextVelocity = (TextView)findViewById(R.id.text_velocity);
		TextCurrent = (TextView)findViewById(R.id.text_current);
		
		ButtonSetfrqRam = (Button)findViewById(R.id.button_setfrq_ram);
		ButtonGetfrqRam= (Button)findViewById(R.id.button_getfrq_ram);
		ButtonSetfrqProm= (Button)findViewById(R.id.button_setfrq_prom);
		ButtonGetfrqProm= (Button)findViewById(R.id.button_getfrq_prom);
		
		mEdtextRam = (EditText)findViewById(R.id.edtext_ram);
		mEdtextProm = (EditText)findViewById(R.id.edtext_prom);
		
		mTextRam = (TextView)findViewById(R.id.text_ram);
		mTextProm = (TextView)findViewById(R.id.text_prom);
		
		mAlarmView = AlarmView.getInstance(this);
		sciModel = new SciModel(this);
		
		mButtonReset.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				sciModel.reset();
				return true;
			}
		});
		mLoading = new LoadingView(this);
	}

	private void registerBroadcast() {
		try {
			IntentFilter filter = new IntentFilter();

			filter.addAction(SciModel.Data_ACK);
			filter.addAction(SciModel.Data_NAK);
			
			filter.addAction(SciModel.Current_Refresh);
			filter.addAction(SciModel.OutFrq_Refresh);
			filter.addAction(SciModel.Status_Refresh);
			
			filter.addAction(SciModel.Frq_Ram);
			filter.addAction(SciModel.Frq_Prom);
			
			filter.addAction(SciModel.Alarm);
			filter.addAction(SciModel.Conn_Error);
			filter.addAction(SciModel.Get_Alarm);
			registerReceiver(mBroadcastReceiver, filter);
		} catch (Exception e) {
			// mBroadcastReceiver register failed
			e.printStackTrace();
		}
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			//数据代码正确
			if (action.equals(SciModel.Data_ACK)) {
				Toast.makeText(getApplicationContext(), "操作正确", Toast.LENGTH_SHORT).show();
			}
			//数据代码错误
			else if (action.equals(SciModel.Data_NAK)) {
				String daErr = intent.getStringExtra("dataError");
				Toast.makeText(getApplicationContext(), "数据错误为：" + daErr, Toast.LENGTH_LONG).show();
			}
			//变频器状态刷新
			else if (action.equals(SciModel.Status_Refresh)) {
				TextStatus.setText(intent.getStringExtra("status"));
			} 
			//电流刷新
			else if (action.equals(SciModel.Current_Refresh)) {
				TextCurrent.setText("" + (double)intent.getIntExtra("current", 0)/100.0);
			}
			//输出频率刷新
			else if (action.equals(SciModel.OutFrq_Refresh)) {
				TextVelocity.setText("" + (double)intent.getIntExtra("outFrq", 0)/100.0);
			}
			//RAM频率
			else if (action.equals(SciModel.Frq_Ram)) {
				mTextRam.setText("" + (double)intent.getIntExtra("frqRam", 0)/100.0);
			}
			//E2PROM频率
			else if (action.equals(SciModel.Frq_Prom)) {
				mTextProm.setText("" + (double)intent.getIntExtra("frqProm", 0)/100.0);
			}
			//报警状态
			else if (action.equals(SciModel.Alarm)) {
				TextStatus.setText(intent.getStringExtra("status"));
				sciModel.setSendNum(SendUtils.numGetAlarm);
			}
			//连接错误
			else if (action.equals(SciModel.Conn_Error)) {
				Toast.makeText(getApplicationContext(), "连接超时,检查连线", Toast.LENGTH_SHORT).show();
			}
			//获取报警信息
			else if (action.equals(SciModel.Get_Alarm)) {
				int aCode = intent.getIntExtra("alarmCode", 0);
				mAlarmView.show(aCode);
			}
			mLoading.hide();
			sciModel.setSendFlag();
		}
	};
	
	public void onButtonClick(View v) {
		switch (v.getId()) {
		case R.id.button_run:
			sciModel.setSendNum(SendUtils.numRun);
			break;
		case R.id.button_reverse:
			sciModel.setSendNum(SendUtils.numReverse);
			break;
		case R.id.button_stop:
			sciModel.setSendNum(SendUtils.numStop);
			break;
		case R.id.button_getfrq_ram:
			sciModel.setSendNum(SendUtils.numGetRam);
			break;
		case R.id.button_getfrq_prom:
			sciModel.setSendNum(SendUtils.numGetProm);
			break;
		case R.id.button_setfrq_ram:
			if(TextUtils.isEmpty(mEdtextRam.getText()))
				Toast.makeText(getApplicationContext(), "请输入设置频率", Toast.LENGTH_SHORT).show();
			else {
				int frqRam = Integer.parseInt(mEdtextRam.getText().toString());
				if(frqRam > 50)
					Toast.makeText(getApplicationContext(), "超过限制频率", Toast.LENGTH_SHORT).show();
				else sciModel.SetFrqRam(frqRam);
			}
			break;
		case R.id.button_setfrq_prom:
			if(TextUtils.isEmpty(mEdtextProm.getText()))
				Toast.makeText(getApplicationContext(), "请输入设置频率", Toast.LENGTH_SHORT).show();
			else {
				int frqProm = Integer.parseInt(mEdtextProm.getText().toString());
				if(frqProm > 50)
					Toast.makeText(getApplicationContext(), "超过限制频率", Toast.LENGTH_SHORT).show();
				else sciModel.SetFrqRam(frqProm);
			}
			break;
		}
		mLoading.show(); 
	}
	
	/*
	 * 串口连接函数
	 */
	public void onSciClick(View view)
	{
		switch(view.getId()) {
		case R.id.Sci_init:
			registerBroadcast();
			sciModel.openSci();
			break;
		case R.id.Sci_close:
			sciModel.closeSci();
			break;
		}
	}
	
	public void onBackPressed() {
		sciModel.closeSci();
		unregisterReceiver(mBroadcastReceiver);
		super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.alarm_item:
			mAlarmView.show(1);
			break;
		}
		return true;
	}
}
