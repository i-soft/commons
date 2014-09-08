package org.isf.commons;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtil {

	public static void copyFile(File src, File dst, int buffsize, boolean forcedelete) throws IOException {
		if (dst.exists()) 
			if (forcedelete) dst.delete();
			else throw new IOException("Destination-File "+dst.getAbsolutePath()+" already exists.");
		if (!src.exists()) throw new IOException("Source-File "+src.getAbsolutePath()+" does not exists.");
		
		byte[] buffer = new byte[(buffsize > 0) ? buffsize : 4096];
		int read = 0;
		RandomAccessFile in = null;
		RandomAccessFile out = null;
		try {
			in = new RandomAccessFile(src, "r");
			out = new RandomAccessFile(dst, "rw");
			while((read = in.read(buffer)) > -1) 
				out.write(buffer, 0, read);
		} catch (IOException e) {
			throw e;
		} finally {
			try { in.close(); } catch (Exception e) { throw new IOException(e); }
			try { out.close(); } catch (Exception e) { throw new IOException(e); }
		}
	}
	
	public static void copyFile(File src, File dst) throws IOException {
		copyFile(src, dst, 4096, true);
	}
	
	public static void createBakFile(File file) throws IOException {
		File dst = new File(file.getParent()+File.separator+extractFileName(file)+".bak");
		copyFile(file, dst);
	}
	
	public static String extractFileName(File file) {
		return extractFileName(file.getName());
	}
	
	public static String extractFileName(String file) {
		try {
			int from = file.lastIndexOf(File.separator)+1;
			return file.substring((from > 0)? from : 0, file.lastIndexOf("."));
		} catch (Exception e) {
			return file;
		}
	}
	
	public static String extractFileExt(File file) {
		return extractFileExt(file.getAbsolutePath());
	}
	
	public static String extractFileExt(String file) {
		try {
			return file.substring(file.lastIndexOf(".")+1);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static boolean deleteFile(File file) {
		if (file.exists()) 
			if (file.isDirectory())
				for (File f : file.listFiles()) { 
					if (f.isDirectory()) deleteFile(file);
					else f.delete();
				}
		return file.delete();
	}
	
}
