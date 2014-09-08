package org.isf.commons.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.isf.commons.bean.Attribute;
import org.isf.commons.bean.Bean;
import org.isf.commons.bean.Converter;
import org.isf.commons.bean.Type;
import org.isf.commons.codec.Base64;
import org.isf.commons.xml.XMLDocument;
import org.w3c.dom.Node;

import com.google.common.collect.Iterables;

public class BeanWriter {
	
	public static final String ROOT = "bean-content";
	
	private XMLDocument doc;
	
	private File ffile;
	private String fname;
	private OutputStream fstream;
	
	protected BeanWriter() throws ParserConfigurationException {
		doc = new XMLDocument();
		doc.createDocument(ROOT);
	}
	
	public BeanWriter(File file) throws ParserConfigurationException {
		this();
		this.ffile = file;
	}
	
	public BeanWriter(String filename) throws ParserConfigurationException {
		this();
		this.fname = filename; 
	}
	
	public BeanWriter(OutputStream out) throws ParserConfigurationException {
		this();
		this.fstream = out;
	}
	
	@SuppressWarnings("unchecked")
	protected void handleCollectionData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		doc.setAttribute(nattr, "typeClass", attr.getType().getName());
		
		Class<?> valueTypeClass = Object.class;
		if (attr.getGenericTypes().length >= 1) 
			valueTypeClass = attr.getGenericTypes()[0];
		Type valueType = Type.getTypePerClass(valueTypeClass);
		
		doc.setAttribute(nattr, "valueType", valueType.name());
		doc.setAttribute(nattr, "valueTypeClass", valueTypeClass.getName());
		
		Collection<Object> coll = (Collection<Object>)bean.invoke(data, attr.getPath(), true);
		for (int i=0;i<coll.size();i++) {
			Node n = doc.createNode(nattr, "value");
			doc.setAttribute(n, "index", String.valueOf(i));
			Object val = Iterables.get(coll, i);
			if (valueType == Type.OBJECT) readBeanContent(n, new Bean(val), val);
			else handleSimpleData(n, valueType, val);
		}
	}
	
	protected void handleArrayData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Class<?> valueTypeClass = attr.getType().getComponentType();
		doc.setAttribute(nattr, "typeClass", valueTypeClass.getName());
		Type valueType = Type.getTypePerClass(valueTypeClass);
		
		doc.setAttribute(nattr, "valueType", valueType.name());
		doc.setAttribute(nattr, "valueTypeClass", valueTypeClass.getName());
		
		Object arr = bean.invoke(data, attr.getPath(), true);
		for (int i=0;i<Array.getLength(arr);i++) {
			Node n = doc.createNode(nattr, "value");
			doc.setAttribute(n, "index", String.valueOf(i));
			Object val = Array.get(arr, i);
			if (valueType == Type.OBJECT) readBeanContent(n, new Bean(val), val);
			else handleSimpleData(n, valueType, val);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void handleMapData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		doc.setAttribute(nattr, "typeClass", attr.getType().getName());
		
		Class<?> keyTypeClass = String.class;
		Class<?> valueTypeClass = String.class;
		if (attr.getGenericTypes().length >= 2) {
			keyTypeClass = attr.getGenericTypes()[0];
			valueTypeClass = attr.getGenericTypes()[1];
		}
		Type keyType = Type.getTypePerClass(keyTypeClass);
		Type valueType = Type.getTypePerClass(valueTypeClass);
				
		doc.setAttribute(nattr, "keyType", keyType.name());
		doc.setAttribute(nattr, "keyTypeClass", keyTypeClass.getName());
		doc.setAttribute(nattr, "valueType", valueType.name());
		doc.setAttribute(nattr, "valueTypeClass", valueTypeClass.getName());
		
		Map<Object, Object> attrMap = (Map<Object, Object>)bean.invoke(data, attr.getPath(), true);
		for (Object key : attrMap.keySet()) {
			Node n = doc.createNode(nattr, "value");
			Object val = attrMap.get(key);
			Node kn = doc.createNode(n, "key");
			if (keyType == Type.OBJECT) readBeanContent(kn, new Bean(key), key);
			else handleSimpleData(kn, keyType, key);
			
			Node vn = doc.createNode(n, "value");
			if (valueType == Type.OBJECT) readBeanContent(vn, new Bean(val), val);
			else handleSimpleData(vn, valueType, val);
		}
	}
	
	protected void handleBeanData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		doc.setAttribute(nattr, "typeClass", attr.getType().getName());
		
		Object attrData = bean.invoke(data, attr.getPath(), true);
		if (attrData == null) return;
		
		Node n = doc.createNode(nattr, "value");
		for (Attribute at : attr.getChildren())
			createAttributeNode(n, bean, at, data);
	}
	
	protected void handleSimpleData(Node n, Type type, Object data) {
		switch (type) {
			case STRING:
				doc.setCDATAValue(n, Converter.convert(String.class, data));
				break;
			case BINARY:
				doc.setCDATAValue(n, Base64.encode((byte[])data));
				break;
			default:
				doc.setTextValue(n, Converter.convert(String.class, data));
		}
	}
	
	protected void handleData(Node nattr, Bean bean, Attribute attr, Object data) throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Object attrData = bean.invoke(data, attr.getPath(), true);
		if (attrData == null) return;
		Node n = doc.createNode(nattr, "value");
		handleSimpleData(n, attr.getDataType(), attrData);	
	}
	
	protected void createAttributeNode(Node parent, Bean bean, Attribute attr, Object data)  throws InvocationTargetException, IllegalAccessException, InstantiationException {
		Node n = doc.createNode(parent, "attribute");
		doc.setAttribute(n, "name", attr.getName());
		doc.setAttribute(n, "type", attr.getDataType().name());
//		doc.setAttribute(n, "typeClass", attr.getType().getName());
		
		if (attr.isCollectionType()) handleCollectionData(n, bean, attr, data);
		else if (attr.isArrayType()) handleArrayData(n, bean, attr, data);
		else if (attr.isMapType()) handleMapData(n, bean, attr, data);
		else if (attr.isBeanType()) handleBeanData(n, bean, attr, data);
		else handleData(n, bean, attr, data);
	}
	
	protected void readBeanContent(Node n, Bean bean, Object data)  throws InvocationTargetException, IllegalAccessException, InstantiationException {
		doc.setAttribute(n, "name", bean.getDisplayName());
		doc.setAttribute(n, "class", bean.getName());
		
		for (Attribute attr : bean.getChildren())
			createAttributeNode(n, bean, attr, data);
	}
	
	public void writeBean(Object ... o) throws IOException {
		for (Object obj : o) {			
			Node root = doc.getRoot();
			Node n = doc.createNode(root, "bean");
			Bean bean = new Bean(obj);
			try {
				readBeanContent(n, bean, obj);
			} catch(Exception e) {
				throw new IOException("Error while read bean-content from \""+bean.getName()+"\".", e);
			}
		}
		
		
		if (ffile != null)
			try {
				doc.saveDocument(ffile);
			} catch(TransformerException te) {
				throw new IOException("Error while save to file \""+ffile.getAbsolutePath()+"\".", te);
			}
		else if (fname != null) 
			try {
				doc.saveDocument(fname);
			} catch(TransformerException te) {
				throw new IOException("Error while save to file \""+fname+"\".", te);
			}
		else if (fstream != null) {
			try {
				doc.saveDocument(fstream);
			} catch(TransformerException te) {
				throw new IOException("Error while save to stream.", te);
			}
			fstream.close();
		}
	}
	
}
