package org.entrementes.tupan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cost-differentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class CostDifferentials {
	
	@XmlElement(name="cost-differential")
	private Float[] costDifferentials;

	public Float[] getCostDifferentials() {
		return costDifferentials;
	}

	public void setCostDifferentials(Float[] costDifferentials) {
		this.costDifferentials = costDifferentials;
	}

}
