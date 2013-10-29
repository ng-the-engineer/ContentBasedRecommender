package com.dmx.cep.recommend.model;

import java.util.HashMap;
import java.util.Hashtable;

public class Item {
		
	// 0. Service Type;
	private String serviceType;
	
	// 1. Item name 
	private String name;
	
	// 2. dynamic attributes of Item, e.g. name, director, actor, description, etc
	private Hashtable <String, Attribute> attribute; // arg: attribute name, attribute object 
		
	// 3. Each attribute has own vector
	private HashMap <String, String[]> vectors; // arg: attribute, vector 
	
	public Hashtable <String, Attribute> getAttribute() {
		return attribute;
	}
	public void setAttribute(Hashtable <String, Attribute> attribute) {
		this.attribute = attribute;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap <String, String[]> getVectors() {
		return vectors;
	}
	public void setVectors(HashMap <String, String[]> vectors) {
		this.vectors = vectors;
	}

		
}
