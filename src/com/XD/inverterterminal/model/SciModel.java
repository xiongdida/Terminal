package com.XD.inverterterminal.model;

import java.io.File;
import java.io.FileDescriptor;


import com.XD.inverterterminal.R;
import com.XD.inverterterminal.serial_jni.SciClass;
import com.XD.inverterterminal.thread.RecvThread;
import com.XD.inverterterminal.thread.SendThread;
import com.XD.inverterterminal.utils.OnOff;
import com.XD.inverterterminal.utils.RecvUtils;
import com.XD.inverterterminal.utils.SendUtils;
import com.XD.inverterterminal.thread.RecvThread.OnRecvListener;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SciModel {

	public static String Data_ACK = "data_ack";
	public static String Data_NAK = "data_nak";
	
	public static String Current_Refresh = "currentRef";
	public static String OutFrq_Refresh = "outFrqRef";
	public static String Status_Refresh = "statusRef";
	
	public static String Frq_Ram = "frqRam";
	public static String Frq_Prom = "frqProm";
	
	public static String Alarm = "Alarm";
	public static String Conn_Error = "conn_error";
	public static String Get_Alarm = "get_alarm";
	
	
	public static final String SERIAL_PORT = "/dev/ttyO0";
	public static final int SERIAL_BAUD = 9600;
	
	private Context mContext;
	
	private SciClass sci;
	private FileDescriptor fd;
	
	private RecvThread sciListener;
	private SendThread connectThread;
	
	private int outFrq = 0;
	
	public SciModel(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void openSci() {
		// TODO Auto-generated method stub
		if (new File(SERIAL_PORT).exists())
		{			
			sci = SciClass.getInstance();
			fd = sci.openSerialPort(new File(SERIAL_PORT), SERIAL_BAUD,
					SciClass.OLD_CHECK);
			if(fd != null) {		
				sciListener = new RecvThread(sci, fd);
				sciListener.setOnRecvListener(new OnRecvListener() {						
					@Override
					public void OnRecv(byte[] response) {
						// TODO Auto-generated method stub
						Log.d("receiveMsg", new String(response));
						parserResponse(response);
					}

					@Override
					public void OnConnError() {
						// TODO Auto-generated method stub
						Log.d("Terminal", "conn_error");
						Intent errIntent = new Intent(SciModel.Conn_Error);
						mContext.sendBroadcast(errIntent);
					}
				});
				connectThread = new SendThread(sci, fd);
				
				OnOff.setSciOpened(true);
				Toast.makeText(mContext,
						mContext.getResources().getString(R.string.openSCI_sucssess),
						Toast.LENGTH_SHORT).show();	
				
				//发送、接收线程打开
				connectThread.open();
				sciListener.open();
			}
			else// 串口存在，打开fd=null则说明没有执行权限
			{
				Toast.makeText(mContext, "串口没有执行权限",
						Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "本地串口不存在", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void parserResponse(byte[] response) {
		// TODO Auto-generated method stub
		switch(response[0]) {
		case RecvUtils.ACK:
			Intent ackIntent = new Intent(SciModel.Data_ACK);
			mContext.sendBroadcast(ackIntent);
			break;
		case RecvUtils.NAK:
			String daErr = RecvUtils.getDataErr(response[3]);
			Intent nakIntent = new Intent(SciModel.Data_NAK);
			nakIntent.putExtra("dataError", daErr);
			mContext.sendBroadcast(nakIntent);
			break;
		case RecvUtils.STX:
			processData(connectThread.getSendNum(), response);
			break;	
		}
	}

	private void processData(int i, byte[] response) {
		// TODO Auto-generated method stub
		int sData;
		switch(i) {
		case SendUtils.numStatus:
			sData = RecvUtils.getData(response);
			if(sData >= 128) {
				Intent alarmIntent = new Intent(SciModel.Alarm);
				alarmIntent.putExtra("status", "发生警报");
				mContext.sendBroadcast(alarmIntent);
			} else {
				Intent statusIntent = new Intent(SciModel.Status_Refresh);
				statusIntent.putExtra("status", "正常工作");
				mContext.sendBroadcast(statusIntent);
			}
			break;
		case SendUtils.numOutFrq:
			sData = RecvUtils.getData(response);
			outFrq = sData/100;
			Intent velIntent = new Intent(SciModel.OutFrq_Refresh);
			velIntent.putExtra("outFrq", sData);
			mContext.sendBroadcast(velIntent);
			break;
		case SendUtils.numCurrent:
			sData = RecvUtils.getData(response);
			Intent curIntent = new Intent(SciModel.Current_Refresh);
			curIntent.putExtra("current", sData);
			mContext.sendBroadcast(curIntent);
			break;
		case SendUtils.numGetRam:
			sData = RecvUtils.getData(response);
			Intent ramFrqIntent = new Intent(SciModel.Frq_Ram);
			ramFrqIntent.putExtra("frqRam", sData);
			mContext.sendBroadcast(ramFrqIntent);
			break;
		case SendUtils.numGetProm:
			sData = RecvUtils.getData(response);
			Intent promFrqIntent = new Intent(SciModel.Frq_Prom);
			promFrqIntent.putExtra("frqProm", sData);
			mContext.sendBroadcast(promFrqIntent);
			break;
		case SendUtils.numGetAlarm:
			int alarmCode = RecvUtils.getAlarm(response);
			Intent getAlarmIntent = new Intent(SciModel.Get_Alarm);
			getAlarmIntent.putExtra("alarmCode", alarmCode);
			mContext.sendBroadcast(getAlarmIntent);
			break;
		}
	}

	public void closeSci() {
		// TODO Auto-generated method stub
		if(OnOff.isSciOpened()) {						
			OnOff.setSciOpened(false);
			sci.close(fd);
			Toast.makeText(mContext, "串口关闭",
					Toast.LENGTH_SHORT).show();
		}
	}


	/************按键处理*************/
	
	public void setSendNum(int i) {
		connectThread.setBtnNum(i);		
	}
	
	/************频率设置*************/
	public void setFrqRam(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setSendNum(SendUtils.numSetRam);
	}

	public void setFrqProm(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setSendNum(SendUtils.numSetProm);
	}
	
	public void frqUp() {
		if(outFrq < 50) {
			outFrq++;
			setFrqRam(outFrq);
		}
		else Toast.makeText(mContext, "已到限制频率",
				Toast.LENGTH_SHORT).show();
	}
	
	public void frqDown() {
		if(outFrq > 0) {
			outFrq--;
			setFrqRam(outFrq);
		}
		else Toast.makeText(mContext, "已到限制频率",
				Toast.LENGTH_SHORT).show();
	}
	
	private byte[] getNumBytes(int frq) {
		String s = Integer.toHexString(frq).toUpperCase();
		String st = String.format("%4s", s).replace(' ', '0');
		return st.getBytes();
	}

	public void setSendFlag() {
		// TODO Auto-generated method stub
		connectThread.setSendFlag();
	}
}
