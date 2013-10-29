/**
 * 文 件 名：CorelationMapping.java
 * 版    权：Copyright DMX. Co. Ltd. All Rights Reserved. 
 * 描    述：CEP 2.0
 * 修 改 人：Johnny.lin
 * 修改时间：Mar 23, 2012
 * 修改内容：新增 
 */
package com.dmx.cep.recommend.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author  Johnny.lin
 * @version CEP V2.0
 * @since 1.0
 */
public class CorelationMapping
{
    
    public static void main(String[] args){
        CorelationMapping corelationMapping = CorelationMapping.getInstance();
        System.out.println("movie is corelation with movie: " + corelationMapping.isCorelation("movie", "movie"));
        System.out.println("movie is corelation with book: "  + corelationMapping.isCorelation("movie", "book"));
        System.out.println("movie is corelation with music: " + corelationMapping.isCorelation("movie", "music"));
        System.out.println();
        
        List<AttributeCorelation> list = corelationMapping.getAttributeCorelations("movie", "movie");
        System.out.println("-------- movie -> movie ------------");
        for(AttributeCorelation corelation : list){
            System.out.println(corelation.getSourceAttribute() + " -> " + corelation.getTargetAttribute() + " : " + corelation.getWeight());
        }
        System.out.println();
        
        list = corelationMapping.getAttributeCorelations("movie", "music");
        System.out.println("-------- movie -> music ------------");
        for(AttributeCorelation corelation : list){
            System.out.println(corelation.getSourceAttribute() + " -> " + corelation.getTargetAttribute() + " : " + corelation.getWeight());
        }        
    }
    
    private static CorelationMapping instance;
    
    public static synchronized CorelationMapping getInstance(){
        if(instance == null){
            instance = new CorelationMapping();
            instance.loadMapping();
        }
        return instance;
    }
    
    private List<ServiceTypeCorelation> serviceTypeCorelations;
    private Map<ServiceTypeCorelation, List<AttributeCorelation>> attributeCorelations;
        
    private CorelationMapping(){
        serviceTypeCorelations = new ArrayList<ServiceTypeCorelation>();
        attributeCorelations = new HashMap<ServiceTypeCorelation, List<AttributeCorelation>>();
    }
    
    private void loadMapping(){
        try
        {
            InputStream in = ClassLoader.getSystemResourceAsStream("corelation.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(in, new CorelationXmlReader());
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Check if two service types is co-relation.
     * @param sourceServiceType
     * @param targetServiceType
     * @return
     */
    public boolean isCorelation(String sourceServiceType, String targetServiceType){
        for(ServiceTypeCorelation corelation : serviceTypeCorelations){
            if(corelation.getSourceServiceType().equals(sourceServiceType) 
                && corelation.getTargetServiceType().equals(targetServiceType)){
                return true;
            }
        }
        return false;
    }
    
    public List<AttributeCorelation> getAttributeCorelations(String sourceServiceType, String targetServiceType){
        ServiceTypeCorelation serviceTypeCorelation = new ServiceTypeCorelation(sourceServiceType, targetServiceType);
        return attributeCorelations.get(serviceTypeCorelation);
    }
    
    private void addServiceTypeCorelation(String sourceServiceType, String targetServiceType){
        ServiceTypeCorelation corelation = new ServiceTypeCorelation(sourceServiceType, targetServiceType);
        serviceTypeCorelations.add(corelation);
    }
    
    private void addAttributeCorelation(String sourceServiceType, String targetServiceType, String sourceAttribute, String targetAttribute, double weight){
        ServiceTypeCorelation serviceTypeCorelation = new ServiceTypeCorelation(sourceServiceType, targetServiceType);
        List<AttributeCorelation> list = null;
        
        if(!attributeCorelations.containsKey(serviceTypeCorelation)){
            list = new ArrayList<AttributeCorelation>();
            attributeCorelations.put(serviceTypeCorelation, list);
        } else {
            list = attributeCorelations.get(serviceTypeCorelation);
        }
        
        AttributeCorelation corelation = new AttributeCorelation(sourceAttribute, targetAttribute, weight);
        list.add(corelation);
    }

    public static class ServiceTypeCorelation {
        private final String sourceServiceType;
        private final String targetServiceType;
        
        public ServiceTypeCorelation(String sourceServiceType, String targetServiceType){
            this.sourceServiceType = sourceServiceType;
            this.targetServiceType = targetServiceType;
        }
        
        @Override
        public boolean equals(Object obj){
            if(obj instanceof ServiceTypeCorelation){
                ServiceTypeCorelation other = (ServiceTypeCorelation)obj;
                if(this.sourceServiceType.equals(other.sourceServiceType) 
                    && this.targetServiceType.equals(other.targetServiceType)){
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode(){
            return sourceServiceType.hashCode()/2 + targetServiceType.hashCode()/2;
        }
        
        /**
         * returns the sourceServiceType.
         * @return the sourceServiceType
         */
        public String getSourceServiceType()
        {
            return sourceServiceType;
        }

        /**
         * returns the targetServiceType.
         * @return the targetServiceType
         */
        public String getTargetServiceType()
        {
            return targetServiceType;
        }
    }
    
    public static class AttributeCorelation {
        private String sourceAttribute;
        private String targetAttribute;
        private double weight;
        
        public AttributeCorelation(String sourceAttribute, String targetAttribute, double weight){
            this.sourceAttribute = sourceAttribute;
            this.targetAttribute = targetAttribute;
            this.weight = weight;
        }
        
        /**
         * returns the sourceAttribute.
         * @return the sourceAttribute
         */
        public String getSourceAttribute()
        {
            return sourceAttribute;
        }
        /**
         * Sets the sourceAttribute.
         * @param sourceAttribute the sourceAttribute to set
         */
        public void setSourceAttribute(String sourceAttribute)
        {
            this.sourceAttribute = sourceAttribute;
        }
        /**
         * returns the targetAttribute.
         * @return the targetAttribute
         */
        public String getTargetAttribute()
        {
            return targetAttribute;
        }
        /**
         * Sets the targetAttribute.
         * @param targetAttribute the targetAttribute to set
         */
        public void setTargetAttribute(String targetAttribute)
        {
            this.targetAttribute = targetAttribute;
        }
        /**
         * returns the weight.
         * @return the weight
         */
        public double getWeight()
        {
            return weight;
        }
        /**
         * Sets the weight.
         * @param weight the weight to set
         */
        public void setWeight(double weight)
        {
            this.weight = weight;
        }
        
    }
    
    
    class CorelationXmlReader extends DefaultHandler {
        
        private String currentSourceServiceType;
        private String currentTargetServiceType;
        
        public CorelationXmlReader(){
        }
        
        public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException {        
            if (qName.equalsIgnoreCase("mapping")) {
                currentSourceServiceType = attributes.getValue("sourceServiceType");
                currentTargetServiceType = attributes.getValue("targetServiceType");
                
                instance.addServiceTypeCorelation(currentSourceServiceType, currentTargetServiceType);
            }
            
            if (qName.equalsIgnoreCase("corelation")) {  
                String sourceAttribute = attributes.getValue("sourceAttribute");
                String targetAttribute = attributes.getValue("targetAttribute");
                double weight = Double.parseDouble(attributes.getValue("weight"));
                
                instance.addAttributeCorelation(currentSourceServiceType, currentTargetServiceType, sourceAttribute, targetAttribute, weight);
            }
        }
    }
    
}




