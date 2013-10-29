package m41highway.recommend.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import m41highway.recommend.api.ItemRecommend;
import m41highway.recommend.api.ItemRecommendProcessor;
import m41highway.recommend.impl.ConfigurationImpl;
import m41highway.recommend.impl.CosineSimilarityCalculatorImpl;
import m41highway.recommend.impl.DataImporterImpl;
import m41highway.recommend.impl.ItemRecommendImpl;
import m41highway.recommend.impl.ItemRecommendProcessorImpl;
import m41highway.recommend.impl.KeywordExtractorImpl;
import m41highway.recommend.impl.KeywordFilterImpl;
import m41highway.recommend.impl.TfidfCalculatorImpl;
import m41highway.recommend.model.Item;
import m41highway.recommend.model.SimilarItem;
import m41highway.recommend.model.SimilarScore;
import m41highway.recommend.model.Statistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import com.dmx.cep.recommend.impl.HybridRecommend;

public class RecommendDemo {

	private static Log log = LogFactory.getLog(RecommendDemo.class);
	
	private Hashtable <String, Item> items; 
	private Hashtable <String, SimilarItem> similiarityMatrix; // arg: Item Name, Compared Item
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// init
		RecommendDemo recommendDemo = new RecommendDemo();						

		long start = System.currentTimeMillis();
		
		ItemRecommendProcessor processor = new ItemRecommendProcessorImpl(DataImporterImpl.getInstance(), 
			TfidfCalculatorImpl.getInstance(),
			KeywordFilterImpl.getInstance(),
		    KeywordExtractorImpl.getInstance(), 
		    CosineSimilarityCalculatorImpl.getInstance(), 
		    ConfigurationImpl.getInstance());
		processor.process();
		
		
		// Query results 
		String sourceItemName = "指环王：双塔奇兵";
		List<SimilarScore> list = processor.getSimilarItem(sourceItemName);
		System.out.println();
		System.out.println("The most similar item to " + sourceItemName);
		log.info("");
		log.info("The most similar item to " + sourceItemName);
		
		for(SimilarScore similarScore : list){
		    System.out.printf("%-10s (%.4f)\n", similarScore.getTargetItemName(), similarScore.getScore());
		    log.info(similarScore.getTargetItemName() + "   (" + similarScore.getScore() + ")");
		}
		System.out.println();
				
		long end = System.currentTimeMillis();		
		log.info("Time spent: " + (end - start) + "ms");
		
		
		/*
		// Hybrid demo
		String userid = "王小君";
		String itemid = "叶问";
		
		HybridRecommend hr = new HybridRecommend();
		hr.setItemSimilarityRepository(processor.getItemSimilarityRepository());
		
		log.info("");
		log.info("用戶[" + userid + "]在点播[" + itemid + "]中");
		List <String> serviceType = new ArrayList();
		serviceType.add("movie");
		serviceType.add("books");
		
		TreeMap tm = hr.recommend(userid, itemid, serviceType);
		
		
		
		Map<String,String> sortedMap =  sortByComparator(tm);
		
		log.info("");
		log.info("采用混合算法，系统根据VOD的相似程度推荐给用户[" + userid +"]，并进行跨媒体推荐。");
		log.info(sortedMap.toString());
		*/
		
	}

	
	
	private static Map sortByComparator(Map unsortMap) {
		 
        List list = new LinkedList(unsortMap.entrySet());
 
        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
            	 
            	 Double d1 = (Double)((Map.Entry)o1).getValue();
            	 Double d2 = (Double)((Map.Entry)o2).getValue();
            	             	
            	 int bigger = -1;
            	 int smaller = 1;
            	 int equal = 0;
            	 
            	 if (((Comparable)d1).compareTo(d2) > 1)
            		 return bigger;
            	 else if (((Comparable)d1).compareTo(d2) <1)
            		 return smaller;
            	 else if (((Comparable)d1).compareTo(d2) == 0)
            		 return equal;
            	 else 
            		 return 0;            	 
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
