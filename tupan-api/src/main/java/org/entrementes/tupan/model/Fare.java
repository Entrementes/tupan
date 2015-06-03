package org.entrementes.tupan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Fare {
	
	@XmlElement(name="base-cost")
	private Double baseCost;
	
	@XmlElement(name="flag")
	private Flag flag;
	
	@XmlElement(name="multiplier")
	private Double multiplier;
	
	@XmlElement(name="cost-differentials")
	private CostDifferentials costDifferentials;

	public Double getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Double baseCost) {
		this.baseCost = baseCost;
	}

	public CostDifferentials getCostDifferentials() {
		return costDifferentials;
	}

	public void setCostDifferentials(CostDifferentials costDifferentials) {
		this.costDifferentials = costDifferentials;
	}

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

	@Override
	public String toString() {
		return "Fare [baseCost=" + baseCost + ", costDifferentials="
				+ costDifferentials + "]";
	}
	
}
