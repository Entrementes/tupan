package org.entrementes.tupan.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tupan")
public class TupanInformation {
	
	private String consumptionFlag;
	
	private Double baseFare;
	
	private String systemMessage;
	
	private Integer updateInterval;

	public String getConsumptionFlag() {
		return consumptionFlag;
	}

	public void setConsumptionFlag(String consumptionFlag) {
		this.consumptionFlag = consumptionFlag;
	}

	public Double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(Double baseFare) {
		this.baseFare = baseFare;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}

	public Integer getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(Integer updateInterval) {
		this.updateInterval = updateInterval;
	}

}
