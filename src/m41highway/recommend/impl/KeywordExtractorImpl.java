package m41highway.recommend.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class KeywordExtractorImpl  {

	private static Log log = LogFactory.getLog(KeywordExtractorImpl.class);
	
	private static KeywordExtractorImpl keywordExtractorImpl;
	
	private KeywordExtractorImpl(){
		
	}
	
	public static KeywordExtractorImpl getInstance(){
		if (keywordExtractorImpl == null){
			keywordExtractorImpl = new KeywordExtractorImpl();
		}
		return keywordExtractorImpl;
	}
	
	public ArrayList <String> tokenize(String text) {
		ArrayList <String> strList = new ArrayList();
		
		// implement the IK analyzer
		Configuration cfg = new  Configuration();
        cfg.setUseSmart(true);
        Reader r = new StringReader(text);     
        
        IKSegmenter iKSegmenter2 =  new IKSegmenter(r, cfg);
        
        StringBuilder str = new StringBuilder();
        
        while (iKSegmenter2 != null){
        	Lexeme lexeme;
			try {
				lexeme = (Lexeme)iKSegmenter2.next();
			
				if (lexeme == null){
					break;
				}
				strList.add(lexeme.getLexemeText());
				//System.out.print("[" + lexeme.getLexemeText() + "] ");
				str.append("[" + lexeme.getLexemeText() + "] ");
				//log.info("[" + lexeme.getLexemeText() + "] ");
			} catch (IOException e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}			
        };
        //System.out.println();
        log.info(str);
        
		return strList;
	}

}
