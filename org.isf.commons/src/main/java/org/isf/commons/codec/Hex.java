package org.isf.commons.codec;

public class Hex {

	public final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	public static String encode(byte[] data) {
		char[] hexc = new char[data.length*2];
		for (int i=0;i<data.length;i++) {
			int val = data[i] & 0xFF;
			hexc[i*2] = hexArray[val >>> 4];
			hexc[i*2+1] = hexArray[val & 0x0F];
		}
		return new String(hexc);
	}
	
	public static byte[] decode(String hex) {
		int len = hex.length();
		byte[] data = new byte[len/2];
		for (int i=0;i<len;i+=2) {
			data[i/2] = (byte)((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i+1),  16)); 
		}
		return data;
	}
	
}
