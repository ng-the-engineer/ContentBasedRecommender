package com.dmx.cep.recommend.api;

import java.util.Hashtable;
import java.util.List;

import com.dmx.cep.recommend.impl.ItemRecommendProcessorImpl;
import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.SimilarScore;

public interface ItemRecommendProcessor {
	
	public boolean process();
	
	public List<SimilarScore> getSimilarItem(String sourceItemName);
	
	public ItemSimilarityRepository getItemSimilarityRepository();
	
}
