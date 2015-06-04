package org.entrementes.tupan.configuration;

import org.entrementes.tupan.model.GridSimulation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tupan")
public class TupanInformation {
	
	private Integer streamPort;
	
	private Long poolingInterval;
	
	private Double fareVariance;

	private Integer historyBufferSize;
	
	private Double fareDifferentialBase;

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
	
	public Double getFareVariance() {
		return fareVariance;
	}
	
	public void setFareVariance(Double fareVariance) {
		this.fareVariance = fareVariance;
	}

	public Integer getHistoryBufferSize() {
		return this.historyBufferSize;
	}
	
	public void setHistoryBufferSize(Integer historyBufferSize) {
		this.historyBufferSize = historyBufferSize;
	}
	
	public Double getFareDifferentialBase() {
		return fareDifferentialBase;
	}
	
	public void setFareDifferentialBase(Double baseFare) {
		this.fareDifferentialBase = baseFare;
	}

	public void update(GridSimulation updated) {
		this.fareVariance = updated.getFareVariance();
		this.poolingInterval = updated.getPoolingInterval();
		this.fareDifferentialBase = updated.getFareDifferentialBase();
	}

	public String getHookUrl() {
		return "http://{device-ip}:9999/v1/postMeasurement";
	}

}
