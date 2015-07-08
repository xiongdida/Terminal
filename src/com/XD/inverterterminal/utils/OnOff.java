package com.XD.inverterterminal.utils;

public class OnOff {

	private static boolean sciOpened = false;

	public static boolean isSciOpened() {
		return sciOpened;
	}

	public static void setSciOpened(boolean sciOpened) {
		OnOff.sciOpened = sciOpened;
	}	
}
