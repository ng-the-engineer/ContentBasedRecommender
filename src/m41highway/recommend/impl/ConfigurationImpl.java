package m41highway.recommend.impl;

public class ConfigurationImpl {

	private ConfigurationImpl(){
		
	}
	
	private static ConfigurationImpl configurationImpl;
	
	public static ConfigurationImpl getInstance(){
		if (configurationImpl == null){
			configurationImpl = new ConfigurationImpl();
		}
		return configurationImpl;
	}
	
	private static String [] FIELDS_REQUIRE_TOKENIZATION = {"description", "actor", "name"};
	
	public boolean isRequirdTokenization(String field){
		boolean result = false;
		for (String s : FIELDS_REQUIRE_TOKENIZATION){
			if (field.equalsIgnoreCase(s)){
				result = true;
				break;
			}
		}
		return result;
	}
	
}
