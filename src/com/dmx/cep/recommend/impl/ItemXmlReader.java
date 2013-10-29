package com.dmx.cep.recommend.impl;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dmx.cep.recommend.model.Attribute;
import com.dmx.cep.recommend.model.Item;

public class ItemXmlReader extends DefaultHandler {
	
	private static Log log = LogFactory.getLog(ItemXmlReader.class);
	
	public Item item = new Item();
	private Hashtable <String, Attribute> attList=  new Hashtable <String, Attribute> ();	
	private Attribute att= new Attribute();
	
	public ItemXmlReader(){
		item.setAttribute(attList);
	}
	
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {		
		//System.out.println("Start Element :" + qName);
		
		if (qName.equalsIgnoreCase("item")) {	
			int length = attributes.getLength();
			String name = null;
			String value = null;
			
			for (int i=0; i<length; i++) {			
				if ("servicetype".equalsIgnoreCase(attributes.getQName(i)) ){
					name = attributes.getQName(i);
					if (attributes.getValue(i) != null) {
						value = attributes.getValue(i);
						
						item.setServiceType(value);
						
						//System.out.println(value);
					}else{
						//System.out.println("Invalid element attribute");
					}
					
				}else if ("name".equalsIgnoreCase(attributes.getQName(i)) ){
					name = attributes.getQName(i);
					if (attributes.getValue(i) != null) {
						value = attributes.getValue(i);
						
						item.setName(value);
						
						//System.out.println(value);
					}else{
						//System.out.println("Invalid element attribute");
					}
					
				}
			}
			
		}
		
		if (qName.equalsIgnoreCase("attribute")) {	
			int length = attributes.getLength();
			String name = null;
			String value = null;
			String required =null;
			
			att = new Attribute();
			
			for (int i=0; i<length; i++) {
				if ("name".equalsIgnoreCase(attributes.getQName(i))){
					name = attributes.getValue(i);
					
					att.setName(name);
					
					//System.out.println(name);
				}
				if ("value".equalsIgnoreCase(attributes.getQName(i))){					
					value = attributes.getValue(i);	
					
					att.setValue(value);
					
					//System.out.println(value);
				}				
				if ("required".equalsIgnoreCase(attributes.getQName(i))){					
					required = attributes.getValue(i);	
					
					if (required.equalsIgnoreCase("true"))
						att.setRequireKeywordExtraction(true);
					else
						att.setRequireKeywordExtraction(false);
					
					//System.out.println(required);
				}
			}
			attList.put(name, att);
		
		}
	}
}
