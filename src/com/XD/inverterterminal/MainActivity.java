package com.XD.inverterterminal;


import com.XD.inverterterminal.model.SciModel;
import com.XD.inverterterminal.utils.OnOff;
import com.XD.inverterterminal.utils.SendUtils;
import com.XD.inverterterminal.view.AlarmView;
import com.XD.inverterterminal.view.LoadingView;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button ButtonSciInit;
	private Button ButtonSciClose;
	private Button mButtonReset;
	private Button ButtonRun;
	private Button ButtonReverse;
	private Button ButtonStop;
	
	private Button ButtonSetfrqRam;
	private Button ButtonSetfrqProm;	
	
	private TextView TextStatus;
	private TextView TextVelocity;
	private TextView TextCurrent;
	private TextView mTextRam;
	private TextView mTextProm;
	
	private EditText mEdtextRam;
	private EditText mEdtextProm;
		
	private AlarmView mAlarmView;
	private SciModel sciModel;
	
	private RelativeLayout mLayoutMain;
//	private LoadingView mLoading;
	
	private int time = 0;
	
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
		mTextRam = (TextView)findViewById(R.id.text_ram);
		mTextProm = (TextView)findViewById(R.id.text_prom);
		
		ButtonSetfrqRam = (Button)findViewById(R.id.button_setfrq_ram);
		ButtonSetfrqProm= (Button)findViewById(R.id.button_setfrq_prom);
		
		mEdtextRam = (EditText)findViewById(R.id.edtext_ram);
		mEdtextProm = (EditText)findViewById(R.id.edtext_prom);
				
		mAlarmView = AlarmView.getInstance(this);
		sciModel = new SciModel(this);
		
		mButtonReset.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				sciModel.setSendNum(SendUtils.numReset);
				return true;
			}
		});
		
		mLayoutMain = (RelativeLayout)findViewById(R.id.layout_main);
		
//		mLoading = LoadingView.getInstance(this);
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
				mLayoutMain.setClickable(true);
			}
			//数据代码错误
			else if (action.equals(SciModel.Data_NAK)) {
				String daErr = intent.getStringExtra("dataError");
				Toast.makeText(getApplicationContext(), "数据错误为：" + daErr, Toast.LENGTH_LONG).show();
				mLayoutMain.setClickable(true);
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
				if(++time == 10) {
					time = 0;				
					Toast.makeText(getApplicationContext(), "连接超时,检查连线", Toast.LENGTH_SHORT).show();
				}
				mLayoutMain.setClickable(true);
			}
			//获取报警信息
			else if (action.equals(SciModel.Get_Alarm)) {
				int aCode = intent.getIntExtra("alarmCode", 0);
				mAlarmView.show(aCode);
			}
//			mLoading.hide();
//			mLayoutMain.setClickable(true);
			sciModel.setSendFlag();
		}
	};
	
	public void onButtonClick(View v) {
		if(OnOff.isSciOpened()) {
//			mLoading.show();
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
			case R.id.button_setfrq_ram:
				if(TextUtils.isEmpty(mEdtextRam.getText()))
					Toast.makeText(getApplicationContext(), "请输入设置频率", Toast.LENGTH_SHORT).show();
				else {
					int frqRam = Integer.parseInt(mEdtextRam.getText().toString());
					if(frqRam > 50)
						Toast.makeText(getApplicationContext(), "超过限制频率", Toast.LENGTH_SHORT).show();
					else sciModel.setFrqRam(frqRam);
				}
				break;
			case R.id.button_setfrq_prom:
				if(TextUtils.isEmpty(mEdtextProm.getText()))
					Toast.makeText(getApplicationContext(), "请输入设置频率", Toast.LENGTH_SHORT).show();
				else {
					int frqProm = Integer.parseInt(mEdtextProm.getText().toString());
					if(frqProm > 50)
						Toast.makeText(getApplicationContext(), "超过限制频率", Toast.LENGTH_SHORT).show();
					else sciModel.setFrqRam(frqProm);
				}
				break;
			case R.id.imgbtn_up:
				sciModel.frqUp();
				break;
			case R.id.imgbtn_down:
				sciModel.frqDown();
				break;
			}
			mLayoutMain.setClickable(false);
		}
		else Toast.makeText(this, "请先开启串口",
				Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * 串口连接函数
	 */
	public void onSciClick(View view)
	{
		switch(view.getId()) {
		case R.id.Sci_init:			
			open();
			break;
		case R.id.Sci_close:
			close();
			break;
		}
	}
	
	/*************开启关闭的按键处理****************/
	private void open() {
		if(OnOff.isSciOpened())
			Toast.makeText(this, "串口已开启",
					Toast.LENGTH_SHORT).show();
		else {
			registerBroadcast();
			sciModel.openSci();			
		}
	}
	
	private void close() {
		if(OnOff.isSciOpened()) {
			unregisterReceiver(mBroadcastReceiver);
			sciModel.closeSci();
		}
	}
	
	public void onBackPressed() {
		close();
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
