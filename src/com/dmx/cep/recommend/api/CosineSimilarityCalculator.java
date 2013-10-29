package com.dmx.cep.recommend.api;

import java.util.List;

import com.dmx.cep.recommend.model.Item;
import com.dmx.cep.recommend.model.SimilarItem;

public interface CosineSimilarityCalculator {
	
    public List<SimilarItem> calculate(List<Item> itemList);
	
}
