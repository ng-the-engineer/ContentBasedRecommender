package m41highway.recommend.model;

public class SimilarItem {
	private String sourceName;
	private String targetName;
	private double score;
	
	public SimilarItem(){
	}
	
	public SimilarItem(String sourceName, String targetName, double score){
	    this.sourceName = sourceName;
	    this.targetName = targetName;
	    this.score = score;
	}
	
	
    /**
     * returns the sourceName.
     * @return the sourceName
     */
    public String getSourceName()
    {
        return sourceName;
    }
    /**
     * Sets the sourceName.
     * @param sourceName the sourceName to set
     */
    public void setSourceName(String sourceName)
    {
        this.sourceName = sourceName;
    }
    /**
     * returns the targetName.
     * @return the targetName
     */
    public String getTargetName()
    {
        return targetName;
    }
    /**
     * Sets the targetName.
     * @param targetName the targetName to set
     */
    public void setTargetName(String targetName)
    {
        this.targetName = targetName;
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
