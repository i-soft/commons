package org.isf.commons.test.bean;

import java.io.File;

import org.isf.commons.io.BeanWriter;

public class TestBeanWriter {

	public static void main(String[] args) throws Exception {
		Person p = Person.createPerson();
		
		File f = new File("/tmp/beanwriter-test1.xml");
		BeanWriter bw = new BeanWriter(f);
		bw.writeBean(p);
	}
	
}
