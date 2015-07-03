package com.XD.inverterterminal.model;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;


import com.XD.inverterterminal.R;
import com.XD.inverterterminal.serial_jni.SciClass;
import com.XD.inverterterminal.thread.RecvThread;
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
	private boolean sciOpened = false;
	
	private SciClass sci;
	private FileDescriptor fd;
	
	private RecvThread sciListener;
	private conThread connectThread;
	
	private boolean sendFlag = true;
	private int sendNum;
	
	public SciModel(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
	}

	public void openSci() {
		// TODO Auto-generated method stub
		if (new File(SERIAL_PORT).exists())
		{
			if (!sciOpened)
			{
//				exeShell("busybox chmod 777 /dev/ttyO0");
				sci = SciClass.getInstance();
				fd = sci.openSerialPort(new File(SERIAL_PORT), SERIAL_BAUD,
						SciClass.OLD_CHECK);
				if(fd != null) {
					Toast.makeText(mContext,
							mContext.getResources().getString(R.string.openSCI_sucssess),
							Toast.LENGTH_SHORT).show();			
					sciListener = new RecvThread(sci, fd);
					sciListener.setOnRecvListener(new OnRecvListener() {						
						@Override
						public void OnRecv(byte[] response) {
							// TODO Auto-generated method stub
							Log.d("receiveMsg", new String(response));
							parserResponse(response);
						}

						@Override
						public void OnError() {
							// TODO Auto-generated method stub
							Log.d("Terminal", "noMsg");
//							setSendFlag();
							Intent errIntent = new Intent(SciModel.Conn_Error);
							mContext.sendBroadcast(errIntent);
						}

						@Override
						public void OnConnError() {
							// TODO Auto-generated method stub
							Log.d("Terminal", "conn_error");
							Intent errIntent = new Intent(SciModel.Conn_Error);
							mContext.sendBroadcast(errIntent);
						}
					});
					connectThread = new conThread();
					
					//发送、接收线程打开
					sciOpened = true;
					sciListener.open();					
					connectThread.start();
					
				}
				else// 串口存在，打开fd=null则说明没有执行权限
				{
					Toast.makeText(mContext, "串口没有执行权限",
							Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(mContext, "串口已经打开",
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
			processData(sendNum, response);
			break;	
		}
	}

	private void processData(int i, byte[] response) {
		// TODO Auto-generated method stub
		int sData;
		switch(i) {
		case SendUtils.numStatus:
//			sData = RecvUtils.getData(response);
//			if(sData == 128) {
//				Intent alarmIntent = new Intent(SciModel.Alarm);
//				alarmIntent.putExtra("status", getStatus(sData));
//				mContext.sendBroadcast(alarmIntent);
//			} else {
//				Intent statusIntent = new Intent(SciModel.Status_Refresh);
//				Log.d("Terminalstatus", "" + sData);
//				statusIntent.putExtra("status", getStatus(sData));
//				mContext.sendBroadcast(statusIntent);
//			}
//			break;
			sData = RecvUtils.getData(response);
			Intent cur2Intent = new Intent(SciModel.Current_Refresh);
			cur2Intent.putExtra("current", sData);
			mContext.sendBroadcast(cur2Intent);
			break;
		case SendUtils.numOutFrq:
			sData = RecvUtils.getData(response);
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

	private String getStatus(int s) {
		// TODO Auto-generated method stub
		switch(s) {
		case 1:
			return "变频器正在运行";
		case 2:
			return "变频器正转";
		case 4:
			return "变频器反转";
		case 8:
			return "频率达到（SU）";
		case 16:
			return "过负荷（OL）";		
		case 64:
			return "频率检测（FU）";
		case 128:
			return "发生报警";
		default:
			return null;	
		}
	}
 
	private class conThread extends Thread {		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			sendNum = 0;
			while(sciOpened) {
				while(sendFlag) {
					if(sendNum < 3)
						sendNum++;
					else if (sendNum == 3)
						sendNum = 1;			
					switch(sendNum) {
					case SendUtils.numStatus:
						send(SendUtils.getStatus);					
						break;
					case 2:
						send(SendUtils.getOutFrq);
						break;
					case 3:
						send(SendUtils.getCurrent);	
						break;
					case SendUtils.numRun:
						send(SendUtils.run);
						sendNum = 0;
						break;
					case SendUtils.numReverse:
						send(SendUtils.reverse);
						sendNum = 0;
						break;
					case SendUtils.numStop:
						send(SendUtils.stop);
						sendNum = 0;
						break;
					case SendUtils.numGetRam:
						send(SendUtils.getFrqRam);
						sendNum = 0;
						break;
					case SendUtils.numGetProm:
						send(SendUtils.getFrqProm);
						sendNum = 0;
						break;
					case SendUtils.numSetRam:
						send(SendUtils.setFrqRam);
						sendNum = 0;
						break;
					case SendUtils.numSetProm:
						send(SendUtils.setFrqProm);
						sendNum = 0;
						break;
					case SendUtils.numGetAlarm:
						send(SendUtils.getAlamrCode);
						sendNum = 0;
						break;
					}
				}
			}
		}
	}

	public void closeSci() {
		// TODO Auto-generated method stub
		if(sciOpened) {
			sciOpened = false;
			sciListener.close();
			Toast.makeText(mContext, "串口关闭",
					Toast.LENGTH_SHORT).show();
		}
	}


	/************按键处理*************/
	
	public void setSendNum(int i) {
		sendNum = i;
	}
	
	public void setSendFlag() {
		sendFlag = true;
	}
	
	/************串口发送*************/
	private void send(byte[] data) {
		if(!sciOpened) {
			Toast.makeText(mContext, "请先开启串口",
					Toast.LENGTH_SHORT).show();			 
		}
		else {
			sendFlag = false;
			sciWrite(data);
		}
	}
	
	private void sciWrite(final byte[] data)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					sci.write(fd, data);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}).start();
	}

	/************频率设置*************/
	public void SetFrqRam(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setSendNum(SendUtils.numSetRam);
	}

	public void SetFrqProm(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setSendNum(SendUtils.numSetProm);
	}
	
	private byte[] getNumBytes(int frq) {
		String s = Integer.toHexString(frq).toUpperCase();
		String st = String.format("%4s", s).replace(' ', '0');
		return st.getBytes();
	}

	public void reset() {
		// TODO Auto-generated method stub
		send(SendUtils.reset);
	}
}
