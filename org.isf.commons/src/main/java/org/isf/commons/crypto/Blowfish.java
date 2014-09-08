package org.isf.commons.crypto;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Blowfish {
	
	private static final String ALGORITHM = "Blowfish";

	public static byte[] encrypt(String key, String text) throws GeneralSecurityException {
		SecretKey sk = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, sk);
		return cipher.doFinal(text.getBytes());
	}
	
	public static String decrypt(String key, byte[] buffer) throws GeneralSecurityException {
		SecretKey sk = new SecretKeySpec(key.getBytes(), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, sk);
		byte[] b = cipher.doFinal(buffer);
		return new String(b);
	}
	
}
