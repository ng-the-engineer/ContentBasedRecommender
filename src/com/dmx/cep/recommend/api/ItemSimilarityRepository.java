/**
 * 文 件 名：ItemSimilarityRepository.java
 * 版    权：Copyright DMX. Co. Ltd. All Rights Reserved. 
 * 描    述：CEP 2.0
 * 修 改 人：Johnny.lin
 * 修改时间：Mar 22, 2012
 * 修改内容：新增 
 */
package com.dmx.cep.recommend.api;

import java.util.List;

import com.dmx.cep.recommend.model.SimilarScore;

/**
 * @author  Johnny.lin
 * @version CEP V2.0
 * @since 1.0
 */
public interface ItemSimilarityRepository
{
    
    public List<SimilarScore> getSimilarItems(String sourceItemName);

}
