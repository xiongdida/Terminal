package com.XD.inverterterminal.utils;


public class RecvUtils {
	//���ݿ�ʼ
	public static final byte STX = 0x02;
	//���ݽ���
	public static final byte ETX = 0x03;
	//û�������ݴ���
	public static final byte ACK = 0x06;
	//���ݴ���
	public static final byte NAK = 0x15;	
	//���ݽ���(�س���
	public static final byte CR = 0x0D;
	
	public static final String[] alarms_title = {"û�б���", "OC1", "OC2", "OC3", "OV1", "OV2", "OV3", "THT", "THM", "FIN", "OLT", 
		"BE", "GF", "LF", "OHT", "OPT", "PE", "PUE", "RET", "P24", "E.3", "E.6", "E.7", "��ȡ��������"};
	
	public static final String[] alarms_name = {"û�б���", "�����й�������·", "�����й�������·", "�����й�������·", 
		"��������������ѹ��·", "��������������ѹ��·", "���٣�ֹͣ����������ѹ��·", "��Ƶ�������ɶ�·(���ӹ�������)", 
		"��������ɶ�·(���ӹ�������)", "ɢ��Ƭ����", "ʧ�ٷ�ֹ", "�ƶ�����ܱ���", "�����ӵع���������", "���Ƿ�ౣ��", 
		"�ⲿ�ȼ̵�������", "ѡ���쳣", "���������쳣", "������Ԫ����", "���Դ�������", "ֱ��24V��Դ�����·", "ѡ���쳣", 
		"CPU ����", "CPU ����", "��ȡ��������"};
	public static final String[] alarms_content = {"û�б���", 
		"���������У�����Ƶ����������ﵽ�򳬹���Լ�������200%ʱ��������·������ֹͣ��Ƶ�����", 
		"���������У�����Ƶ����������ﵽ�򳬹���Լ�������200%ʱ��������·������ֹͣ��Ƶ�����", 
		"���������У����١���������֮�⣩������Ƶ����������ﵽ�򳬹���Լ�������200%ʱ��������·������ֹͣ��Ƶ�����", 
		"������������ʹ��Ƶ���ڲ�������·ֱ����ѹ�����涨ֵ��������·������ֹͣ��Ƶ���������Դϵͳ�﷢������ӿ��ѹҲ����������", 
		"������������ʹ��Ƶ���ڲ�������·ֱ����ѹ�����涨ֵ��������·������ֹͣ��Ƶ���������Դϵͳ�﷢������ӿ��ѹҲ����������", 
		"������������ʹ��Ƶ���ڲ�������·ֱ����ѹ�����涨ֵ��������·������ֹͣ��Ƶ���������Դϵͳ�﷢������ӿ��ѹҲ����������", 
		"������������������150%����δ����������·(200%����)ʱ��Ϊ�����������ܣ��÷�ʱ�����ԣ�ʹ���ӹ�������������ֹͣ��Ƶ�����", 
		"����Ƶ�������õ��ӹ�����������⵽���ڹ����ɻ�������ʱ����ȴ�������ͣ�����������ʱ��ֹͣ��Ƶ��������༫�������̨���ϵ������ʱ�����ڱ�Ƶ������లװ�ȼ̵���", 
		"�����ȴ���ȹ��ȣ�ͨ���¶ȴ����������ʹ��Ƶ��ֹͣ���", "��ʧ�ٷ�ֹ����������Ƶ�ʽ���0ʱ��ʧ�ٷ�ֹ��������ʾOL", 
		"���ڴӵ�����ص���������̫��ʹ�ƶ�����ܷ����쳣�������ƶ�������쳣���ڴ�����£���Ƶ����Դ�������̹ض�", 
		"��Ƶ������ʱ����Ƶ��������ࣨ���ɣ������ӵع��ϣ��Ե���©����ʱ����Ƶ�������ֹͣ", "����Ƶ�������(���ɲ�)����(U��V��W)����һ��Ͽ�ʱ���˹���ֹͣ��Ƶ�������", 
		"Ϊ��ֹ������ȣ���װ���ⲿ�ȼ̵��������ڲ���װ���¶ȼ̵�������ʱ(�ӵ��)��ʹ��Ƶ�����ֹͣ����ʹ�̵����ӵ��Զ���λ����Ƶ������λ�Ͳ�����������", 
		"����������ѡ�������ϵ��쳣��ͨѶѡ����ͨѶ�쳣�ȣ�ʱ����Ƶ��ֹͣ���������ģʽʱ������վΪ���״̬�����Ƶ��ֹͣ���", 
		"�洢�Ĳ����﷢���쳣", 
		"��Pr.75�趨�ڡ�2������3��״̬�£���PU����Ƶ����PU֮���ͨѶ�жϣ���Ƶ�������ֹͣ����RS-485ͨ��PU�ӿ�ͨѶ����Pr.121��Ϊ��9999��ʱ�������������ͨѶ������������������Դ�������Ƶ���������ֹͣ", 
		"����������趨����������û�лָ����˹��ܽ�ֹͣ��Ƶ�������", 
		"��PC���������ֱ��24V��Դ��·ʱ����Դ����жϡ���ʱ���ⲿ�ӵ�����ȫ��ΪOFF������RES���벻�ܸ�λ����λ�Ļ�����ʹ�ò��������Դ�ж���Ͷ��ķ���", 
		"ʹ�ñ�Ƶ��ר�õ�ͨ��ѡ��ʱ���趨�����Ӵ����ӿڣ�����ʱ����Ƶ��ֹͣ���", "����CPU��ͨ���쳣����ʱ����Ƶ��ֹͣ���", "����CPU��ͨ���쳣����ʱ����Ƶ��ֹͣ���", 
		"��ȡ��������"};
	public static final String[] alarms_checkPoint = {"û�б���", 
		"�Ƿ񼱼�����ת�������õ��½��μ���ʱ���Ƿ�̫��������Ƿ��·���ӵ�", "�����Ƿ��м��ٱ仯������Ƿ��·���ӵ�", 
		"�Ƿ񼱼�����ת������Ƿ��·���ӵأ�����Ļ�е�ƶ��Ƿ����", "���ٶ��Ƿ�̫����", "�����Ƿ��м��ٱ仯", "�Ƿ񼱼�����ת", "����Ƿ��ڹ�����", "����Ƿ��ڹ�����", 
		"��Χ�¶��Ƿ���ߣ���ȴɢ��Ƭ�Ƿ����", "����Ƿ������ʹ��", "�ƶ���ʹ��Ƶ���Ƿ����", "������������Ƿ�ӵ�", 
		"ȷ�Ͻ��ߣ�����Ƿ����������Ƿ�ʹ�ñȱ�Ƶ������С�ö�ĵ��", "����Ƿ���ȣ���Pr.180��Pr.183(������ӹ���ѡ��)����һ�����趨ֵ7��OH�źţ��Ƿ���ȷ�趨", 
		"ͨѶ�����Ƿ��ж���", "����д������Ƿ�̫��", "������壨FR-PA02-02����FR-PU04-CH�İ�װ�Ƿ�̫�ɣ�ȷ��Pr.75���趨ֵ", 
		"�����쳣������ԭ��", "PC��������Ƿ��·", "ѡ���Ĺ����趨�������Ƿ�����ͨ��ѡ�����Ӳ�ͷ�����Ƿ�ȷʵ���Ӻã���Ƶ����Χ����ǿ�ҵ��Ӳ�����", 
		"��Ƶ����Χ����ǿ�ҵ��Ӳ�����", "��Ƶ����Χ����ǿ�ҵ��Ӳ�����", "��ȡ��������"};
	public static final String[] alarms_handle = {"û�б���", 
		"�ӳ�����ʱ�䣻���������õ��½��μ���ʱ��", "ȡ�����ɵļ��ٱ仯", 
		"�ӳ�����ʱ�䣻����ƶ�����", "���̼���ʱ��", "ȡ�����ɵļ��ٱ仯���������Ҫʹ���ƶ���Ԫ���߹�����������FR-HC������Դ����������������FR-CV��", 
		"�ӳ�����ʱ�䣨ʹ����ʱ����ϸ��ɵ�ת���������������ƶ�Ƶ�ȣ��������Ҫʹ���ƶ���Ԫ���߹�����������FR-HC������Դ����������������FR-CV��", 
		"���Ḻ��", "���Ḻ�ɣ���ת�ص��ʱ����Pr.71�趨Ϊ��ת�ص��", "��Χ�¶ȵ��ڵ��涨��Χ��", "���Ḻ��", 
		"������Ƶ�����뾭���̻򱾹�˾��ϵ", "�ų��ӵصĵط�", "��ȷ���ߣ�ȷ��Pr.251�����Ƿ�ౣ��ѡ�񡱵��趨ֵ", "���͸��ɺ�����Ƶ��", 
		"�뾭���̻򱾹�˾��ϵ", "�뾭���̻򱾹�˾��ϵ", "�ι̰�װ�ò�����壨FR-PA02-02����FR-PU04-CH", "�������쳣֮ǰһ�����쳣", "�ų���·��", 
		"�ι̵ؽ������ӣ���Ƶ����Χ��������з���ǿ�Ҹ����Ӳ���װ�ã�Ҫ��ȡ��Ӧ�Ĵ�ʩ���뾭���̻򱾹�˾��ϵ", 
		"��Ƶ����Χ��������з���ǿ�Ҹ����Ӳ���װ�ã�Ҫ��ȡ��Ӧ�Ĵ�ʩ���뾭���̻򱾹�˾��ϵ", "��Ƶ����Χ��������з���ǿ�Ҹ����Ӳ���װ�ã�Ҫ��ȡ��Ӧ�Ĵ�ʩ���뾭���̻򱾹�˾��ϵ", 
		"��ȡ��������"};
//	public static final String[] alarms_title = {"û�б���", "OC1", "OC2", "OC3", "OV1", "OV2", "OV3", "THT", "THM", "FIN", "OLT", 
//		"BE", "GF", "LF", "OHT", "OPT", "PE", "PUE", "RET", "P24", "E.3", "E.6", "E.7", "��ȡ��������"};	
	public static String getDataErr(byte input) {
		// TODO Auto-generated method stub
		switch(input) {
		case 0x30:
			return "�ն�NAK����";
		case 0x31:
			return "��żУ�����";
		case 0x32:
			return "�ܺ�У�����";
		case 0x33:
			return "Э�����";
		case 0x34:
			return "��ʽ����";
		case 0x35:
			return "�������";
		case 0x37:
			return "�ַ�����";
		case 0x41:
			return "ģʽ����";
		case 0x42:
			return "ָ��������";
		case 0x43:
			return "���ݷ�Χ����";
		}
		return null;
	}

	public static int getData(byte[] input) {
		// TODO Auto-generated method stub
		int data;
		switch(input.length) {
		case 9:
			data = ascToInt(input[3]) * 16 + ascToInt(input[4]);
			return data;
		case 11:
			data = (int) (ascToInt(input[3]) * 4096 + ascToInt(input[4]) * 256
					+ ascToInt(input[5]) * 16 + ascToInt(input[6]));
			return data;
		default:
			return 0;
		}
	}

	private static int ascToInt(byte a) {
		if (a >= '0' && a <= '9') 
			return (a - '0');
		else if (a >= 'A' && a <= 'F') 
			return (a - 'A' + 10);
		else if (a >= 'a' && a <= 'f')
			return (a - 'a' + 10);
		return 0;
	}

	public static int getAlarm(byte[] response) {
		// TODO Auto-generated method stub
		byte[] a = {response[5], response[6]};
		int ac = getAlarmCode(a);
		return ac;
	}

	private static int getAlarmCode(byte[] a) {
		switch(a[0]) {
		case 0x30 :
			if (a[1] == 0x30)
				return 0;
			break;
		case 0x31 :
			if (a[1] == 0x30)
				return 1;
			else if (a[1] == 0x31)
				return 2;
			else if (a[1] == 0x32)
				return 3;
			break;
		case 0x32 :
			if (a[1] == 0x30)
				return 4;
			else if (a[1] == 0x31)
				return 5;
			else if (a[1] == 0x32)
				return 6;
			break;
		case 0x33 :
			if (a[1] == 0x30)
				return 7;
			else if (a[1] == 0x31)
				return 8;
			break;
		case 0x34 :
			if (a[1] == 0x30)
				return 9;
			break;
		case 0x36 :
			if (a[1] == 0x30)
				return 10;
			break;
		case 0x37 :
			if (a[1] == 0x30)
				return 11;
			break;
		case 0x38 :
			if (a[1] == 0x30)
				return 12;
			else if (a[1] == 0x31)
				return 13;
			break;
		case 0x39 :
			if (a[1] == 0x30)
				return 14;
			break;
		case 0x41 :
			if (a[1] == 0x30)
				return 15;
			break;
		case 0x42 :
			if (a[1] == 0x30)
				return 16;
			else if (a[1] == 0x31)
				return 17;
			else if (a[1] == 0x32)
				return 18;
			break;
		case 0x43 :
			if (a[1] == 0x32)
				return 19;
			break;
		case 0x46 :
			if (a[1] == 0x33)
				return 20;
			else if (a[1] == 0x36)
				return 21;
			else if (a[1] == 0x37)
				return 22;
			break;			
		}
		return 23;
		
	}

}