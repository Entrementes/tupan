package org.entrementes.tupan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Fare {
	
	@XmlElement(name="base-cost")
	private Float baseCost;
	
	@XmlElement(name="cost-differentials")
	private CostDifferentials costDifferentials;

	public Float getBaseCost() {
		return baseCost;
	}

	public void setBaseCost(Float baseCost) {
		this.baseCost = baseCost;
	}

	public CostDifferentials getCostDifferentials() {
		return costDifferentials;
	}

	public void setCostDifferentials(CostDifferentials costDifferentials) {
		this.costDifferentials = costDifferentials;
	}

	@Override
	public String toString() {
		return "Fare [baseCost=" + baseCost + ", costDifferentials="
				+ costDifferentials + "]";
	}
	
}
