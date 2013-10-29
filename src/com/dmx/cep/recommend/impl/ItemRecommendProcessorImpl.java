package com.dmx.cep.recommend.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dmx.cep.recommend.api.ItemRecommendProcessor;
import com.dmx.cep.recommend.demo.RecommendDemo;
import com.dmx.cep.recommend.api.ItemSimilarityRepository;
import com.dmx.cep.recommend.model.Attribute;
import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.SimilarItem;
import com.dmx.cep.recommend.model.SimilarScore;
import com.dmx.cep.recommend.model.Statistics;

public class ItemRecommendProcessorImpl implements ItemRecommendProcessor {
	
	private static Log log = LogFactory.getLog(ItemRecommendProcessorImpl.class);
	
	private static double THRESHOLD = 0.3 ;	
	
	private Hashtable <String, Item> items; 
	private DataImporterImpl dataImporter;
	private TfidfCalculatorImpl calculator;
	private KeywordFilterImpl keywordFilter;
	private KeywordExtractorImpl keywordExtractor;
	private CosineSimilarityCalculatorImpl cosineSimilarityCalculator;
	private ItemSimilarityRepository itemSimilarityRepository;
	private ConfigurationImpl configurationImpl;
	
	public ItemSimilarityRepository getItemSimilarityRepository(){
		return this.itemSimilarityRepository;
	}
	
	public void setDataImporter(DataImporterImpl dataImporter){
		this.dataImporter = dataImporter;
	}
	
	public void setTfidfCalculator (TfidfCalculatorImpl calculator){
		this.calculator = calculator;
	}
	
	public void setKeywordFilter(KeywordFilterImpl keywordFilter){
		this.keywordFilter = keywordFilter;
	}
	
	public void setKeywordExtractor(KeywordExtractorImpl keywordExtractor){
		this.keywordExtractor = keywordExtractor;
	}
	
    public void setCosineSimilarityCalculator(CosineSimilarityCalculatorImpl cosineSimilarityCalculator)
    {
        this.cosineSimilarityCalculator = cosineSimilarityCalculator;
    }
    
	public ConfigurationImpl getConfigurationImpl() {
		return configurationImpl;
	}

	public void setConfigurationImpl(ConfigurationImpl configurationImpl) {
		this.configurationImpl = configurationImpl;
	}

    public ItemRecommendProcessorImpl(DataImporterImpl dataImporter, TfidfCalculatorImpl calculator, KeywordFilterImpl keywordFilter,
	                                  KeywordExtractorImpl keywordExtractor, CosineSimilarityCalculatorImpl cosineSimilarityCalculator,
	                                  ConfigurationImpl configurationImpl){
		this.setDataImporter(dataImporter);
		this.setTfidfCalculator(calculator);
		this.setKeywordFilter(keywordFilter);
		this.setKeywordExtractor(keywordExtractor);
		this.setCosineSimilarityCalculator(cosineSimilarityCalculator);
		this.setConfigurationImpl(configurationImpl);
	}
	
	public boolean process(){
		boolean status = false;
		
		// 1. Get data
		items = dataImporter.getData();
		
		// 2. Transform data, i.e. tokenize
		Iterator <String> itor = items.keySet().iterator();
		while (itor.hasNext()){
			String key = itor.next();
			Item item = items.get(key);
			item = this.tfidfTransform(item);
		}
		
		// 3. tfidf
		items = calculator.calculate(items);
		
		// 4. filter out weak keywords
		Hashtable <String, Item> tempItems = new Hashtable <String, Item> ();

		Iterator <String> itemsItor = items.keySet().iterator();
		while (itemsItor.hasNext()){
			String key = itemsItor.next();
			Item item = items.get(key);
			item = keywordFilter.filter(item, THRESHOLD);

			tempItems.put(key, item);
		}
		items = tempItems;
		
		// 5. cosine similarity (tokenize desc field in item object)
		Iterator itr = items.keySet().iterator();
		while (itr.hasNext()){
			
			String key = (String)itr.next();
			Item item = items.get(key);
			
			//System.out.println();
			//System.out.println(item.getName());
			log.info("========================================================================");
			log.info(item.getName());
			
			HashMap <String, String[]> vectors = new HashMap <String, String[]> ();
			
			Iterator itemItr = item.getAttribute().keySet().iterator();
			while(itemItr.hasNext()){
								
				String itemKey = (String)itemItr.next();
				Attribute attr = item.getAttribute().get(itemKey);
				
				if (attr.getImportantWords() != null){
					
					//Iterator attrItr = attr.getImportantWords().keySet().iterator();
					Iterator attrItr = attr.getImportantWords().entrySet().iterator();
					
					//String [] vector = new String [attr.getImportantWords().keySet().size()];
					String [] vector = new String [attr.getImportantWords().entrySet().size()];
					
					int i=0;
					while (attrItr.hasNext()){
						//String attrKey = (String)attrItr.next();
						Map.Entry me = (Map.Entry)attrItr.next();
						//Statistics stats = attr.getImportantWords().get(attrKey);
						Statistics stats = (Statistics)me.getValue();
						
						// put to vector
						vector[i++] = stats.getKeyword();						
					}
					//System.out.println( attr.getName() + ": " + Arrays.toString(vector));
					log.info(attr.getName() + ": " + Arrays.toString(vector));
					
					vectors.put(attr.getName(), vector);
					
					
				}else{
					
					StringTokenizer str = new StringTokenizer(attr.getValue(), ";");
					String [] vector = new String [str.countTokens()];
					int i=0;
					while(str.hasMoreElements()){
						vector[i++] = str.nextToken();
					}
					//System.out.println(attr.getName() + ": " +  Arrays.toString(vector));
					log.info(attr.getName() + ": " +  Arrays.toString(vector));
					
					vectors.put(attr.getName(), vector);
				}
				
				item.setVectors(vectors);
			}			
		}
		
		List<Item> itemList = new ArrayList<Item>();
		itemList.addAll(items.values());
		List<SimilarItem> similarItemList = cosineSimilarityCalculator.calculate(itemList);
		
		System.out.println();
		System.out.println("Cosine Similarity:");
		for(SimilarItem similarItem : similarItemList){
		    System.out.println(similarItem.getSourceName() + " -> " + similarItem.getTargetName() + " : " + similarItem.getScore());
		    log.info(similarItem.getSourceName() + " -> " + similarItem.getTargetName() + " : " + similarItem.getScore());
		}
		
		this.itemSimilarityRepository = new ItemSimilarityRepositoryImpl(similarItemList);
		
		return status;
	}
	
	@Override
	public List<SimilarScore> getSimilarItem(String sourceItemName){
	    return this.itemSimilarityRepository.getSimilarItems(sourceItemName);
	}

//	@Override
//	public Hashtable<String, Item> inputData(String sourcePath) {
//		// Get Data from XML
//		// Set the name and attributes property of Item object 
//		return null;
//	}
//
//	@Override
//	public Hashtable<String, Item> inputData() {
//		// Get Data from Database 
//		// Set the name and attributes property of Item object 
//		return null;
//	}

	
	private Item tfidfTransform(Item item) {
		
		HashMap <String, Statistics> tfidf = new HashMap <String, Statistics> ();
		Hashtable <String, Attribute> newAttributes = new Hashtable <String, Attribute> ();
		// TODO Auto-generated method stub
		// loop item.attribute
		
		if (item.getAttribute() != null){
			
			Iterator <String> itor = item.getAttribute().keySet().iterator();
			while (itor.hasNext()){
				String key = itor.next();
				Attribute attribute = item.getAttribute().get(key);
				
				if ( configurationImpl.isRequirdTokenization( attribute.getName())){
					if (attribute.getValue() != null) {
						ArrayList <String> str = keywordExtractor.tokenize(attribute.getValue());
						
						attribute.setRawWordList(str);
						
						
//						Iterator itr = str.iterator();
//						while (itr.hasNext()){
//							String keyword = (String)itr.next();
//							tfidf.put(keyword, new Statistics(keyword, 0.0, 0.0));
//						}	
//						
//						// update attribute 's tfidf
						attribute.setTfidf(tfidf);
					}
				}else{		
					if (attribute.getValue() != null) {
						
						// simply tokenize
						//while (itr.hasNext()){
						//tfidf.put(keyword, new Statistics(attribute.getValue(), 0.0, 0.0));
						//}
					}					
				}
				
				// update item's attribute
				newAttributes.put(attribute.getName(), attribute);
			}
			item.setAttribute(newAttributes);
		}
		
		// set the transform result
		//item.setTdidf(tdidf);	
		return item;
	}




}
