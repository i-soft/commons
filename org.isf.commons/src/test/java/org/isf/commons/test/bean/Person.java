package org.isf.commons.test.bean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Person {

	private String firstname;
	private String name;
	private int age;
	private Date birthdate;
	private byte[] image;
	private Address address;
	private Properties properties = new Properties();
	private Map<String, Address> customMap = new HashMap<String, Address>();
	private ArrayList<Address> addresses = new ArrayList<Address>();
	private Address[] arrayAddr = new Address[0];
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public Map<String, Address> getCustomMap() {
		return customMap;
	}
	public void setCustomMap(Map<String, Address> customMap) {
		this.customMap = customMap;
	}	
	public ArrayList<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(ArrayList<Address> addresses) {
		this.addresses = addresses;
	}
	public Address[] getArrayAddr() {
		return arrayAddr;
	}
	public void setArrayAddr(Address[] arrayAddr) {
		this.arrayAddr = arrayAddr;
	}
	
	public static Person createPerson() {
		Person p = new Person();
		p.setFirstname("Milo");
		p.setName("Pape");
		p.setAge(2);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2012);
		c.set(Calendar.MONTH, 7);
		c.set(Calendar.DAY_OF_MONTH, 25);
		p.setBirthdate(c.getTime());
		p.setImage(new byte[] {12,-128,102,127,52,112,15,-56,-23});
		
		p.getProperties().setProperty("Haarfarbe", "blond");
		p.getProperties().setProperty("Augenfarbe", "blau");
		
		Address a = new Address();
		a.setStreet("Alte Dorfstraße 15");
		a.setZip("27404");
		a.setCity("Badenstedt");
		p.setAddress(a);
		
		Address a2 = new Address();
		a2.setStreet("Eschenweg 5");
		a2.setZip("27404");
		a2.setCity("Zeven");
		
		p.getCustomMap().put("addr1", a);
		p.getCustomMap().put("addr2", a2);
		
		p.getAddresses().add(a);
		p.getAddresses().add(a2);
		
		p.setArrayAddr(new Address[] {a,a2});
		
		return p;
	}
	
	public String toString() {
//		String ret = this.getClass().getName()+":\n";
		String ret = "";
		ret+="  firstname: "+getFirstname()+"\n";
		ret+="  name: "+getName()+"\n";
		ret+="  age: "+getAge()+"\n";
		ret+="  birthdate: "+getBirthdate()+"\n";
		ret+="  image: ";
		for (int i=0;i<getImage().length;i++)
			ret+=(i==0?"":",")+getImage()[i];
		ret+="\n";
		if (getAddress() != null) {
			ret+="\n  address:\n";
			ret+=getAddress().toString()+"\n";
		}
		
		ret+="  customMap:\n";
		for (Object key : getCustomMap().keySet()) {
			ret+="    entry: "+key+"="+getCustomMap().get(key)+"\n";
		}
		
		ret+="  properties:\n";
		for (Object key : getProperties().keySet()) {
			ret+="    entry: "+key+"="+getProperties().get(key)+"\n";
		}
		
		return ret;
	}
}
