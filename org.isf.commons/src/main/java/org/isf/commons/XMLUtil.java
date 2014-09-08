package org.isf.commons;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XMLUtil {

	public static String getAttribute(Node node, String name) {
        String ret = "";
        NamedNodeMap map = node.getAttributes();
        for(int i=0;i<map.getLength();i++) {
            Node tmp = map.item(i);
            if (tmp.getNodeType() == Node.ATTRIBUTE_NODE) {
                if (tmp.getNodeName().equalsIgnoreCase(name)) {
                    ret = tmp.getNodeValue();
                }
            }
            if (ret.length() > 0) break;
        }
        return ret;
    }
    
    public static boolean hasAttribute(Node node, String name) {
    	NamedNodeMap map = node.getAttributes();
    	for(int i=0;i<map.getLength();i++) {
    		Node n = map.item(i);
    		if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
    			if (n.getNodeName().equals(name)) return true;
    		}
    	}
    	return false;
    }
    
    public static String getTextValue(Node n) {
    	if (n == null) return "";
        NodeList nl = n.getChildNodes();
        for (int i=0;i<nl.getLength(); i++) {
            Node x = nl.item(i);
            switch (x.getNodeType()) {
                case Node.TEXT_NODE:
                case Node.CDATA_SECTION_NODE:
                    return x.getNodeValue();
            }
        }
        return "";
    }
    
    public static Node getTextNode(Node n) {
    	if (n == null) return null;
    	NodeList nl = n.getChildNodes();
    	for (int i=0;i<nl.getLength(); i++) {
            Node x = nl.item(i);
            switch (x.getNodeType()) {
                case Node.TEXT_NODE: return x;       
                case Node.CDATA_SECTION_NODE: return x;                   
            }
        }
    	return null;
    }
    
    public static void setAttribute(Node n, String name, String value) {
    	try {
    		((Element)n).setAttribute(name, value);
    	} catch(Exception e) {
    	}
    }
    
    public static void setTextValue(Node n, String value) {
    	Element e = (Element)n;
    	Node txt = getTextNode(n);
    	if (txt == null) {    		
    		Text t = e.getOwnerDocument().createTextNode(value);
    		e.appendChild(t);
    	} else txt.setNodeValue(value);
    }
    
    public static void setCDataValue(Node n, String value) {
    	Element e = (Element)n;
    	Node txt = getTextNode(n);
    	if (txt == null) {    		
    		CDATASection t = e.getOwnerDocument().createCDATASection(value);
    		e.appendChild(t);
    	} else {
    		txt.setNodeValue(value);
    	}
    }
    
    public static Node getChildNode(Node node, String nodeName) {
    	Node[] narr = getChildNodes(node, nodeName);
    	return narr.length > 0 ? narr[0] : null;
    }
    
    public static Node[] getChildNodes(Node node, String nodeName) {
		NodeList nl = node.getChildNodes();
		List<Node> l = new ArrayList<Node>();
		for (int i=0;i<nl.getLength();i++)
			if (nodeName.equals(nl.item(i).getNodeName()))
				l.add(nl.item(i));
		return l.toArray(new Node[0]);
	}
    
    public static void saveXML(String docfile, Document doc) throws IOException {
        saveXML(new File(docfile), doc);
         
    }
    
    public static void saveXML(File file, Document doc) throws IOException {
        try {
            if(file.exists()) file.delete();
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(file)));
        } catch (Exception e) {
            throw new IOException(e.toString());
        }
    }
    
    public static Document stringToDocument(String xmlsrc) throws SAXException, ParserConfigurationException, IOException {
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    	DocumentBuilder builder = factory.newDocumentBuilder();
    	return builder.parse(new ByteArrayInputStream(xmlsrc.getBytes("UTF-8")));
    }
	
    public static String documentToString(Document doc) throws TransformerException {
    	TransformerFactory factory = TransformerFactory.newInstance();
    	Transformer trans = factory.newTransformer();
    	trans.setOutputProperty(OutputKeys.INDENT, "yes");
    	DOMSource src = new DOMSource(doc);
    	StringWriter sw = new StringWriter();
    	StreamResult result = new StreamResult(sw);
    	trans.transform(src, result);
    	return sw.toString();
    }
	
}
