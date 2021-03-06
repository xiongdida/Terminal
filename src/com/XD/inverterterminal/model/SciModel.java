package com.XD.inverterterminal.model;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;


import com.XD.inverterterminal.R;
import com.XD.inverterterminal.serial_jni.SciClass;
import com.XD.inverterterminal.thread.ParaThread;
import com.XD.inverterterminal.thread.RecvThread;
import com.XD.inverterterminal.thread.SendThread;
import com.XD.inverterterminal.utils.RecvUtils;
import com.XD.inverterterminal.utils.SendUtils;
import com.XD.inverterterminal.thread.RecvThread.OnRecvListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class SciModel {

//	public static String Data_ACK = "data_ack";
//	public static String Data_NAK = "data_nak";
//	
//	public static String Current_Refresh = "currentRef";
//	public static String OutFrq_Refresh = "outFrqRef";
//	public static String Status_Refresh = "statusRef";
//	
//	public static String Frq_Ram = "frqRam";
//	public static String Frq_Prom = "frqProm";
//	
//	public static String Alarm = "Alarm";
//	public static String Conn_Error = "conn_error";
//	public static String Get_Alarm = "get_alarm";
	
	public static final int Data_ACK = 0;
	public static final int Data_NAK = 1;
	
	public static final int Current_Refresh = 2;
	public static final int OutFrq_Refresh = 3;
	public static final int Status_Refresh = 4;
	
	public static final int Frq_Ram = 5;
	public static final int Frq_Prom = 6;
	
	public static final int Alarm = 7;
	public static final int Conn_Error = 8;
	public static final int Get_Alarm = 9;
	
	public static final String SERIAL_PORT = "/dev/ttyO0";
	public static final int SERIAL_BAUD = 9600;
	
	private SciClass sci;
	private FileDescriptor fd;
	
	private RecvThread sciListener;
	private SendThread connectThread;
	private ParaThread paraThread;
	
	private int outFrq = 0;
	
	//参数编号
	private SharedPreferences.Editor editor;
	
	//变频器运行方向
	private int run = 0;    //1为正转  0为停止   -1为反转
	
	//按键标号
	private int btnNum = 0;
	
	//串口开启标志
	private boolean sciOpened = false;
	
	//按键按下标示
	private boolean btnClicked = false;
	
	//参数询问线程打开标志
	private boolean paraFlag = false;
	
	//单例模式，方便main 与 setting 两个界面的通信
	private static SciModel instance = null;
	private static Context mContext;
	private static Handler mHandler;
	
	public void setHandler(Handler h) {
		mHandler = h;
	}
	
    public static SciModel getInstance(Context context){
        if(instance == null){
            instance = new SciModel();
        }
        mContext = context;
        return instance;
    }
    
	public SciModel() {
	}

/****************串口动作**********************/
	//串口开启，线程开启
	public void openSci() {
		// TODO Auto-generated method stub
		if (new File(SERIAL_PORT).exists())
		{			
			//获取串口实例
			sci = SciClass.getInstance();
			fd = sci.openSerialPort(new File(SERIAL_PORT), SERIAL_BAUD,
					SciClass.OLD_CHECK);
			if(fd != null) {
				//接收线程开启
				sciListener = new RecvThread(this);
				sciListener.setOnRecvListener(new OnRecvListener() {						
					@Override
					public void OnRecv(byte[] response) {
						// TODO Auto-generated method stub
//						Log.d("receiveMsg", new String(response));
						Log.v("TimeCheck", new String(response));
						parserResponse(response);
					}

					@Override
					public void OnConnError() {
						// TODO Auto-generated method stub
						Log.d("Terminal", "conn_error");
//						Intent errIntent = new Intent(SciModel.Conn_Error);
//						mContext.sendBroadcast(errIntent);
						Message msg = Message.obtain();
						msg.what = Conn_Error;
						mHandler.sendMessage(msg);
					}
				});
				
				connectThread = new SendThread(this);
				
				//串口开启标志设置
				setSciOpened(true);
				Toast.makeText(mContext, mContext.getResources().getString(R.string.openSCI_sucssess),
						Toast.LENGTH_SHORT).show();	
				
				//发送、接收线程打开
				connectThread.open();
				sciListener.open();
			}
			else// 串口存在，打开fd=null则说明没有执行权限
			{
				Toast.makeText(mContext, "串口没有执行权限", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(mContext, "本地串口不存在", Toast.LENGTH_SHORT).show();
		}
	}

	//串口关闭
	public void closeSci() {
		// TODO Auto-generated method stub
		if(isSciOpened()) {						
			sci.close(fd);
			setSciOpened(false);
			Toast.makeText(mContext, "串口关闭",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	//串口发送
	public void send(byte[] data) {
		setSendFlag(false);
		sciWrite(data);		
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

/****************接收数据解析动作**********************/
	//解析获取数据
	private void parserResponse(byte[] response) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		switch(response[0]) {
		//数据代码正确
		case RecvUtils.ACK:
//			Toast.makeText(mContext, "操作正确", Toast.LENGTH_SHORT).show();
//			Intent ackIntent = new Intent(SciModel.Data_ACK);
//			mContext.sendBroadcast(ackIntent);
			msg.what = Data_ACK;
			mHandler.sendMessage(msg);
			break;
		//数据代码错误
		case RecvUtils.NAK:
			String daErr = RecvUtils.getDataErr(response[3]);
//			Intent nakIntent = new Intent(SciModel.Data_NAK);
//			nakIntent.putExtra("dataError", daErr);
//			mContext.sendBroadcast(nakIntent);
//			Toast.makeText(mContext, "数据错误为：" + daErr, Toast.LENGTH_LONG).show();
			msg.what = Data_NAK;
			msg.obj = daErr;
			mHandler.sendMessage(msg);
			break;
		case RecvUtils.STX:
			if(!paraFlag) {
				//MainActivity的数据处理
				processData(connectThread.getSendNum(), response);
			} else {
				SharedPreferences sPreferences = mContext.getSharedPreferences("para_info", 0);
				editor = sPreferences.edit();
				processPara(paraThread.getParaNum(), response);
			}
			break;	
		}
	}

	//收到为返回数据，获得数据
		private void processData(int i, byte[] response) {
			// TODO Auto-generated method stub
			Message msg = Message.obtain();
			int sData;
			switch(i) {
			case SendUtils.numStatus:
				sData = RecvUtils.getData(response);
				if(sData >= 128) {
					msg.what = SciModel.Alarm;
					mHandler.sendMessage(msg);
				} else {
					msg.what = Status_Refresh;
					mHandler.sendMessage(msg);
				}
				break;
			case SendUtils.numOutFrq:
				sData = RecvUtils.getData(response);
				outFrq = sData/100;
				msg.what = OutFrq_Refresh;
				msg.arg1 = sData;
				mHandler.sendMessage(msg);
				break;
			case SendUtils.numCurrent:
				sData = RecvUtils.getData(response);
				msg.what = Current_Refresh;
				msg.arg1 = sData;
				mHandler.sendMessage(msg);
				break;
			case SendUtils.numGetRam:
				sData = RecvUtils.getData(response);
				msg.what = Frq_Ram;
				msg.arg1 = sData;
				mHandler.sendMessage(msg);
				break;
			case SendUtils.numGetProm:
				sData = RecvUtils.getData(response);
				msg.what = Frq_Prom;
				msg.arg1 = sData;
				mHandler.sendMessage(msg);
				break;
			case SendUtils.numGetAlarm:
				int alarmCode = RecvUtils.getData(response);
				msg.what = Get_Alarm;
				msg.arg1 = alarmCode;
				mHandler.sendMessage(msg);
				break;
			}
		}
		
//	//收到为返回数据，获得数据
//	private void processData(int i, byte[] response) {
//		// TODO Auto-generated method stub
//		int sData;
//		switch(i) {
//		case SendUtils.numStatus:
//			sData = RecvUtils.getData(response);
//			if(sData >= 128) {
//				Intent alarmIntent = new Intent(SciModel.Alarm);
//				alarmIntent.putExtra("status", "发生警报");
//				mContext.sendBroadcast(alarmIntent);
//			} else {
//				Intent statusIntent = new Intent(SciModel.Status_Refresh);
//				statusIntent.putExtra("status", "正常工作");
//				mContext.sendBroadcast(statusIntent);
//			}
//			break;
//		case SendUtils.numOutFrq:
//			sData = RecvUtils.getData(response);
//			outFrq = sData/100;
//			Intent velIntent = new Intent(SciModel.OutFrq_Refresh);
//			velIntent.putExtra("outFrq", sData);
//			mContext.sendBroadcast(velIntent);
//			break;
//		case SendUtils.numCurrent:
//			sData = RecvUtils.getData(response);
//			Intent curIntent = new Intent(SciModel.Current_Refresh);
//			curIntent.putExtra("current", sData);
//			mContext.sendBroadcast(curIntent);
//			break;
//		case SendUtils.numGetRam:
//			sData = RecvUtils.getData(response);
//			Intent ramFrqIntent = new Intent(SciModel.Frq_Ram);
//			ramFrqIntent.putExtra("frqRam", sData);
//			mContext.sendBroadcast(ramFrqIntent);
//			break;
//		case SendUtils.numGetProm:
//			sData = RecvUtils.getData(response);
//			Intent promFrqIntent = new Intent(SciModel.Frq_Prom);
//			promFrqIntent.putExtra("frqProm", sData);
//			mContext.sendBroadcast(promFrqIntent);
//			break;
//		case SendUtils.numGetAlarm:
//			int alarmCode = RecvUtils.getAlarm(response);
//			Intent getAlarmIntent = new Intent(SciModel.Get_Alarm);
//			getAlarmIntent.putExtra("alarmCode", alarmCode);
//			mContext.sendBroadcast(getAlarmIntent);
//			break;
//		}
//	}
	
	//获取参数
	private void processPara(int i, byte[] response) {
		int para = RecvUtils.getData(response);
		switch(i) {
		case 1:
			editor.putString("para0", para/10 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 2:
			editor.putString("para1", para/100 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 3:
			editor.putString("para2", para/100 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 4:
			editor.putString("para7", (1.0f * para)/10 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 5:
			editor.putString("para8", (1.0f * para)/10 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 6:
			editor.putString("para9", (1.0f * para)/100 + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 7:
			editor.putString("para14", para + "");
			editor.commit();
			paraThread.setSendFlag(true);
			break;
		case 8:
			editor.putString("para71", para + "");
			editor.commit();
			paraFlag = false;
			paraThread = null;
			break;
		}
	}

/*****************参数处理*********************/
	//获取参数值线程开启
	public void getPara() {
		if(isSciOpened()) {
			paraFlag = true;
			paraThread = new ParaThread(this);
			paraThread.start();
		}
	}
	
	public void closeParaThread() {
		paraThread.close();
	}
	//设置参数值
	public void setPar(int a, int value) {
		// TODO Auto-generated method stub
		switch(a) {
		case 0: byte[] num0 = getNumBytes(value * 10);
				for(int i = 0; i < num0.length; i++) {
					SendUtils.set0[6 + i] = num0[i];
				}
				SendUtils.getCheckSum(SendUtils.set0);
				send(SendUtils.set0);
				break;
		case 1: byte[] num1 = getNumBytes(value * 100);
				for(int i = 0; i < num1.length; i++) {
					SendUtils.set1[6 + i] = num1[i];
				}
				SendUtils.getCheckSum(SendUtils.set1);
				send(SendUtils.set1);
				break;
		case 2: byte[] num2 = getNumBytes(value * 100);
				for(int i = 0; i < num2.length; i++) {
					SendUtils.set2[6 + i] = num2[i];
				}
				SendUtils.getCheckSum(SendUtils.set2);
				send(SendUtils.set2);
				break;
		case 7: byte[] num7 = getNumBytes(value * 10);
				for(int i = 0; i < num7.length; i++) {
					SendUtils.set7[6 + i] = num7[i];
				}
				SendUtils.getCheckSum(SendUtils.set7);
				send(SendUtils.set7);
				break;
		case 8: byte[] num8 = getNumBytes(value * 10);
				for(int i = 0; i < num8.length; i++) {
					SendUtils.set8[6 + i] = num8[i];
				}
				SendUtils.getCheckSum(SendUtils.set8);
				send(SendUtils.set8);
				break;
		case 9: byte[] num9 = getNumBytes(value * 100);
				for(int i = 0; i < num9.length; i++) {
					SendUtils.set9[6 + i] = num9[i];
				}
				SendUtils.getCheckSum(SendUtils.set9);
				send(SendUtils.set9);
				break;
		case 14: byte[] num14 = getNumBytes(value);
				for(int i = 0; i < num14.length; i++) {
					SendUtils.set14[6 + i] = num14[i];
				}
				SendUtils.getCheckSum(SendUtils.set14);
				send(SendUtils.set14);
				break;
		case 71: byte[] num71 = getNumBytes(value);
				for(int i = 0; i < num71.length; i++) {
					SendUtils.set71[6 + i] = num71[i];
				}
				SendUtils.getCheckSum(SendUtils.set71);
				send(SendUtils.set71);
				break;
		}
	}
	
	//获取保存的参数值
	public String[] getSavePara() {
		String[] nValue = new String[8];
		SharedPreferences sPreferences = mContext.getSharedPreferences("para_info", 0);
		if(sPreferences.contains("para0") && sPreferences.contains("para1") && sPreferences.contains("para2") && sPreferences.contains("para7")
		 && sPreferences.contains("para8") && sPreferences.contains("para9") && sPreferences.contains("para14") && sPreferences.contains("para71")) {
			nValue[0] = sPreferences.getString("para0", "null");
			nValue[1] = sPreferences.getString("para1", "null");
			nValue[2] = sPreferences.getString("para2", "null");
			nValue[3] = sPreferences.getString("para7", "null");
			nValue[4] = sPreferences.getString("para8", "null");
			nValue[5] = sPreferences.getString("para9", "null");
			nValue[6] = sPreferences.getString("para14", "null");
			nValue[7] = sPreferences.getString("para71", "null");
		}
		return nValue;
	}

/*****************按键处理*********************/
	//保存按键标号
	public void setBtnClicked(int btn) {
		btnClicked = true;
		btnNum = btn;
	}
	
	//返回按键标志
	public boolean getBtnClicked() {
		return btnClicked;
	}
	
	//按键发送具体数据
	public void btnAction() {
		if(btnClicked){
			switch(btnNum) {
			case SendUtils.numRun:
				send(SendUtils.run);
				break;
			case SendUtils.numReverse:
				send(SendUtils.reverse);
				break;
			case SendUtils.numStop:
				send(SendUtils.stop);
				break;
			case SendUtils.numGetRam:
				send(SendUtils.getFrqRam);
				break;
			case SendUtils.numGetProm:
				send(SendUtils.getFrqProm);
				break;
			case SendUtils.numSetRam:
				send(SendUtils.setFrqRam);
				break;
			case SendUtils.numSetProm:
				send(SendUtils.setFrqProm);
				break;
			case SendUtils.numReset:
				send(SendUtils.reset);
				break;
			case SendUtils.numGetAlarm:
				send(SendUtils.getAlamrCode);
				break;
			}
		}
		btnClicked = false;
	}

/************频率设置*************/
	public void setFrqRam(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setBtnClicked(SendUtils.numSetRam);
	}

	public void setFrqProm(int frq) {
		// TODO Auto-generated method stub
		byte[] num = getNumBytes(frq * 100);
		for(int i = 0; i < num.length; i++) {
			SendUtils.setFrqRam[6 + i] = num[i];
		}
		SendUtils.getCheckSum(SendUtils.setFrqRam);
		setBtnClicked(SendUtils.numSetProm);
	}
	
	public void frqUp() {
		if(outFrq < 50) {
			outFrq++;
			setFrqRam(outFrq);
		}
		else Toast.makeText(mContext, "已到限制频率", Toast.LENGTH_SHORT).show();
	}
	
	public void frqDown() {
		if(outFrq > 0) {
			outFrq--;
			setFrqRam(outFrq);
		}
		else Toast.makeText(mContext, "已到限制频率", Toast.LENGTH_SHORT).show();
	}
	
	//发送数据的4个数据位
	private byte[] getNumBytes(int frq) {
		String s = Integer.toHexString(frq).toUpperCase();
		String st = String.format("%4s", s).replace(' ', '0');
		return st.getBytes();
	}

	//让循环发送线程先暂停发送
	public void setSendFlag(boolean b) {
		// TODO Auto-generated method stub
		if(!paraFlag)
			connectThread.setSendFlag(b);
		else paraThread.setSendFlag(b);
	}
	
/*****************串口开启状态获取与设置*********************/	
	public boolean isSciOpened() {
		return sciOpened;
	}
	
	public void setSciOpened(boolean b) {
		sciOpened = b;
	}

	public SciClass getSci() {
		return sci;
	}

	public FileDescriptor getFd() {
		return fd;
	}

	//变频器运动状态获取 修改
	public int getRun() {
		return run;
	}

	public void setRun(int run) {
		this.run = run;
		switch(run) {
		case 1:	setBtnClicked(SendUtils.numRun);
				break;
		case -1:setBtnClicked(SendUtils.numReverse);
				break;
		case 0:	setBtnClicked(SendUtils.numStop);
				break;		
		}
	}
	
}
