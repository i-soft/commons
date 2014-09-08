package org.isf.commons.bean;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.isf.commons.DateUtil;
import org.isf.commons.Util;

public class Converter {

	public static Integer toInteger(Short o) { 				return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(Byte o) { 				return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(Long o) { 				return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(Float o) { 				return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(Double o) { 			return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(BigDecimal o) { 		return o!=null ? new Integer(o.intValue()) : null; }
	public static Integer toInteger(Boolean o) { 			return o!=null ? new Integer(o ? 1 : 0) : null; }
	public static Integer toInteger(String o) {
		try {
			return o!=null && o.length() > 0 ? new Integer(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Integer toInteger(Date o) { 				return o!=null ? toInteger(o.getTime()) : null; }
	public static Integer toInteger(Time o) { 				return o!=null ? toInteger(o.getTime()) : null; }
	public static Integer toInteger(Timestamp o) { 			return o!=null ? toInteger(o.getTime()) : null; }
	public static Integer toInteger(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Integer(bb.getInt());
	}
	
	public static Short toShort(Integer o) { 				return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(Byte o) { 					return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(Long o) { 					return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(Float o) { 					return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(Double o) { 				return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(BigDecimal o) { 			return o!=null ? new Short(o.shortValue()) : null; }
	public static Short toShort(Boolean o) { 				return o!=null ? toShort(o ? 1 : 0) : null; }
	public static Short toShort(String o) {
		try {
			return o!=null && o.length() > 0 ? new Short(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Short toShort(Date o) { 					return o!=null ? toShort(o.getTime()) : null; }
	public static Short toShort(Time o) { 					return o!=null ? toShort(o.getTime()) : null; }
	public static Short toShort(Timestamp o) { 				return o!=null ? toShort(o.getTime()) : null; }
	public static Short toShort(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Short.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Short(bb.getShort());
	}
	
	
	public static Byte toByte(Short o) { 				return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(Integer o) { 				return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(Long o) { 				return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(Float o) { 				return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(Double o) { 				return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(BigDecimal o) { 			return o!=null ? new Byte(o.byteValue()) : null; }
	public static Byte toByte(Boolean o) { 				return o!=null ? toByte(o ? 1 : 0) : null; }
	public static Byte toByte(String o) { 				
		try {
			return o!=null && o.length() > 0 ? new Byte(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Byte toByte(Date o) { 				return o!=null ? toByte(o.getTime()) : null; }
	public static Byte toByte(Time o) { 				return o!=null ? toByte(o.getTime()) : null; }
	public static Byte toByte(Timestamp o) { 			return o!=null ? toByte(o.getTime()) : null; }
	public static Byte toByte(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Byte.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Byte(bb.get());
	}
	
	
	
	public static Long toLong(Short o) { 				return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(Byte o) { 				return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(Integer o) { 				return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(Float o) { 				return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(Double o) { 				return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(BigDecimal o) { 			return o!=null ? new Long(o.longValue()) : null; }
	public static Long toLong(Boolean o) { 				return o!=null ? new Long(o ? 1l : 0l) : null; }
	public static Long toLong(String o) { 				
		try { 
			return o!=null && o.length() > 0 ? new Long(o) : null; 
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Long toLong(Date o) { 				return o!=null ? new Long(o.getTime()) : null; }
	public static Long toLong(Time o) { 				return o!=null ? new Long(o.getTime()) : null; }
	public static Long toLong(Timestamp o) { 			return o!=null ? new Long(o.getTime()) : null; }
	public static Long toLong(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Long(bb.getLong());
	}
	
	
	public static Float toFloat(Short o) { 				return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(Byte o) { 				return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(Long o) { 				return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(Integer o) { 			return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(Double o) { 			return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(BigDecimal o) { 		return o!=null ? new Float(o.floatValue()) : null; }
	public static Float toFloat(Boolean o) { 			return o!=null ? new Float(o ? 1 : 0) : null; }
	public static Float toFloat(String o) {
		try {
			return o!=null && o.length() > 0 ? new Float(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Float toFloat(Date o) { 				return o!=null ? toFloat(o.getTime()) : null; }
	public static Float toFloat(Time o) { 				return o!=null ? toFloat(o.getTime()) : null; }
	public static Float toFloat(Timestamp o) { 			return o!=null ? toFloat(o.getTime()) : null; }
	public static Float toFloat(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Float.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Float(bb.getFloat());
	}
	
	
	public static Double toDouble(Short o) { 				return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(Byte o) { 				return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(Long o) { 				return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(Float o) { 				return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(Integer o) { 				return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(BigDecimal o) { 			return o!=null ? new Double(o.doubleValue()) : null; }
	public static Double toDouble(Boolean o) { 				return o!=null ? new Double(o ? 1 : 0) : null; }
	public static Double toDouble(String o) {
		try {
			return o!=null && o.length() > 0 ? new Double(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static Double toDouble(Date o) { 				return o!=null ? toDouble(o.getTime()) : null; }
	public static Double toDouble(Time o) { 				return o!=null ? toDouble(o.getTime()) : null; }
	public static Double toDouble(Timestamp o) { 			return o!=null ? toDouble(o.getTime()) : null; }
	public static Double toDouble(byte[] o) {
		ByteBuffer bb = ByteBuffer.allocate(Double.SIZE/Byte.SIZE);
		bb.put(o);
		bb.flip();
		return new Double(bb.getDouble());
	}
	
	
	public static BigDecimal toBigDecimal(Short o) { 				return o!=null ? new BigDecimal(o.intValue()) : null; }
	public static BigDecimal toBigDecimal(Byte o) { 				return o!=null ? new BigDecimal(o.intValue()) : null; }
	public static BigDecimal toBigDecimal(Long o) { 				return o!=null ? new BigDecimal(o.longValue()) : null; }
	public static BigDecimal toBigDecimal(Float o) { 				return o!=null ? new BigDecimal(o.doubleValue()) : null; }
	public static BigDecimal toBigDecimal(Double o) { 				return o!=null ? new BigDecimal(o.doubleValue()) : null; }
	public static BigDecimal toBigDecimal(Integer o) { 				return o!=null ? new BigDecimal(o.intValue()) : null; }
	public static BigDecimal toBigDecimal(Boolean o) { 				return o!=null ? new BigDecimal(o ? 1 : 0) : null; }
	public static BigDecimal toBigDecimal(String o) {
		try {
			return o!=null && o.length() > 0 ? new BigDecimal(o) : null;
		} catch(NumberFormatException nfe) {
			return null;
		}
	}
	public static BigDecimal toBigDecimal(Date o) { 				return o!=null ? toBigDecimal(o.getTime()) : null; }
	public static BigDecimal toBigDecimal(Time o) { 				return o!=null ? toBigDecimal(o.getTime()) : null; }
	public static BigDecimal toBigDecimal(Timestamp o) { 			return o!=null ? toBigDecimal(o.getTime()) : null; }
	public static BigDecimal toBigDecimal(byte[] o) {
		return new BigDecimal(new BigInteger(o), 0); 
	}
	
	
	public static Boolean toBoolean(Short o) { 				return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(Byte o) { 				return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(Long o) { 				return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(Float o) { 				return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(Double o) { 			return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(BigDecimal o) { 		return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(Integer o) { 			return o!=null ? new Boolean(o.intValue() != 0) : null; }
	public static Boolean toBoolean(String o) { 			return o!=null && o.length() > 0 ? new Boolean(o) : null; }
	public static Boolean toBoolean(Date o) { 				return o!=null ? toBoolean(o.getTime()) : null; }
	public static Boolean toBoolean(Time o) { 				return o!=null ? toBoolean(o.getTime()) : null; }
	public static Boolean toBoolean(Timestamp o) { 			return o!=null ? toBoolean(o.getTime()) : null; }
	public static Boolean toBoolean(byte[] o) {
		return new Boolean(toByte(o).byteValue() == 0);
	}
	
	
	public static String toString(Short o) { 				return o!=null ? o.toString() : null; }
	public static String toString(Byte o) { 				return o!=null ? o.toString() : null; }
	public static String toString(Long o) { 				return o!=null ? o.toString() : null; }
	public static String toString(Float o) { 				return o!=null ? o.toString() : null; }
	public static String toString(Double o) { 				return o!=null ? o.toString() : null; }
	public static String toString(BigDecimal o) { 			return o!=null ? o.toString() : null; }
	public static String toString(Boolean o) { 				return o!=null ? o.toString() : null; }
	public static String toString(Integer o) { 				return o!=null ? o.toString() : null; }
	public static String toString(java.util.Date o) {		return o!=null ? DateUtil.toString(o) : null; }
	public static String toString(Date o) { 				return o!=null ? DateUtil.toString(o) : null; }
	public static String toString(Time o) { 				return o!=null ? DateUtil.toString(o) : null; }
	public static String toString(Timestamp o) { 			return o!=null ? DateUtil.toString(o) : null; }
	public static String toString(byte[] o) {
		try {
			return (new String(o, "UTF-8")).trim();
		} catch(UnsupportedEncodingException uee) {
			return (new String(o)).trim();
		}
	}
	
	
	public static Date toDate(Short o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Byte o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Long o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Float o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Double o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(BigDecimal o) { 			return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Boolean o) { 				return o!=null ? toDate(o ? 1 : 0) : null; }
	public static Date toDate(String o) {
		try {
			return o!=null && o.length() > 0 ? DateUtil.toSqlDate(o) : null;
		} catch(NullPointerException npe) {
			return null;
		}
	}
	public static Date toDate(Integer o) { 				return o!=null ? new Date(o.longValue()) : null; }
	public static Date toDate(Time o) { 				return o!=null ? new Date(o.getTime()) : null; }
	public static Date toDate(Timestamp o) { 			return o!=null ? new Date(o.getTime()) : null; }
	public static Date toDate(java.util.Date o) { 		return o!=null ? DateUtil.toSqlDate(o) : null; }
	public static Date toDate(byte[] o) { return new Date(toLong(o).longValue()); }
	
	
	public static Time toTime(Short o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Byte o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Long o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Float o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Double o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(BigDecimal o) { 			return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Boolean o) { 				return o!=null ? toTime(o ? 1 : 0) : null; }
	public static Time toTime(String o) {
		try {
			return o!=null && o.length() > 0 ? DateUtil.toSqlTime(o) : null;
		} catch(NullPointerException npe) {
			return null;
		}
	}
	public static Time toTime(Date o) { 				return o!=null ? new Time(o.getTime()) : null; }
	public static Time toTime(Integer o) { 				return o!=null ? new Time(o.longValue()) : null; }
	public static Time toTime(Timestamp o) { 			return o!=null ? new Time(o.getTime()) : null; }
	public static Time toTime(byte[] o) { return new Time(toLong(o).longValue()); }
	
	
	public static Timestamp toTimestamp(Short o) { 				return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(Byte o) { 				return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(Long o) { 				return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(Float o) { 				return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(Double o) { 			return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(BigDecimal o) { 		return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(Boolean o) { 			return o!=null ? toTimestamp(o ? 1 : 0) : null; }
	public static Timestamp toTimestamp(String o) {
		try {
			return o!=null && o.length() > 0 ? DateUtil.toSqlTimestamp(o) : null;
		} catch(NullPointerException npe) {
			return null;
		}
	}
	public static Timestamp toTimestamp(Date o) { 				return o!=null ? new Timestamp(o.getTime()) : null; }
	public static Timestamp toTimestamp(Time o) { 				return o!=null ? new Timestamp(o.getTime()) : null; }
	public static Timestamp toTimestamp(Integer o) { 			return o!=null ? new Timestamp(o.longValue()) : null; }
	public static Timestamp toTimestamp(byte[] o) { return new Timestamp(toLong(o).longValue()); }
	
	
	public static byte[] toByteArray(Integer o) {
		ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
		bb.putInt(o);
		return bb.array();
	}
	public static byte[] toByteArray(Short o) {
		ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE/Byte.SIZE);
		bb.putShort(o);
		return bb.array();
	}
	public static byte[] toByteArray(Byte o) {
		ByteBuffer bb = ByteBuffer.allocate(Byte.SIZE/Byte.SIZE);
		bb.put(o);
		return bb.array();
	}
	public static byte[] toByteArray(Long o) {
		ByteBuffer bb = ByteBuffer.allocate(Long.SIZE/Byte.SIZE);
		bb.putLong(o);
		return bb.array();
	}
	public static byte[] toByteArray(Float o) {
		ByteBuffer bb = ByteBuffer.allocate(Float.SIZE/Byte.SIZE);
		bb.putFloat(o);
		return bb.array();
	}
	public static byte[] toByteArray(Double o) {
		ByteBuffer bb = ByteBuffer.allocate(Double.SIZE/Byte.SIZE);
		bb.putDouble(o);
		return bb.array();
	}
	public static byte[] toByteArray(BigDecimal o) {
		BigInteger bi = o.unscaledValue();
		return bi.toByteArray();
	}
	public static byte[] toByteArray(Boolean o) {
		return toByteArray((byte)(o?1:0));
	}
	public static byte[] toByteArray(String o) {
		return toByteArray(o,o.length());
	}
	public static byte[] toByteArray(String o, int length) {
		o = Util.fillString(o, ' ', length);
		try {
			return o.getBytes("UTF-8");
		} catch(UnsupportedEncodingException uee) {
			return null; // TODO handle Exception
		}
	}
	public static byte[] toByteArray(Date o) {
		return toByteArray(o.getTime());
	}
	public static byte[] toByteArray(Time o) {
		return toByteArray(o.getTime());
	}
	public static byte[] toByteArray(Timestamp o) {
		return toByteArray(o.getTime());
	}
	
	
	public static <T> T convert(Class<T> clazz, Object data) {
		if (data == null) return null;
		if (clazz.isAssignableFrom(data.getClass())) return clazz.cast(data);
		
		String clsname = clazz.getSimpleName();
		/* ERROR
		// wrap simple names for simple types
		if (clazz == int.class) clsname = "Integer";
		if (clazz == short.class) clsname = "Short";
		if (clazz == byte.class) clsname = "Byte";
		if (clazz == long.class) clsname = "Long";
		if (clazz == float.class) clsname = "Float";
		if (clazz == double.class) clsname = "Double";
		if (clazz == boolean.class) clsname = "Boolean";		
		*/
		
		Class<Converter> cl = Converter.class;
		String name = "to"+clsname+(clazz.isArray() ? "Array" : "");
		try {
			Method m = cl.getMethod(name, data.getClass());
			return clazz.cast(m.invoke(null, data));
		} catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Can not convert "+data.getClass().getName()+" to "+clazz.getName());
		}
	}
	
	public static Object convert(Type type, Object data) {
		return convert(type.wrapper()[0], data);
	}
	
	public static void main(String[] args) {
		long l = 728903471324346575l;
		System.out.println("before: "+l);
		byte[] buf = toByteArray(l);
		System.out.println("buf-len: "+buf.length);
		String vals = "";
		for (int i=0;i<buf.length;i++)
			vals += (i==0?"":",")+buf[i];
		System.out.println("buf-content: "+vals);
		long nl = toLong(buf).longValue();
		System.out.println("after: "+nl);
	}
	
}
