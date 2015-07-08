package com.XD.inverterterminal.thread;

import java.io.FileDescriptor;
import java.io.IOException;

import com.XD.inverterterminal.serial_jni.SciClass;
import com.XD.inverterterminal.utils.OnOff;
import com.XD.inverterterminal.utils.SendUtils;

public class SendThread extends Thread {		
	
	private boolean sendFlag = false;
	private boolean btnClick = false;
	private int sendNum;
	private int btnNum;
	
	private SciClass sci;
	private FileDescriptor fd;
	
	public SendThread(SciClass s, FileDescriptor f) {
		// TODO Auto-generated constructor stub
		sci = s;
		fd = f;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(OnOff.isSciOpened()) {
			while(sendFlag) {
				if(!btnClick) {
					if(++sendNum > 5)
						sendNum = 1;
					switch(sendNum) {
					case SendUtils.numStatus:
						send(SendUtils.getStatus);					
						break;
					case SendUtils.numOutFrq:
						send(SendUtils.getOutFrq);
						break;
					case SendUtils.numCurrent:
						send(SendUtils.getCurrent);	
						break;
					case SendUtils.numGetRam:
						send(SendUtils.getFrqRam);
						break;
					case SendUtils.numGetProm:
						send(SendUtils.getFrqProm);
						break;
					}
				}
				else {
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
					btnClick = false;
				}
			}
		}
	}

	public void open()
	{
		sendNum = 0;
		sendFlag = true;
		this.start();
	}
	
	public void setSendFlag() {
		sendFlag = true;
	}
	
	/************´®¿Ú·¢ËÍ*************/
	private void send(byte[] data) {
		sendFlag = false;
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

	public int getBtnNum() {
		return btnNum;
	}

	public void setBtnNum(int btnNum) {
		btnClick = true;
		this.btnNum = btnNum;
	}

	public int getSendNum() {
		return sendNum;
	}

	public void setSendNum(int sendNum) {
		this.sendNum = sendNum;
	}
}
