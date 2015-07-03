package com.XD.inverterterminal.thread;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import android.util.Log;

import com.XD.inverterterminal.inter.OnSciCommunication;
import com.XD.inverterterminal.serial_jni.SciClass;



public class RecvThread extends Thread implements OnSciCommunication{

	boolean openFlag;
	private SciClass sci;
	private FileDescriptor fd;

	private OnRecvListener mListener;

	byte[] buf = new byte[2048];

	int num1 = 0;
	int num2 = 0;
	
	public RecvThread(SciClass s, FileDescriptor f) {
		// TODO Auto-generated constructor stub
		sci = s;
		fd = f;
	}

	@Override
	public void run()
	{
		openFlag = true;

		while (openFlag)
		{
			boolean m = false;
			try
			{
				m = sci.select(fd);
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			if (m)
			{
				Log.d("num1", "" + num1);
				num1 = 0;
				try {
					byte[] result = readData(sci);
					if(mListener != null) {
						if (result!= null) {
//							Log.d("receiveMsg", result.toString());
							mListener.OnRecv(result);
						}
						else mListener.OnError();
					}					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			else if (++num1 == 10000) {
//				num1 = 0;
//				mListener.OnError();
//				if (++num2 == 10){
//					num2 = 0;
//					mListener.OnConnError();
//				}
//			}
		}
	}

	private byte[] readData(SciClass sci) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 1;
        int i = 0;
        while(buffer[len-1] != 0x0D){
        	len = sci.read(buffer);
        	baos.write(buffer, 0, len);
        	if(++i == 1000) {
        		byte[] data = baos.toByteArray();
        		Log.d("receiveErr", new String(data));
        		baos.close();
        		return null;
        	}
        }
        byte[] data = baos.toByteArray();
        baos.close();
		return data;		
	}

	@Override
	public void open()
	{
		this.start();
	}

	/*
	 * ¹Ø±ÕSCI
	 */
	@Override
	public void close()
	{
		openFlag = false;
		sci.close(fd);
	}

	public interface OnRecvListener
	{
		public void OnRecv(byte[] response);
		public void OnError();
		public void OnConnError();
	}
	
	public void setOnRecvListener(OnRecvListener listener) {
		// TODO Auto-generated method stub
		mListener = listener;
	}
}