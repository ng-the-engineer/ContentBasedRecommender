package m41highway.recommend.api;

import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import m41highway.recommend.impl.KeywordExtractorImpl;
import m41highway.recommend.model.Item;
import m41highway.recommend.model.Statistics;


public interface ItemRecommend {
	public Hashtable <String, Item> recommend(String itemName); // arg: itemName, Item object

	public TreeMap <String, Double>  recommend(String userid, String itemName, List <String> targetServiceType) ;
}
