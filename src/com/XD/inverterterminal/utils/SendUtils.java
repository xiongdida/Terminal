package com.XD.inverterterminal.utils;

public class SendUtils {
	//ͨѶ����
	public static final byte ENQ = 0x05;
	//��Ƶ��վ��
	public static final byte NUM = 0x30;
	//��Ƶ���ȴ�ʱ��
	public static final byte WAITTIME = 0x31;
	//ͨѶЭ���β
	public static final byte TAIL = 0x0D;
	
	//�ֽ�0
	public static final byte ZERO = 0x30;
	
	
	//���ͱ��
	public static final int numStatus 	= 1;
	public static final int numOutFrq 	= 2;
	public static final int numCurrent 	= 3;
	
	public static final int numRun 		= 10;
	public static final int numReverse 	= 11;
	public static final int numStop 	= 12;
	public static final int numGetRam 	= 13;
	public static final int numGetProm 	= 14;
	public static final int numSetRam 	= 15;
	public static final int numSetProm 	= 16;
	
	public static final int numGetAlarm = 19;
	

	/**********����***********/	
	//��ѯ������� H70
	public static final byte[] getCurrent = {ENQ, NUM, NUM, 0x37, 0x30, WAITTIME, 0x46, 0x38, TAIL};
	
	//��ѯ���Ƶ�ʣ��ٶȣ� H6F
	public static final byte[] getOutFrq = {ENQ, NUM, NUM, 0x36, 0x46, WAITTIME, 0x30, 0x44, TAIL};
	
	//��Ƶ��״̬���� H7A
	public static final byte[] getStatus = {ENQ, NUM, NUM, 0x37, 0x41, WAITTIME, 0x30, 0x39, TAIL};
	
	//��ѯ�����ѹ H71
	public static final byte[] getVoltage = {ENQ, NUM, NUM, 0x37, 0x31, WAITTIME, 0x46, 0x39, TAIL};
	
	/**********����Ƶ�����á�����***********/
	
	//�趨Ƶ�ʶ�����RAM��H6D
	public static final byte[] getFrqRam = {ENQ, NUM, NUM, 0x36, 0x44, WAITTIME, 0x30, 0x42, TAIL};
	
	//�趨Ƶ�ʶ�����E2PROM��H6E
	public static final byte[] getFrqProm = {ENQ, NUM, NUM, 0x36, 0x45, WAITTIME, 0x30, 0x43, TAIL};
	
	//�趨Ƶ��д�루RAM��HED ZERO����Ϊʵʱ����
	public static final byte[] setFrqRam = {ENQ, NUM, NUM, 0x45, 0x44, WAITTIME, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, TAIL};
	
	//�趨Ƶ��д�루E2PROM��HEE ZERO����Ϊʵʱ����
	public static final byte[] setFrqProm = {ENQ, NUM, NUM, 0x45, 0x45, WAITTIME, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, TAIL};

	/**********��������***********/
	
	//�������� H74
	public static final byte[] getAlamrCode = {ENQ, NUM, NUM, 0x37, 0x34, WAITTIME, 0x46, 0x43, TAIL};
	
	/**********����ָ��FA***********/
	//��ת H02
	public static final byte[] run = {ENQ, NUM, NUM, 0x46, 0x41, WAITTIME, 0x30, 0x32, 0x37, 0x41, TAIL};
	//ֹͣ H00
	public static final byte[] stop = {ENQ, NUM, NUM, 0x46, 0x41, WAITTIME, 0x30, 0x30, 0x37, 0x38, TAIL};
	//��ת H04
	public static final byte[] reverse = {ENQ, NUM, NUM, 0x46, 0x41, WAITTIME, 0x30, 0x34, 0x37, 0x43, TAIL};
	
	/**********��λָ�� HFD***********/
	public static final byte[] reset = {ENQ, NUM, NUM, 0x46, 0x44, WAITTIME, 0x39, 0x36, 0x39, 0x36, 0x46, 0x39, TAIL};
	
	public static void getCheckSum(byte[] a) {
		byte sum = 0x00;
		for(int i = 1; i <= 9; i++) {
			sum += a[i];
		}
		byte[] bSum = Integer.toHexString(sum & 0xff).toUpperCase().getBytes();
		if(bSum.length == 1) {
			a[10] = 0x30;
			a[11] = bSum[0];
		}
		else {
			a[10] = bSum[0];
			a[11] = bSum[1];
		}
	}
}
