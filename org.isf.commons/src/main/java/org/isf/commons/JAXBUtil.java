package org.isf.commons;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class JAXBUtil {

	public static <T> T unmarshall(Class<T> clazz, File file) throws JAXBException, FileNotFoundException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller u = jc.createUnmarshaller();
		return clazz.cast(u.unmarshal(new FileInputStream(file)));
	}
	
	public static void marshall(Object obj, File file) throws JAXBException, FileNotFoundException {
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		Marshaller ms = jc.createMarshaller();
		ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ms.marshal(obj, new FileOutputStream(file));
	}

	public static <T> T unmarshall(Class<T> clazz, InputStream in) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller u = jc.createUnmarshaller();
		return clazz.cast(u.unmarshal(in));
	}
	
	public static void marshall(Object obj, OutputStream out) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(obj.getClass());
		Marshaller ms = jc.createMarshaller();
		ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		ms.marshal(obj, out);
	}
	
	public static <T> T unmarshall(Class<T> clazz, Document doc) throws JAXBException, TransformerConfigurationException, TransformerException, IOException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer trans = factory.newTransformer();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			trans.transform(new DOMSource(doc), new StreamResult(out));
			
			return unmarshall(clazz, new ByteArrayInputStream(out.toByteArray()));
		} finally {
			out.close();
		}
	}
	
	public static Document marshall(Object obj) throws JAXBException, IOException, ParserConfigurationException, SAXException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(); 
		try {
			marshall(obj, out);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			return builder.parse(new ByteArrayInputStream(out.toByteArray()));
		} finally {
			out.close();
		}
	}

	public static <T> T unmarshall(Class<T> clazz, String value) throws JAXBException, UnsupportedEncodingException {
		return unmarshall(clazz, new ByteArrayInputStream(value.getBytes("UTF-8")));
	}
	
	public static String marshallToString(Object obj) throws JAXBException, UnsupportedEncodingException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			marshall(obj, out);
			return new String(out.toByteArray(), "UTF-8");
		} finally {
			out.close();
		}
	}
	
}
