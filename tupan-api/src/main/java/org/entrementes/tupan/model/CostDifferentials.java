package org.entrementes.tupan.model;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cost-differentials")
@XmlAccessorType(XmlAccessType.FIELD)
public class CostDifferentials {
	
	@XmlElement(name="system-message")
	private String systemMessage;
	
	@XmlElement(name="cost-differential")
	private Double[] costDifferentials;

	public Double[] getCostDifferentials() {
		return costDifferentials;
	}

	public void setCostDifferentials(Double[] costDifferentials) {
		this.costDifferentials = costDifferentials;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	@Override
	public String toString() {
		return "{\"systemMessage\":\"" + systemMessage
				+ "\",\"costDifferentials\":" + Arrays.toString(costDifferentials)
				+ "}";
	}

}
