package org.isf.commons.test.bean;

import java.io.File;

import org.isf.commons.io.BeanReader;
import org.isf.commons.io.BeanWriter;

public class TestBeanReader {

	public static void main(String[] args) throws Exception {
		File f = new File("/tmp/beanwriter-test1.xml");
		BeanReader reader = new BeanReader(f);
//		for (Object ret : reader.readBean()) {
//			System.out.println(ret.toString());
//		}
		Object[] beans = reader.readBean();
		
		f = new File("/tmp/beanwriter-test2.xml");
		BeanWriter writer = new BeanWriter(f);
		writer.writeBean(beans);
	}
	
}
