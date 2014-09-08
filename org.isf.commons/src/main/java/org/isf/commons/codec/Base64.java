package org.isf.commons.codec;

public class Base64 {

	public static final char[] BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	public static int[] BASE64_ALPHABET_INDEX = new int[128];
	static {
		for (int i = 0;i<BASE64_ALPHABET.length;i++)
			BASE64_ALPHABET_INDEX[BASE64_ALPHABET[i]] = i;
	}
	
	public static String encode(byte[] data) {
		int size = data.length;
		char[] ar = new char[((size+2)/3)*4];
		int a = 0;
		int i = 0;
		while (i < size) {
			byte b0 = data[i++];
			byte b1 = (i < size) ? data[i++] : 0;
			byte b2 = (i < size) ? data[i++] : 0;
			int mask = 0x3F;
			ar[a++] = BASE64_ALPHABET[(b0 >> 2) & mask];
		    ar[a++] = BASE64_ALPHABET[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
		    ar[a++] = BASE64_ALPHABET[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
		    ar[a++] = BASE64_ALPHABET[b2 & mask];
		}
		switch(size % 3) {
			case 1: ar[--a] = '=';
			case 2: ar[--a] = '=';
		}
		return new String(ar);
	}
	
	public static byte[] decode(String base64) {
		int delta = base64.endsWith("==") ? 2 : base64.endsWith("=") ? 1 : 0;
		byte[] data = new byte[base64.length()*3/4-delta];
		int mask = 0xFF;
		int index = 0;
		for (int i=0;i<base64.length(); i += 4) {
			int c0 = BASE64_ALPHABET_INDEX[base64.charAt(i)];
			int c1 = BASE64_ALPHABET_INDEX[base64.charAt(i+1)];
			data[index++] = (byte)(((c0 << 2) | (c1 >> 4)) & mask);
			if (index >= data.length) return data;
			int c2 = BASE64_ALPHABET_INDEX[base64.charAt(i+2)];
			data[index++] = (byte)(((c1 << 4) | (c2 >> 2)) & mask);
			if (index >= data.length) return data;
			int c3 = BASE64_ALPHABET_INDEX[base64.charAt(i+3)];
			data[index++] = (byte)(((c2 << 6) | c3) & mask);
		}
		return data;
	}
	
	
}
