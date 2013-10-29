package m41highway.recommend.api;

import java.util.List;

import m41highway.recommend.model.Item;
import m41highway.recommend.model.SimilarItem;


public interface CosineSimilarityCalculator {
	
    public List<SimilarItem> calculate(List<Item> itemList);
	
}
