package com.XD.inverterterminal.serial_jni;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class SciClass {
	/**
	 * 奇偶校验位的设置
	 */
	public final static int OLD_CHECK = 1;
	public final static int NONE_CHECK = 0;
	public final static int EVEN_CHECK = 2;
	
	private FileDescriptor fd;				// 申明文件描述类用于本地C程序调用
	private FileInputStream in;
	private FileOutputStream out;
	private static SciClass sci;
	
	/**
	 * 获取SCI实例，单例模式
	 * 
	 * @return sci 当前的SCI实例
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
	
	// =============控制数据流的开关=================
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
		
	// ==========================应用程序接口=============================
	/**
	 * 打开串口设备
	 * @param device
	 * @param baud
	 * @param checkout
	 * @return 串口文件对应的文件描述符
	 */
	public FileDescriptor openSerialPort(File device, int baud, int checkout) {
		return open(device.getAbsolutePath(), baud, checkout);
	}
	
	/**
	 * 向串口设备写入数据
	 * @param fd
	 * @param data
	 * @throws IOException
	 */
	public void write(FileDescriptor fd, byte[] data) throws IOException {
		out.write(data);
	}
	
	/**
	 * 串口读入数据到buf
	 * @param buf
	 * @return 读取数据的字节数
	 * @throws IOException
	 */
	public int read(byte[] buf) throws IOException {
		return in.read(buf);
	}
	
	/**
	 * 轮询串口设备，检查是否有数据输入
	 * @param fd
	 * @return 是否有数据输入
	 * @throws IOException
	 */
	public boolean select(FileDescriptor fd) throws IOException {
		openFileStream(fd);
		return in.available() > 0 ? true : false;
	}
	
	/**
	 * 关闭串口设备
	 * @param fd
	 */
	public void close(FileDescriptor fd) {
		closeFileStream();
		close();
	}

	// ==========================JNI函数申明=============================
	private native FileDescriptor open(String path, int baudrate, int check);

	private native void close();
}
