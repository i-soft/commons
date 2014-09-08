package org.isf.commons.test.bean;

public class Address {

	private String street;
	private String zip;
	private String city;
	private String country;
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public String toString() {
		String ret = "";
		ret+="  street: "+getStreet()+"\n";
		ret+="  zip: "+getZip()+"\n";
		ret+="  city: "+getCity()+"\n";
		ret+="  country: "+getCountry()+"\n";
		return ret;
	}
	
}
