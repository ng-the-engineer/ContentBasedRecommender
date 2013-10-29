package m41highway.recommend.model;

public class Statistics {
	private String keyword;
	private double tf;
	private double idf;
	private double tfidf; // the product of tf x idf
	
	public Statistics(String keyword, double tf, double idf){
		this.setKeyword(keyword);
		this.setTf(tf);
		this.setIdf(idf);
		this.tfidf = 0.0;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public double getTf() {
		return tf;
	}

	public void setTf(double tf) {
		this.tf = tf;
	}

	public double getIdf() {
		return idf;
	}

	public void setIdf(double idf) {
		this.idf = idf;
	}

	public double getTfidf() {
		return tfidf;
	}

	public void setTfidf(double tfidf) {
		this.tfidf = tfidf;
	}
}
