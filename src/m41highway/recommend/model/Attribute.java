package m41highway.recommend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Attribute {
	private String name;
	private String value;
	private boolean requireKeywordExtraction;
	private ArrayList <String> rawWordList; // tokenized value
	//private Hashtable <String, Statistics> tfidf;// tokinized value, Statistics
	private HashMap <String, Statistics> tfidf;// tokinized value, Statistics
	private HashMap <String, Statistics> importantWords;// tokinized value, Statistics
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	//public boolean isRequireKeywordExtraction() {
	//	return requireKeywordExtraction;
	//}
	public void setRequireKeywordExtraction(boolean requireKeywordExtraction) {
		this.requireKeywordExtraction = requireKeywordExtraction;
	}
	public HashMap <String, Statistics> getTfidf() {
		return tfidf;
	}
	public void setTfidf(HashMap <String, Statistics> tfidf) {
		this.tfidf = tfidf;
	}
	public HashMap <String, Statistics> getImportantWords() {
		return importantWords;
	}
	public void setImportantWords(HashMap <String, Statistics> importantWords) {
		this.importantWords = importantWords;
	}
	public ArrayList <String> getRawWordList() {
		return rawWordList;
	}
	public void setRawWordList(ArrayList <String> rawWordList) {
		this.rawWordList = rawWordList;
	}
	
}
