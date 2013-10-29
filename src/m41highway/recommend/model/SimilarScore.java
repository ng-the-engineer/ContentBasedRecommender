/**
 * 文 件 名：SimilarScore.java
 * 版    权：Copyright DMX. Co. Ltd. All Rights Reserved. 
 * 描    述：CEP 2.0
 * 修 改 人：Johnny.lin
 * 修改时间：Mar 22, 2012
 * 修改内容：新增 
 */
package m41highway.recommend.model;


/**
 * @author  Johnny.lin
 * @version CEP V2.0
 * @since 1.0
 */
public class SimilarScore implements Comparable<SimilarScore>
{

    private String targetItemName;

    private double score;

    public SimilarScore()
    {

    }

    public SimilarScore(String targetItemName, double score)
    {
        this.targetItemName = targetItemName;
        this.score = score;
    }

    public int compareTo(SimilarScore other)
    {
        if (this.score > other.score)
        {
            return -1;
        }
        else if (this.score < other.score)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * returns the targetItemName.
     * @return the targetItemName
     */
    public String getTargetItemName()
    {
        return targetItemName;
    }

    /**
     * Sets the targetItemName.
     * @param targetItemName the targetItemName to set
     */
    public void setTargetItemName(String targetItemName)
    {
        this.targetItemName = targetItemName;
    }

    /**
     * returns the score.
     * @return the score
     */
    public double getScore()
    {
        return score;
    }

    /**
     * Sets the score.
     * @param score the score to set
     */
    public void setScore(double score)
    {
        this.score = score;
    }

}
