package com.XD.inverterterminal.utils;


public class RecvUtils {
	//数据开始
	public static final byte STX = 0x02;
	//数据结束
	public static final byte ETX = 0x03;
	//没发现数据错误
	public static final byte ACK = 0x06;
	//数据错误
	public static final byte NAK = 0x15;	
	//数据结束(回车）
	public static final byte CR = 0x0D;
	
	public static final String[] alarms_title = {"没有报警", "OC1", "OC2", "OC3", "OV1", "OV2", "OV3", "THT", "THM", "FIN", "OLT", 
		"BE", "GF", "LF", "OHT", "OPT", "PE", "PUE", "RET", "P24", "E.3", "E.6", "E.7", "获取报警错误"};
	
	public static final String[] alarms_name = {"没有报警", "加速中过电流断路", "定速中过电流断路", "减速中过电流断路", 
		"加速中再生过电压断路", "定速中再生过电压断路", "减速，停止中再生过电压断路", "变频器过负荷断路(电子过流保护)", 
		"电机过负荷断路(电子过流保护)", "散热片过热", "失速防止", "制动晶体管报警", "输出侧接地过电流保护", "输出欠相保护", 
		"外部热继电器动作", "选件异常", "参数记忆异常", "参数单元脱落", "再试次数超出", "直流24V电源输出短路", "选件异常", 
		"CPU 错误", "CPU 错误", "获取报警错误"};
	public static final String[] alarms_content = {"没有报警", 
		"加速运行中，当变频器输出电流达到或超过大约额定电流的200%时，保护回路动作，停止变频器输出", 
		"定速运行中，当变频器输出电流达到或超过大约额定电流的200%时，保护回路动作，停止变频器输出", 
		"减速运行中（加速、低速运行之外），当变频器输出电流达到或超过大约额定电流的200%时，保护回路动作，停止变频器输出", 
		"因再生能量，使变频器内部的主回路直流电压超过规定值，保护回路动作，停止变频器输出。电源系统里发生的浪涌电压也可能引起动作", 
		"因再生能量，使变频器内部的主回路直流电压超过规定值，保护回路动作，停止变频器输出。电源系统里发生的浪涌电压也可能引起动作", 
		"因再生能量，使变频器内部的主回路直流电压超过规定值，保护回路动作，停止变频器输出。电源系统里发生的浪涌电压也可能引起动作", 
		"如果电流超过额定电流的150%，而未发生电流断路(200%以下)时，为保护输出晶体管，用反时限特性，使电子过流保护动作，停止变频器输出", 
		"当变频器的内置电子过流保护，检测到由于过负荷或定速运行时，冷却能力降低，引起电机过热时，停止变频器输出。多极电机或两台以上电机运行时，请在变频器输出侧安装热继电器", 
		"如果冷却风扇过热，通过温度传感器检出，使变频器停止输出", "当失速防止动作，运行频率降到0时，失速防止动作中显示OL", 
		"由于从电机返回的再生能量太大，使制动晶体管发生异常，检测出制动晶体管异常，在此情况下，变频器电源必须立刻关断", 
		"变频器启动时，变频器的输出侧（负荷）发生接地故障，对地有漏电流时，变频器的输出停止", "当变频器输出侧(负荷侧)三相(U，V，W)中有一相断开时，此功能停止变频器的输出", 
		"为防止电机过热，安装在外部热继电器或电机内部安装的温度继电器动作时(接点打开)，使变频器输出停止。即使继电器接点自动复位，变频器不复位就不能重新启动", 
		"当发生内置选件功能上的异常（通讯选件的通讯异常等）时，变频器停止输出。网络模式时，若本站为解除状态，则变频器停止输出", 
		"存储的参数里发生异常", 
		"当Pr.75设定在“2”，“3”状态下，拆开PU，变频器和PU之间的通讯中断，变频器的输出停止。用RS-485通过PU接口通讯，当Pr.121不为“9999”时，如果连续发生通讯错误次数超过允许再试次数，变频器的输出将停止", 
		"如果在再试设定次数内运行没有恢复，此功能将停止变频器的输出", 
		"从PC端子输出的直流24V电源短路时，电源输出切断。此时，外部接点输入全部为OFF，端子RES输入不能复位。复位的话，请使用操作面板或电源切断再投入的方法", 
		"使用变频器专用的通信选件时，设定错误或接触（接口）不良时，变频器停止输出", "内置CPU的通信异常发生时，变频器停止输出", "内置CPU的通信异常发生时，变频器停止输出", 
		"获取报警错误"};
	public static final String[] alarms_checkPoint = {"没有报警", 
		"是否急加速运转；升降用的下降段加速时间是否太长；输出是否短路，接地", "负荷是否有急速变化；输出是否短路，接地", 
		"是否急减速运转；输出是否短路，接地；电机的机械制动是否过早", "加速度是否太缓慢", "负荷是否有急速变化", "是否急减速运转", "电机是否处于过负荷", "电机是否处于过负荷", 
		"周围温度是否过高；冷却散热片是否堵塞", "电机是否过负荷使用", "制动的使用频度是否合适", "电机，连接线是否接地", 
		"确认接线（电机是否正常）；是否使用比变频器容量小得多的电机", "电机是否过热；在Pr.180～Pr.183(输入端子功能选择)中任一个，设定值7（OH信号）是否正确设定", 
		"通讯电缆是否有断线", "参数写入回数是否太多", "操作面板（FR-PA02-02）或FR-PU04-CH的安装是否太松；确认Pr.75的设定值", 
		"调查异常发生的原因", "PC端子输出是否短路", "选件的功能设定、操作是否有误；通信选件连接插头插座是否确实连接好；变频器周围有无强烈的杂波干扰", 
		"变频器周围有无强烈的杂波干扰", "变频器周围有无强烈的杂波干扰", "获取报警错误"};
	public static final String[] alarms_handle = {"没有报警", 
		"延长加速时间；缩短升降用的下降段加速时间", "取消负荷的急速变化", 
		"延长减速时间；检查制动动作", "缩短加速时间", "取消负荷的急速变化；请根据需要使用制动单元、高功率整流器（FR-HC）及电源再生共用整流器（FR-CV）", 
		"延长减速时间（使减速时间符合负荷的转动惯量）；减少制动频度；请根据需要使用制动单元、高功率整流器（FR-HC）及电源再生共用整流器（FR-CV）", 
		"减轻负荷", "减轻负荷；定转矩电机时，将Pr.71设定为恒转矩电机", "周围温度调节到规定范围内", "减轻负荷", 
		"更换变频器；与经销商或本公司联系", "排除接地的地方", "正确接线；确认Pr.251“输出欠相保护选择”的设定值", "降低负荷和运行频度", 
		"与经销商或本公司联系", "与经销商或本公司联系", "牢固安装好操作面板（FR-PA02-02）和FR-PU04-CH", "处理该异常之前一个的异常", "排除短路处", 
		"牢固地进行连接；变频器周围如果设置有发射强烈干扰杂波的装置，要采取相应的措施；与经销商或本公司联系", 
		"变频器周围如果设置有发射强烈干扰杂波的装置，要采取相应的措施；与经销商或本公司联系", "变频器周围如果设置有发射强烈干扰杂波的装置，要采取相应的措施；与经销商或本公司联系", 
		"获取报警错误"};
//	public static final String[] alarms_title = {"没有报警", "OC1", "OC2", "OC3", "OV1", "OV2", "OV3", "THT", "THM", "FIN", "OLT", 
//		"BE", "GF", "LF", "OHT", "OPT", "PE", "PUE", "RET", "P24", "E.3", "E.6", "E.7", "获取报警错误"};	
	public static String getDataErr(byte input) {
		// TODO Auto-generated method stub
		switch(input) {
		case 0x30:
			return "终端NAK错误";
		case 0x31:
			return "奇偶校验错误";
		case 0x32:
			return "总和校验错误";
		case 0x33:
			return "协议错误";
		case 0x34:
			return "格式错误";
		case 0x35:
			return "溢出错误";
		case 0x37:
			return "字符错误";
		case 0x41:
			return "模式错误";
		case 0x42:
			return "指令代码错误";
		case 0x43:
			return "数据范围错误";
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
