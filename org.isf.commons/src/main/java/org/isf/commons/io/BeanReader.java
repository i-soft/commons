package org.isf.commons.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.isf.commons.bean.Attribute;
import org.isf.commons.bean.Bean;
import org.isf.commons.bean.Converter;
import org.isf.commons.bean.Type;
import org.isf.commons.codec.Base64;
import org.isf.commons.xml.XMLDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BeanReader {

	private XMLDocument doc = new XMLDocument();
	
	public BeanReader(String filepath) throws IOException {
		try {
			doc.parseDocument(filepath);
		} catch(SAXException se) {
			throw new IOException("Error while parse document \""+filepath+"\".", se);
		}
	}
	
	public BeanReader(File file) throws IOException {
		this(file.getAbsolutePath());
	}
	
	public BeanReader(InputStream in) throws IOException {
		try {
			doc.parseDocument(in);
		} catch(SAXException se) {
			throw new IOException("Error while parse document from stream.", se);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	protected void handleCollectionData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Class<?> valueTypeClass = Object.class;
		if (attr.getGenericTypes().length >= 1) 
			valueTypeClass = attr.getGenericTypes()[0];
		Type valueType = Type.getTypePerClass(valueTypeClass);
		Node[] nl = doc.getChildNodes(nattr, "value");
		
		Collection<Object> coll = (Collection<Object>)bean.invoke(data, attr.getPath(), true);
		coll.clear();
		for (int i=0;i<nl.length;i++) {
			Object val = null;
			Node nval = nl[i];
			
			if (valueType == Type.OBJECT) val = createBeanContent(nval);
			else if (valueType == Type.BINARY) val = Base64.decode(doc.getTextValue(nval));
			else val = Converter.convert(valueType, doc.getTextValue(nval));
			
			coll.add(val);
		}
		bean.invoke(data, attr.getPath(), true, coll);
	}
	
	protected void handleArrayData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Class<?> valueTypeClass = attr.getType().getComponentType();
		Type valueType = Type.getTypePerClass(valueTypeClass);
		Node[] nl = doc.getChildNodes(nattr, "value");
		
		Object arr = Array.newInstance(valueTypeClass, nl.length);
		for (int i=0;i<nl.length;i++) {
			Object val = null;
			Node nval = nl[i];
			
			if (valueType == Type.OBJECT) val = createBeanContent(nval);
			else if (valueType == Type.BINARY) val = Base64.decode(doc.getTextValue(nval));
			else val = Converter.convert(valueType, doc.getTextValue(nval));
			
			Array.set(arr, i, val);
		}
		bean.invoke(data, attr.getPath(), true, arr);
	}
	
	@SuppressWarnings("unchecked")
	protected void handleMapData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Class<?> keyTypeClass = String.class;
		Class<?> valueTypeClass = String.class;
		if (attr.getGenericTypes().length >= 2) {
			keyTypeClass = attr.getGenericTypes()[0];
			valueTypeClass = attr.getGenericTypes()[1];
		}
		Type keyType = Type.getTypePerClass(keyTypeClass);
		Type valueType = Type.getTypePerClass(valueTypeClass);
		
		Map<Object, Object> attrMap = (Map<Object, Object>)bean.invoke(data, attr.getPath(), true);
		attrMap.clear();
		for (Node nvalue : doc.getChildNodes(nattr, "value")) {
			Object key = null;
			Object val = null;
			Node nkey = doc.getChildNode(nvalue, "key");
			Node nval = doc.getChildNode(nvalue, "value");
			
			if (keyType == Type.OBJECT) key = createBeanContent(nkey);
			else if (keyType == Type.BINARY) key = Base64.decode(doc.getTextValue(nkey));
			else key = Converter.convert(keyType, doc.getTextValue(nkey));
			
			if (valueType == Type.OBJECT) val = createBeanContent(nval);
			else if (valueType == Type.BINARY) val = Base64.decode(doc.getTextValue(nval));
			else val = Converter.convert(valueType, doc.getTextValue(nval));
			
			attrMap.put(key, val);
		}
		bean.invoke(data, attr.getPath(), true, attrMap);
	}
	
	protected void handleBeanData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Node nval = doc.getChildNode(nattr, "value");
		
		for (Node sattr : doc.getChildNodes(nval, "attribute")) 
			try {
				handleAttribute(sattr, bean, attr.getChild(doc.getAttribute(sattr, "name")), data);
			} catch(Exception e) {
				throw new InvocationTargetException(e);
			}
		
	}
	
	protected void handleData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Node nval = doc.getChildNode(nattr, "value");
		String val = doc.getTextValue(nval);
		Object attrData = null;
		
		if (attr.getDataType() == Type.BINARY) attrData = Base64.decode(val);
		else attrData = Converter.convert(attr.getDataType(), val);
		
		bean.invoke(data, attr.getPath(), true, attrData);
	}
	
	protected void handleAttribute(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Node[] cnl = doc.getChildNodes(nattr, "value");
		if (cnl.length == 0) return;
//		Attribute attr = bean.getChild(doc.getAttribute(nattr, "name"));
		if (attr == null) throw new IllegalArgumentException("Attribute \""+doc.getAttribute(nattr, "name")+"\" unknown in bean \""+bean.getName()+"\".");
		
		if (attr.isCollectionType()) handleCollectionData(nattr, bean, attr, data);
		else if (attr.isArrayType()) handleArrayData(nattr, bean, attr, data);
		else if (attr.isMapType()) handleMapData(nattr, bean, attr, data);
		else if (attr.isBeanType()) handleBeanData(nattr, bean, attr, data);
		else handleData(nattr, bean, attr, data);
	}
	
	protected Object createBeanContent(Node nbean) throws IllegalAccessException, InstantiationException, InvocationTargetException {
		if (!doc.hasAttribute(nbean, "class")) throw new IllegalArgumentException("Bean-Class is not set.");
	
		try {
			Class<?> clsbean = Class.forName(doc.getAttribute(nbean, "class"));
		
			Object obj = clsbean.newInstance();
			Bean bean = new Bean(obj);
								
			for (Node nattr : doc.getChildNodes(nbean, "attribute"))
					handleAttribute(nattr, bean, bean.getChild(doc.getAttribute(nattr, "name")), obj);
		
			return obj;
		} catch(ClassNotFoundException cnfe) {
			throw new IllegalArgumentException("Bean-Class \""+doc.hasAttribute(nbean, "class")+"\" not found.");
		}
	}
	
	public Object[] readBean() throws IOException {
		try {
			NodeList nl = (NodeList)doc.evaluateXPath("/bean-content/bean", XPathConstants.NODESET);
			Object[] ret = new Object[nl.getLength()];
			
			for (int i=0;i<nl.getLength();i++) {
				Node nbean = nl.item(i);
				try {
					ret[i] = createBeanContent(nbean);
				} catch(Exception e) {
					throw new IOException("Error while read bean.", e);
				}
			}
			
			return ret;
		} catch(XPathExpressionException xpee) {
			throw new IOException("Error while read bean list.", xpee);
		}
	}
	
}
