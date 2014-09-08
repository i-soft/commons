package org.isf.commons.type;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.isf.commons.codec.Hex;

public class Binary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890930238369279276L;

	private byte[] data;
	
	public Binary() {}
	
	public byte[] getData() { return data; }
	public void setData(byte[] data) { this.data = data; }
	
	public int length() { return getData() != null ? getData().length : 0; }
	
	public InputStream openStream() { return new ByteArrayInputStream(getData()); }
	
	public void saveToFile(File file) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
		try {
			saveToStream(out);
		} finally {
			out.close();
		}
	}
	
	public void saveToFile(String filename) throws IOException {
		saveToFile(new File(filename));
	}
	
	public void saveToStream(OutputStream out) throws IOException {
		byte[] buf = new byte[1024];
		int len = 0;
		InputStream in = openStream();
		try {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} finally {
			in.close();
		}
	}
	
	public void loadFromFile(File file) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		try {
			loadFromStream(in);
		} finally {
			in.close();
		}
	}
	
	public void loadFromFile(String filename) throws IOException {
		loadFromFile(new File(filename));
	}
	
	public void loadFromStream(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int len = 0;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			setData(out.toByteArray());
		} finally {
			out.close();
		}
	}
	
	public byte[] md5Hash() {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(getData(), 0, length());
			return md.digest();
		} catch(NoSuchAlgorithmException nsae) {
			throw new RuntimeException(nsae);
		}
	}
	
	public String toString() {
		return Hex.encode(md5Hash());
	}
	
	public int hashCode() {
		int hash = 0;
		byte[] md5 = md5Hash();
		for (int i=0;i<md5.length;i+=4) {
			hash ^= md5[i] << 24 | (md5[i+1] & 0xFF) << 16 | (md5[i+2] & 0xFF) << 8 | (md5[i+1] & 0xFF);
		}
		return hash;
	}
	
}
