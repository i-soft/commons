package org.isf.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

	public static String normalizeFloatingString(String s) {
		if (s.indexOf(",") >= 0) return s.replace(",", ".");
		else return s;
	}
	
	public static byte[] objectToByteArray(Object obj) throws IOException {
		if (obj == null) return null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();
		bos.close();
		byte[] data = bos.toByteArray();
		return data;
	}
	
	public static Object byteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException {
		Object ret = null;
		if (bytes != null) {
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));
			ret = oin.readObject();
			oin.close();
		}
		return ret;
	}
	
	public static String[] splitLine(String line, String separator) { // TODO String ... separators
		ArrayList<String> arr = new ArrayList<String>();
		int pos = 0;
		try {
			while((pos = line.indexOf(separator)) != -1) {
				arr.add(line.substring(0, pos));
				line = line.substring(pos+1);
			}
			arr.add(line);
		} catch(Exception e) {
		}
		
		String[] ret = new String[arr.size()];
		for (int i=0;i<arr.size();i++) ret[i] = arr.get(i);
		return ret;		
	}
	
	public static String flatLine(String[] values, String separator) {
		String ret = "";
		for (int i=0;i<values.length;i++)
			ret += i==0 ? values[i] : separator+values[i];
		return ret;
	}
	
	public static String flatLineQuote(String[] values, String separator, String quote) {
		String ret = "";
		for (int i=0;i<values.length;i++)
			ret += (i==0 ? "" : separator) + quote(values[i], quote);
		return ret;
	}
	
	public static String normalizeName(String s) {
		try {
			return (s.startsWith("get", 0) || s.startsWith("set", 0)) ? s.substring(3,4).toLowerCase()+s.substring(4) : (s.startsWith("is", 0)) ? s.substring(2,3).toLowerCase()+s.substring(3) : s.substring(0,1).toLowerCase()+s.substring(1);
		} catch (Exception e) {
			return s;
		}
	}
	
	public static String denormalizeName(String s, String prefix) {
		try {
			return prefix+s.substring(0, 1).toUpperCase() + s.substring(1);
		} catch(Exception e) {
			return s;
		}
	}
	
	public static String firstSegment(String path, String separator) {
		String[] split = splitLine(path, separator);
		return split.length > 0 ? split[0] : "";
	}
	
	public static String cutFirstSegment(String path, String separator) {
		String[] split = splitLine(path, separator);
		String ret = "";
		if (split.length > 1) 
			for (int i=1;i<split.length;i++)
				ret += (i==1 ? "" : separator)+split[i];
		return ret;
	}
	
	public static int matchCount(String regex, String text) {
		return listMatches(regex, text).length;
	}
	
	public static boolean hasMatches(String regex, String text) {
//		Pattern p = Pattern.compile(regex);
//		Matcher m = p.matcher(text);
//		return m.matches();
		return matchCount(regex, text) > 0;
	}
	
	public static String[] listMatches(String regex, String text) {
		List<String> l = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text); 
		while(m.find()) l.add(m.group());
		return l.toArray(new String[0]);
	}
	
	public static String replaceMatches(String regex, String text) {
		for (String s : listMatches(regex, text))
			text = text.replace(s, "");
		return text;
	}
	
	public static String cut(String value, String start, String end) {
		int idxs = value.indexOf(start)+start.length();
		int idxe = value.lastIndexOf(end);
		return value.substring(idxs, idxe);
		
	}
	
	public static boolean arrayContains(Object[] array, Object o) {
		for (Object ao : array)
			if (o.equals(ao)) return true;
		return false;
	}
	
	public static String fillString(String value, char fillChar, int length) {
		return fillString(value, fillChar, length, true);
	}
	
	public static String fillString(String value, char fillChar, int length, boolean trailing) {
		if (fillChar == 0) fillChar = ' ';
		if (value.length() > length) return value.substring(0, length);
		else if (value.length() < length) {
			int olen = length-value.length();
			char[] of = new char[olen];
			for (int i=0;i<of.length;i++) of[i] = fillChar;
			return trailing ? value + String.valueOf(of) : String.valueOf(of) + value;
		} else return value;
	}
	
	public static String trim(String s, char tchar) {
		int len = s.length();
		int st = 0;
		char[] val = s.toCharArray();
		while ((st < len) && (val[st] == tchar))
			st++;
		while ((st < len) && (val[len-1] == tchar))
			len--;
		return ((st > 0) || (len < s.length())) ? s.substring(st, len) : s;
	}
	
	public static String ltrim(String s, char tchar) {
		int len = s.length();
		int st = 0;
		char[] val = s.toCharArray();
		while ((st < len) && (val[st] == tchar))
			st++;
		return ((st > 0) || (len < s.length())) ? s.substring(st, len) : s;
	}
	
	public static String rtrim(String s, char tchar) {
		int len = s.length();
		int st = 0;
		char[] val = s.toCharArray();
		while ((st < len) && (val[len-1] == tchar))
			len--;
		return ((st > 0) || (len < s.length())) ? s.substring(st, len) : s;
	}
	
	public static String doubleToGERString(Double d) {
		if (d == null) return "";
		return String.valueOf(d).replace('.', ',');
	}
	
	public static String quote(String s, String quote) {
		return quote(s, quote, quote);
	}
	
	public static String quote(String s, String lquote, String rquote) {
		return lquote+s+rquote;
	}
	
	public static boolean isGreater(Object o1, Object o2) {
		if (o1 == null && o2 == null) return false;
		else if (o1 == null && o2 != null) return false;
		else if (o1 != null && o2 == null) return true;
		else if (o1 instanceof Comparable) {
			@SuppressWarnings("unchecked")
			Comparable<Object> co1 = (Comparable<Object>)o1;
			return co1.compareTo(o2) > 0;
		} else return false;
	}
	
	public static boolean isGreaterOrEqual(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		else if (o1 == null && o2 != null) return false;
		else if (o1 != null && o2 == null) return true;
		else if (o1 instanceof Comparable) {
			@SuppressWarnings("unchecked")
			Comparable<Object> co1 = (Comparable<Object>)o1;
			return co1.compareTo(o2) >= 0;
		} else return false;
	}
	
	public static boolean isLess(Object o1, Object o2) {
		if (o1 == null && o2 == null) return false;
		else if (o1 == null && o2 != null) return true;
		else if (o1 != null && o2 == null) return false;
		else if (o1 instanceof Comparable) {
			@SuppressWarnings("unchecked")
			Comparable<Object> co1 = (Comparable<Object>)o1;
			return co1.compareTo(o2) < 0;
		} else return false;
	}
	
	public static boolean isLessOrEqual(Object o1, Object o2) {
		if (o1 == null && o2 == null) return true;
		else if (o1 == null && o2 != null) return true;
		else if (o1 != null && o2 == null) return false;
		else if (o1 instanceof Comparable) {
			@SuppressWarnings("unchecked")
			Comparable<Object> co1 = (Comparable<Object>)o1;
			return co1.compareTo(o2) <= 0;
		} else return false;
	}
	
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
	
//	public static void main(String[] args) throws Exception {
//		String text = "/adresses['sales'][1].city";
//		System.out.println(text);
//		for (String s : listMatches("\\[[^\\]]+\\]", text)) {
//			System.out.println(s);
//		}
//		System.out.println(replaceMatches("\\[[^\\]]+\\]", text));
//		System.out.println(text);
//		System.out.println(cut("${name = 'Milo'}", "${", "}"));
//		String regex = "\\$\\{[^\\}]+\\}";
//		text = "\n\t\t\t\t(fahrzeugid,logdate) in \n\t\t\t\t(select a.fahrzeugid,max(a.logdate) as logdate\n\t\t\t\tfrom ( \n\t\t\t\tselect FAHRZEUGID,logdate from caroffice.fzbrief \n\t\t\t\tunion \n\t\t\t\tselect FAHRZEUGID,logdate from caroffice.fzbrief_log \n\t\t\t\t) a, atlas.fzhistory b\n\t\t\t\twhere a.fahrzeugid = b.fahrzeugid and a.logdate <= b.logdate and date(b.logdate) = '${DWH_LOAD_DATE}'\n\t\t\t\tgroup by a.fahrzeugid)\n\t\t\t";
//		System.out.println(hasMatches(regex, text));
//		for (String s : listMatches(regex, text))
//			System.out.println(cut(s, "${", "}"));
//		String val = "ROW-EL 235";
//		String slike = "ROW-%";
//		regex = "^"+slike.replace("_", ".").replace("%", ".*")+"$";
//		System.out.println(hasMatches(regex, val));
//	}
	
}
