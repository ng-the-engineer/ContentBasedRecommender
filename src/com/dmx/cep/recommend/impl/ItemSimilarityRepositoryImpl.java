/**
 * 文 件 名：ItemSimilarityRepositoryImpl.java
 * 版    权：Copyright DMX. Co. Ltd. All Rights Reserved. 
 * 描    述：CEP 2.0
 * 修 改 人：Johnny.lin
 * 修改时间：Mar 22, 2012
 * 修改内容：新增 
 */
package com.dmx.cep.recommend.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dmx.cep.recommend.api.ItemSimilarityRepository;
import com.dmx.cep.recommend.model.SimilarItem;
import com.dmx.cep.recommend.model.SimilarScore;

/**
 * @author  Johnny.lin
 * @version CEP V2.0
 * @since 1.0
 */
public class ItemSimilarityRepositoryImpl implements ItemSimilarityRepository
{
    private Map<String, List<SimilarScore>> repository;
    
    public ItemSimilarityRepositoryImpl(List<SimilarItem> similarItemList){
        saveToRepository(similarItemList);
    }
    
    @Override
    public List<SimilarScore> getSimilarItems(String sourceItemName){
        return repository.get(sourceItemName);
    }

    private void saveToRepository(List<SimilarItem> similarItemList){
        repository = new HashMap<String, List<SimilarScore>>();
        for(SimilarItem similarItem : similarItemList){
            String sourceItemName = similarItem.getSourceName();
            String targetItemName = similarItem.getTargetName();
            double score = similarItem.getScore();
            
            if(repository.containsKey(sourceItemName) == false){
                repository.put(sourceItemName, new ArrayList<SimilarScore>());
            }
            List<SimilarScore> list = repository.get(sourceItemName);
            list.add(new SimilarScore(targetItemName, score));
            repository.put(sourceItemName, list);
        }
        
        for(String key : repository.keySet()){
            Collections.sort(repository.get(key));
        }
    }
}

