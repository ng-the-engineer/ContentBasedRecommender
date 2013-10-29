package com.dmx.cep.recommend.api;

import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;

import com.dmx.cep.recommend.impl.KeywordExtractorImpl;
import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.Statistics;

public interface ItemRecommend {
	public Hashtable <String, Item> recommend(String itemName); // arg: itemName, Item object

	public TreeMap <String, Double>  recommend(String userid, String itemName, List <String> targetServiceType) ;
}
