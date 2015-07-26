package org.entrementes.tupan.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "transport")
public class TransportLayerInformation {

	private Integer queryPort;
	
	private Integer registerPort;
	
	private Integer consumptionPort;

	public Integer getQueryPort() {
		return queryPort;
	}

	public void setQueryPort(Integer queryPort) {
		this.queryPort = queryPort;
	}

	public Integer getRegisterPort() {
		return registerPort;
	}

	public void setRegisterPort(Integer registerPort) {
		this.registerPort = registerPort;
	}

	public Integer getConsumptionPort() {
		return consumptionPort;
	}

	public void setConsumptionPort(Integer consumptionPort) {
		this.consumptionPort = consumptionPort;
	}
}
