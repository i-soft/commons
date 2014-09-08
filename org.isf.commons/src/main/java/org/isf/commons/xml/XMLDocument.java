package org.isf.commons.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.xerces.parsers.DOMParser;
import org.isf.commons.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLDocument {

	private Document document;
	private XPathFactory xPathFactory;
	private XPath xPath;
	
	public void parseDocument(String docfile) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(docfile);
		setDocument(parser.getDocument());
	}
	
	public void parseDocument(InputStream input) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(input));
		setDocument(parser.getDocument());
	}
	
	public void parseDocumentFromStringSource(String source) throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		parser.parse(new InputSource(new StringReader(source)));
		setDocument(parser.getDocument());
	}
	
	protected Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		return builder.newDocument(); 
	}
	
	public void createDocument(String rootElement) throws ParserConfigurationException {
		Document doc = createDocument();
		Element root = doc.createElement(rootElement);
		doc.appendChild(root);
		setDocument(doc);
	}
	
	public void saveDocument(String docfile) throws IOException, TransformerException {
		saveDocument(new File(docfile));
	}
	
	public void saveDocument(File file) throws IOException, TransformerException {
		if (file.exists()) file.delete();
//		TransformerFactory transfact = TransformerFactory.newInstance();
//		Transformer trans = transfact.newTransformer();
//		trans.transform(new DOMSource(getDocument()), new StreamResult(file));
		saveDocument(new FileOutputStream(file));
	}
	
	public void saveDocument(OutputStream out) throws IOException, TransformerException {
		TransformerFactory transfact = TransformerFactory.newInstance();
		Transformer trans = transfact.newTransformer();
		trans.transform(new DOMSource(getDocument()), new StreamResult(out));
	}
	
	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	public Element getRoot() { return getDocument().getDocumentElement(); }
	
	/* XPath Section */
	
	protected XPathFactory getXPathFactory() {
		if (xPathFactory == null) xPathFactory = XPathFactory.newInstance();
		return xPathFactory; 
	}
	protected XPath getXPath() { 
		if (xPath == null) xPath = getXPathFactory().newXPath();
		return xPath; 
	}
	
	public Object evaluateXPath(String path, Node node, QName qname) throws XPathExpressionException {
		if (path.length() == 0) return node;
		return getXPath().evaluate(path, node, qname);
	}
	
	public Object evaluateXPath(String path, QName qname) throws XPathExpressionException {
		return evaluateXPath(path, getDocument(), qname);
	}
	
	public Node[] findNodes(String nodeName) {
		NodeList nl = getDocument().getElementsByTagName(nodeName);
		Node[] ret = new Node[nl.getLength()];
		for (int i=0;i<nl.getLength();i++)
			ret[i] = nl.item(i);
		return ret;
	}
	
	public Node getChildNode(Node node, String nodeName) {
		return XMLUtil.getChildNode(node, nodeName);
	}
	
	public Node[] getChildNodes(Node node, String nodeName) {
		return XMLUtil.getChildNodes(node, nodeName);
	}
	
	public Node createNode(Node node, String nodeName) {
		Node ret = getDocument().createElement(nodeName);
		node.appendChild(ret);
		return ret;
	}
	
	public boolean nodeExists(Node node, String nodeName) {
		return getChildNodes(node, nodeName).length > 0;
	}
	
	public boolean hasAttribute(Node node, String attrName) { return XMLUtil.hasAttribute(node, attrName); }
	public String getAttribute(Node node, String attrName) { return XMLUtil.getAttribute(node, attrName); }
	public void setAttribute(Node node, String attrName, String value) { XMLUtil.setAttribute(node, attrName, value); }
	
	public String getTextValue(Node node) { return XMLUtil.getTextValue(node); }
	public void setTextValue(Node node, String text) { XMLUtil.setTextValue(node, text); }
	
	public String getCDATAValue(Node node) { return getTextValue(node); }
	public void setCDATAValue(Node node, String text) { XMLUtil.setCDataValue(node, text); }
	
}
