package com.dmx.cep.recommend.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmx.cep.recommend.model.Attribute;
import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.Statistics;

public class KeywordFilterImpl {

	private static Log log = LogFactory.getLog(KeywordFilterImpl.class);
	
	private static KeywordFilterImpl keywordFilterImpl;
	
	private ConfigurationImpl configurationImpl;
	
	private KeywordFilterImpl(){
		configurationImpl = configurationImpl.getInstance();
	}
	
	public static KeywordFilterImpl getInstance(){
		if (keywordFilterImpl == null){
			keywordFilterImpl = new KeywordFilterImpl();
		}
		
		return keywordFilterImpl;
	}
	
	/**
	 * To separate the important and not important keyword by using the threshold such that certain percentage (threshold) of total keyword will be classified 
	 * as important keyword, and the remaining keyword will be classified as not important keyword
	 * @param item
	 * @param threshold
	 * @return
	 */
	public Item filter(Item item, double threshold) {		
		Hashtable <String, Attribute> atts = item.getAttribute();
		Iterator it = atts.keySet().iterator();
		
		log.debug("Important keyword for " + item.getName());
		
		while (it.hasNext()){
			Statistics importantWords = null;
			
			String key = (String)it.next();
			Attribute att = atts.get(key);
			
			//if (att.isRequireKeywordExtraction() == true ){
			
			if ( configurationImpl.isRequirdTokenization( att.getName())){
				HashMap <String, Statistics> newStats = new HashMap <String, Statistics> ();								
				HashMap <String, Statistics> stats = att.getTfidf();
				
				Map<String,String> sortedMap =  sortByComparator(stats);
				long size = sortedMap.size();
				double filter = size * (1 - threshold);
				int cnt = 0;
				
		        for (Map.Entry entry : sortedMap.entrySet()) {
		        	cnt++;
		        	Statistics s = 	(Statistics)entry.getValue();
		        			        	
		        	if (cnt > filter){
		        		log.debug("important word " + entry.getKey() + " " + s.getTfidf());
		        		newStats.put((String)entry.getKey(), s);
		        	}else{
		        		log.debug("Not important word " + entry.getKey() + " " + s.getTfidf());
		        	}
		        }
				
				att.setImportantWords(newStats);				
				
			}
		}		
		return item;
	}
	
	 private static Map sortByComparator(Map unsortMap) {
		 
	        List list = new LinkedList(unsortMap.entrySet());
	 
	        //sort list based on comparator
	        Collections.sort(list, new Comparator() {
	             public int compare(Object o1, Object o2) {
	            	 Statistics s1 = (Statistics)((Map.Entry) (o1)).getValue();
	            	 Statistics s2 = (Statistics)((Map.Entry) (o2)).getValue();
	            	 Double d1 = new Double(s1.getTfidf());
	            	 Double d2 = new Double(s2.getTfidf());
	            	 //return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
	            	 return ((Comparable)d1).compareTo(d2);
	             }
		});
	 
	        //put sorted list into map again
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
		     Map.Entry entry = (Map.Entry)it.next();
		     sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	   }	
	
}
