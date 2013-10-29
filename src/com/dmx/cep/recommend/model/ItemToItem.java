package com.dmx.cep.recommend.model;

public class ItemToItem {
	private String sourceServiceType;
	private String sourceAttribute;
	private String targetServiceType;
	private String targetAttribute;
	private double weight;  // range between 0-1
	public String getSourceServiceType() {
		return sourceServiceType;
	}
	public void setSourceServiceType(String sourceServiceType) {
		this.sourceServiceType = sourceServiceType;
	}
	public String getSourceAttribute() {
		return sourceAttribute;
	}
	public void setSourceAttribute(String sourceAttribute) {
		this.sourceAttribute = sourceAttribute;
	}
	public String getTargetServiceType() {
		return targetServiceType;
	}
	public void setTargetServiceType(String targetServiceType) {
		this.targetServiceType = targetServiceType;
	}
	public String getTargetAttribute() {
		return targetAttribute;
	}
	public void setTargetAttribute(String targetAttribute) {
		this.targetAttribute = targetAttribute;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
}
