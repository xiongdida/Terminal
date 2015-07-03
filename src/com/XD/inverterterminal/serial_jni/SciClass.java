package com.XD.inverterterminal.serial_jni;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class SciClass {
	/**
	 * ��żУ��λ������
	 */
	public final static int OLD_CHECK = 1;
	public final static int NONE_CHECK = 0;
	public final static int EVEN_CHECK = 2;
	
	private FileDescriptor fd;				// �����ļ����������ڱ���C�������
	private FileInputStream in;
	private FileOutputStream out;
	private static SciClass sci;
	
	/**
	 * ��ȡSCIʵ��������ģʽ
	 * 
	 * @return sci ��ǰ��SCIʵ��
	 */
	public static SciClass getInstance()
	{
		if (sci == null)
		{
			sci = new SciClass();
		}
		return sci;
	}
	
	static {
		System.loadLibrary("serial_port");
	}
	
	// =============�����������Ŀ���=================
	private void openFileStream(FileDescriptor fd)
	{
		this.fd = fd;
		in = new FileInputStream(fd);
		out = new FileOutputStream(fd);
	}

	private void closeFileStream()
	{
		try
		{
			in.close();
			out.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
		
	// ==========================Ӧ�ó���ӿ�=============================
	/**
	 * �򿪴����豸
	 * @param device
	 * @param baud
	 * @param checkout
	 * @return �����ļ���Ӧ���ļ�������
	 */
	public FileDescriptor openSerialPort(File device, int baud, int checkout) {
		return open(device.getAbsolutePath(), baud, checkout);
	}
	
	/**
	 * �򴮿��豸д������
	 * @param fd
	 * @param data
	 * @throws IOException
	 */
	public void write(FileDescriptor fd, byte[] data) throws IOException {
		out.write(data);
	}
	
	/**
	 * ���ڶ������ݵ�buf
	 * @param buf
	 * @return ��ȡ���ݵ��ֽ���
	 * @throws IOException
	 */
	public int read(byte[] buf) throws IOException {
		return in.read(buf);
	}
	
	/**
	 * ��ѯ�����豸������Ƿ�����������
	 * @param fd
	 * @return �Ƿ�����������
	 * @throws IOException
	 */
	public boolean select(FileDescriptor fd) throws IOException {
		openFileStream(fd);
		return in.available() > 0 ? true : false;
	}
	
	/**
	 * �رմ����豸
	 * @param fd
	 */
	public void close(FileDescriptor fd) {
		closeFileStream();
		close();
	}

	// ==========================JNI��������=============================
	private native FileDescriptor open(String path, int baudrate, int check);

	private native void close();
}
