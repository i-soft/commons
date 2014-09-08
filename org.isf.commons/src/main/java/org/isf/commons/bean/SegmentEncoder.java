package org.isf.commons.bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.isf.commons.Util;

import com.google.common.collect.Iterables;

public class SegmentEncoder {

	private static final String REGEX = "\\[[^\\]]+\\]";
	private static final String SCRIPT_REGEX = "\\$\\{[^\\}]+\\}";
	
	private String segment;
	private String[] matches;
	
	public SegmentEncoder(String segment) {
		setSegment(segment);
	}
	
	public String getSegment() { return segment; }
	protected void setSegment(String segment) { 
		this.segment = segment;
		setMatches(Util.listMatches(REGEX, segment));
	}
	
	public String[] getMatches() { return matches; }
	protected void setMatches(String[] matches) { this.matches = matches; }
			
	public String getRealSegment() { return Util.replaceMatches(REGEX, getSegment()); }
	
//	protected String cut(String value, char start, char end) { 
//		int idx = value.indexOf(start)+1;
//		return value.substring(value.indexOf(start)+1,value.indexOf(end, idx)); 
//	}
	
	public String getParam(int index) {
		return index >= 0 && index < matches.length ? Util.cut(matches[index], "[", "]") : "";
	}
	
	protected boolean isNumber(String value) {
		try {
			new Integer(value);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean isNumber(int index) {
		return isNumber(getParam(index));
	}
	
	public int getNumber(int index) {
		return new Integer(getParam(index)).intValue();
	}
	
	public boolean isString(String value) {
		return value.startsWith("'") && value.endsWith("'");
	}
	
	public boolean isString(int index) {
		return isString(getParam(index));
	}
	
	public String getString(int index) {
		return Util.cut(getParam(index), "'", "'");
	}
	
	public boolean isScript(int index) {
		return Util.hasMatches(SCRIPT_REGEX, getParam(index));
	}
	
	public String[] getScript(int index) {
		List<String> l = new ArrayList<String>();
		for (String s : Util.listMatches(SCRIPT_REGEX, getParam(index))) 
			l.add(Util.cut(s, "${", "}"));
		return l.toArray(new String[0]);
	}
	
	public int count() { return matches.length; }
	
	public boolean hasParams() { return count() > 0; }
	
	protected boolean checkObject(String script, Object obj) {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		try {
			Object e = engine.eval(script, new BeanBindings(obj));
			return e instanceof Boolean ? ((Boolean)e).booleanValue() : false;
		} catch(Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected Collection<Object> handleCollection(Collection<Object> input) {
		if (isString(0)) throw new IllegalArgumentException("Can not handle collections with key-names.");
		else if (isNumber(0) || isScript(0)) {
			try {
				Collection<Object> ret = input.getClass().newInstance();
				if (isNumber(0)) {
					Object res = Iterables.get(input, getNumber(0));
					ret.add(res);
		 		} else if (isScript(0)) {
		 			for (Object o : input)
		 				if (checkObject(getScript(0)[0], o))
		 					ret.add(o);
		 		}
				return ret;
			} catch(Exception e) {
				throw new IllegalArgumentException(e);
			}
		} else return input;
	}
	
	protected Object handleArray(Object input) {
		if (isString(0)) throw new IllegalArgumentException("Can not handle arrays with key-names.");
		List<Object> l = new ArrayList<Object>();
		for (int i=0;i<Array.getLength(input);i++) 
			l.add(Array.get(input, i));
		Collection<Object> cl = handleCollection(l);
		Object arr = Array.newInstance(input.getClass().getComponentType(), cl.size());
		for (int i=0;i<cl.size();i++) {
			Object o = Iterables.get(cl, i);
			Array.set(arr, i, o);
		}
		return arr;
	}
	
	@SuppressWarnings("unchecked")
	protected Map<Object, Object> handleMap(Map<Object, Object> input) {
		if (isNumber(0)) throw new IllegalArgumentException("Can not handle maps with indexes.");
		else if (isString(0) || isScript(0)) {
			try {
				Map<Object, Object> ret = input.getClass().newInstance();
				if (isString(0)) {
					String key = getString(0);
					ret.put(key, input.get(key));
				} else if (isScript(0)) {
					for (Object key : input.keySet()) { 
						Object obj = input.get(key);
						if (checkObject(getScript(0)[0], obj)) 
							ret.put(key, obj);
					}
				}
				return ret;
			} catch(Exception e) {
				throw new IllegalArgumentException(e);
			}
		} else return input;
	}
	
	@SuppressWarnings("unchecked")
	public Object getData(Object input) {
		if (count() > 1) throw new IllegalArgumentException("More than one conditions not supported.");
		if (input == null) return null;
		if (input.getClass().isArray() && hasParams()) {
			return handleArray(input);
		} else if (Collection.class.isAssignableFrom(input.getClass()) && hasParams()) {
			return handleCollection((Collection<Object>)input);
		} else if (Map.class.isAssignableFrom(input.getClass()) && hasParams()) {
			return handleMap((Map<Object, Object>)input);
		} else if (hasParams()) {
			throw new IllegalArgumentException("Conditions only for collection, arrays or maps allowed");
		}
		return input;
	}
	
	public static String getRealSegment(String segment) { return Util.replaceMatches(REGEX, segment); }
	
}
