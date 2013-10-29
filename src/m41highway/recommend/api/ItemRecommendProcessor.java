package m41highway.recommend.api;

import java.util.Hashtable;
import java.util.List;

import m41highway.recommend.impl.ItemRecommendProcessorImpl;
import m41highway.recommend.model.Item;
import m41highway.recommend.model.SimilarScore;


public interface ItemRecommendProcessor {
	
	public boolean process();
	
	public List<SimilarScore> getSimilarItem(String sourceItemName);
	
	public ItemSimilarityRepository getItemSimilarityRepository();
	
}
