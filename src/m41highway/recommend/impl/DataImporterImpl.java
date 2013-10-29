package m41highway.recommend.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import m41highway.recommend.model.Attribute;
import m41highway.recommend.model.Item;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DataImporterImpl{

	private static Log log = LogFactory.getLog(DataImporterImpl.class);	
	private static DataImporterImpl dataImporterImpl;
	private Hashtable <String, Item> data = new Hashtable <String, Item> (); 
	private final String ITEMS_DATA_PATH = "D:\\workspace_recommendation\\ItemBasedRecommendation\\data";
	
	private DataImporterImpl (){
		
	}
	
	public static DataImporterImpl getInstance(){
		if (dataImporterImpl == null){
			dataImporterImpl = new DataImporterImpl();
		}
		return dataImporterImpl;
	}
		
	public Hashtable<String, Item> getData() {
		File dir = new File(ITEMS_DATA_PATH);
		File[] files = dir.listFiles();
		for (File file: files){
			try{
				InputStream is = new FileInputStream(file);
		        
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				ItemXmlReader xmlReader = new ItemXmlReader();
				saxParser.parse(is, xmlReader);
				data.put(xmlReader.item.getName(), xmlReader.item);
				
			}catch(FileNotFoundException fe){
	        	System.out.println(fe.getMessage());
			}catch(Exception e){
				System.out.println(e.getMessage());	
			}
			
		}
		return data;
	}

}
