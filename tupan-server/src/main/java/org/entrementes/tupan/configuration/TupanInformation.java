package org.entrementes.tupan.configuration;

import org.entrementes.tupan.model.GridSimulation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tupan")
public class TupanInformation {
	
	private Integer streamPort;
	
	private Long poolingInterval;
	
	private Float fareVariance;

	private Integer historyBufferSize;
	
	private Float baseFare;

	public Integer getStreamPort() {
		return streamPort;
	}

	public void setStreamPort(Integer streamPort) {
		this.streamPort = streamPort;
	}

	public Long getPoolingInterval() {
		return poolingInterval;
	}

	public void setPoolingInterval(Long poolingInterval) {
		this.poolingInterval = poolingInterval;
	}
	
	public Float getFareVariance() {
		return fareVariance;
	}
	
	public void setFareVariance(Float fareVariance) {
		this.fareVariance = fareVariance;
	}

	public Integer getHistoryBufferSize() {
		return this.historyBufferSize;
	}
	
	public void setHistoryBufferSize(Integer historyBufferSize) {
		this.historyBufferSize = historyBufferSize;
	}
	
	public Float getBaseFare() {
		return baseFare;
	}
	
	public void setBaseFare(Float baseFare) {
		this.baseFare = baseFare;
	}

	public void update(GridSimulation updated) {
		this.fareVariance = updated.getFareVariance();
		this.poolingInterval = updated.getPoolingInterval();
		this.baseFare = updated.getBaseFare();
	}

}
