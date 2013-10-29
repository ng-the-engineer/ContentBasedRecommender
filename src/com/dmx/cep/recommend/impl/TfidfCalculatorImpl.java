package com.dmx.cep.recommend.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmx.cep.recommend.model.Attribute;
import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.Statistics;

public class TfidfCalculatorImpl {

	private static Log log = LogFactory.getLog(TfidfCalculatorImpl.class);
	
	private static TfidfCalculatorImpl tfidfCalculatorImpl;
	
	private ConfigurationImpl configurationImpl;
	
	private TfidfCalculatorImpl(){
		configurationImpl = configurationImpl.getInstance();
	}
	
	public static TfidfCalculatorImpl getInstance(){
		if (tfidfCalculatorImpl == null){
			tfidfCalculatorImpl = new TfidfCalculatorImpl();
		}
		return tfidfCalculatorImpl;
	}
	
	
	public Hashtable<String, Item> calculate(Hashtable<String, Item> items) {		
		
		long allUniqueWordCount = 0;  // The total number of unique word in the process
		long allWordCount = 0; // The total number of word in the process
		long allItemCount = items.size(); // The total number of items in the process
		Hashtable <String, Long> allKeywordMap = new Hashtable(); // the map of unique keyword with the appear count in all items
		
		HashMap <String, Hashtable<String, Long>> itemKeywordMap = new HashMap<String, Hashtable<String, Long>>();
		HashMap <String, Long> itemWordCount = new  HashMap <String, Long>(); // total WordCount per item
		
		Iterator <String> allItor =  items.keySet().iterator();
		while (allItor.hasNext()){
						
			long uniqueWordCount = 0;  // use to count the total number of unique word in each item
			long wordCount = 0; // use to count the total number of word in each item
			Hashtable <String, Long> keywordMap = new Hashtable(); // the map of unique keyword with the appear count in each item
			
			String key = allItor.next();
			Item item = items.get(key);
			
			log.debug(item.getName());
			
			Hashtable <String, Attribute> newAttributes = new Hashtable <String, Attribute> (); 
			
			Hashtable <String, Attribute> attributes = item.getAttribute();			
			Iterator <String> attrItor = attributes.keySet().iterator();
			
			while (attrItor.hasNext()){
				
				String k = attrItor.next();
				Attribute attr = attributes.get(k);
				
				//if (attr.isRequireKeywordExtraction() == true) {  // calculate only this flag is true
				if (attr.getName() == null || attr.getName().equals("")){
					log.error("attribute name is null");
				}
				
				if ( configurationImpl.isRequirdTokenization( attr.getName())){
					
					String keyword="";					
					
					HashMap <String, Statistics> tfidf = new HashMap <String, Statistics>();
					
					if (attr.getRawWordList() != null){
						Iterator rawItr = attr.getRawWordList().iterator();
						while (rawItr.hasNext() ){
							keyword = (String)rawItr.next();
							
							if (keywordMap.containsKey(keyword)){
								keywordMap.put(keyword, keywordMap.get(keyword)+1);								
							}else{
								keywordMap.put(keyword, new Long(1));
								tfidf.put(keyword, new Statistics(keyword, 0.0, 0.0));
								uniqueWordCount++;
							}
							wordCount++;
							
							if (allKeywordMap.containsKey(keyword)){	
								allKeywordMap.put(keyword, allKeywordMap.get(keyword)+1);								
							}else{
								allKeywordMap.put(keyword, new Long(1));
								allUniqueWordCount ++;
							}
							allWordCount ++;
						}
					}
					attr.setTfidf(tfidf);						
				}
				newAttributes.put(k, attr);				
			}
			item.setAttribute(newAttributes);
						
			log.info("");
			itemKeywordMap.put(item.getName(), keywordMap);		
			itemWordCount.put(item.getName(), wordCount);
		}
		
		
		// update tf idf
		allItor =  items.keySet().iterator();
		while (allItor.hasNext()){
			String key = allItor.next();
			Item item = items.get(key);
			
			Hashtable <String, Long> km = itemKeywordMap.get(item.getName());			
			Long wc = itemWordCount.get(item.getName());
						
			Hashtable <String, Attribute> attributes = item.getAttribute();
			Iterator <String> attrItor = attributes.keySet().iterator();
			
			while (attrItor.hasNext()){
				String k = attrItor.next();
				Attribute attr = attributes.get(k);
				
				//if (attr.isRequireKeywordExtraction() == true) {  // calculate only this flag is true
				if ( configurationImpl.isRequirdTokenization( attr.getName())){
					
					HashMap <String, Statistics> tfidf = attr.getTfidf();
										
					Iterator tfidfItor = tfidf.entrySet().iterator();
					while (tfidfItor.hasNext()){
						
						Map.Entry tk = (Map.Entry)tfidfItor.next();
												
						Statistics s = (Statistics)tk.getValue();
						double tf = 0.0;
						double idf = 0.0;
						double product = 0.0;
						
						Long hit = km.get((String)tk.getKey());
						
						if (wc > 0){
							tf = (double)hit / wc;							
						}
						s.setTf(tf);
						
						
						// appear in all item count
						int appearCount = 1;
						Iterator allKeywordMapItor = itemKeywordMap.entrySet().iterator();
						while (allKeywordMapItor.hasNext()){
							Map.Entry me = (Map.Entry)allKeywordMapItor.next();
							
							if (! me.getKey().equals(item.getName())){

								Hashtable<String, Long> kwm = (Hashtable<String, Long>)me.getValue();
								if (kwm.containsKey((String)tk.getKey())){
									appearCount ++;								
								}							
							}
						}
						
						if (allUniqueWordCount > 0){							
							idf = Math.log10((double) allItemCount/ appearCount);
						}
						s.setIdf(idf);
						
						product = tf * idf;
						s.setTfidf(product);
						
						log.info(s.getKeyword() + " hit=" + hit + " appearCount=" + appearCount + " tf=" + s.getTf() + " idf=" + s.getIdf() + " tfidf=" + s.getTfidf() + " ");																		

					}
				}
			}
		}
		
		
		return items;
	}
	
	

}
