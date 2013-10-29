package m41highway.recommend.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m41highway.recommend.api.CosineSimilarityCalculator;
import m41highway.recommend.model.CorelationMapping;
import m41highway.recommend.model.Item;
import m41highway.recommend.model.SimilarItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CosineSimilarityCalculatorImpl implements
		CosineSimilarityCalculator {
    
	private static Log log = LogFactory.getLog(CosineSimilarityCalculatorImpl.class);
	
    public static void main(String[] args){
 
    }
    

	private static CosineSimilarityCalculatorImpl instance;

    public static CosineSimilarityCalculatorImpl getInstance(){
        if (instance == null){
            instance = new CosineSimilarityCalculatorImpl();
        }
        return instance;
    }
	
    
    private CorelationMapping corelationMapping;
    
	private CosineSimilarityCalculatorImpl(){
	    this.corelationMapping = CorelationMapping.getInstance();
	}
	
    @Override
    public List<SimilarItem> calculate(List<Item> itemList){
	    if(itemList == null || itemList.size() < 2){
	        return null;
	    }
	    	    
	    List<SimilarItem> similarItemList = new ArrayList<SimilarItem>();
	    int size = itemList.size();
	    SimilarItem similarItem = null;
	    Item sourceItem = null;
	    Item targetItem = null;
	    double score;
	    for(int i = 0; i < size; i++){
	        sourceItem = itemList.get(i);
	        for(int j = 0; j < size; j++){
	            targetItem = itemList.get(j);
	            if(sourceItem.getName().equals(targetItem.getName())){
	                continue;
	            }
	            
	            // Check if two item is corelation. 
	            if(corelationMapping.isCorelation(sourceItem.getServiceType(), targetItem.getServiceType())){
	                score = calculate(sourceItem, targetItem);
	                similarItem = new SimilarItem(sourceItem.getName(), targetItem.getName(), score);
	                similarItemList.add(similarItem);
	            }
	        }
	    }
	    return similarItemList;
	}
	
	private double calculate(Item sourceItem, Item targetItem){
	    HashMap<String, String[]> sourceVectors = sourceItem.getVectors();
	    HashMap<String, String[]> targetVectors = targetItem.getVectors();
	    
	    String sourceServiceType = sourceItem.getServiceType();
	    String targetServiceType = targetItem.getServiceType();
	    List<CorelationMapping.AttributeCorelation> list =  corelationMapping.getAttributeCorelations(sourceServiceType, targetServiceType);
	    
	    double totalScore = 0;
        for(CorelationMapping.AttributeCorelation corelation : list){
            String sourceAttribute = corelation.getSourceAttribute();
            String targetAttribute = corelation.getTargetAttribute();
            String[] sourceVector = sourceVectors.get(sourceAttribute);
            String[] targetVector = targetVectors.get(targetAttribute);
            double score = calculate(sourceVector, targetVector);
            double weight = corelation.getWeight();
            totalScore = totalScore + score * weight;
        }	    

	    return totalScore;
	}
	
	private double calculate(String[] vector1, String[] vector2) {
	    Map<String, Integer> wordCount1 = calculateWordCount(vector1);
	    Map<String, Integer> wordCount2 = calculateWordCount(vector2);
	    
	    //log.info(wordCount1.toString());
	    //log.info(wordCount2.toString());
	    
        Set<String> wordSet = new HashSet<String>();
        wordSet.addAll(wordCount1.keySet());
        wordSet.addAll(wordCount2.keySet());
        for(String word : wordSet){
            if(!wordCount1.containsKey(word)) wordCount1.put(word, 0);
            if(!wordCount2.containsKey(word)) wordCount2.put(word, 0);
        }
        
        double sclar = 0, norm1 = 0, norm2 = 0;
        for(String word : wordSet) sclar += wordCount1.get(word) * wordCount2.get(word);
        for(String word : wordSet) norm1 += wordCount1.get(word) * wordCount1.get(word);
        for(String word : wordSet) norm2 += wordCount2.get(word) * wordCount2.get(word);
        double cosine = sclar / Math.sqrt(norm1 * norm2);
        return cosine;
	}
	
	
    private Map<String, Integer> calculateWordCount(String[] vector){
        Map<String, Integer> wordCount = new HashMap<String, Integer>();
        if (vector != null) {
        	for(String word : vector){
        		if(wordCount.containsKey(word)){
        			Integer count = wordCount.get(word);
        			count++;
        			wordCount.put(word, count);
        		} else {
        			wordCount.put(word, 1);
        		}
        	}
        }else{
        	log.info("vector is null");
        }
        return wordCount;
    }	

}
