package com.XD.inverterterminal.thread;

import com.XD.inverterterminal.model.SciModel;
import com.XD.inverterterminal.utils.SendUtils;

public class ParaThread extends Thread {

	private SciModel sModel;
	private int paraNum;
	
	private boolean sendOnce = true;
	private boolean sendFlag = true;
	public ParaThread(SciModel sci) {
		sModel = sci;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(sendOnce) {
			while(sendFlag) {
				if(paraNum++ >= 7)
					sendOnce = false;
				switch(paraNum) {
				case 1:
					sModel.send(SendUtils.get0);
					break;
				case 2:
					sModel.send(SendUtils.get1);
					break;
				case 3:
					sModel.send(SendUtils.get2);
					break;
				case 4:
					sModel.send(SendUtils.get7);
					break;
				case 5:
					sModel.send(SendUtils.get8);
					break;
				case 6:
					sModel.send(SendUtils.get9);
					break;
				case 7:
					sModel.send(SendUtils.get14);
					break;
				case 8:
					sModel.send(SendUtils.get71);
					break;		
				}
			}
		}
	}
	
	public void setSendFlag(boolean b) {
		sendFlag = b;
	}
	
	public int getParaNum() {
		return paraNum;
	}
}
