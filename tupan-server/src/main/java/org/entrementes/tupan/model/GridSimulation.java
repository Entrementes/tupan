package org.entrementes.tupan.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class GridSimulation {
	
	@NotNull
	@XmlElement(name="pooling-interval")
	private Long poolingInterval;
	
	@NotNull
	@XmlElement(name="fare-variance")
	private Double fareVariance;

	@NotNull
	@XmlElement(name="fare-differential-base")
	private Double fareDifferentialBase;

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

	public Double getFareDifferentialBase() {
		return this.fareDifferentialBase;
	}
	
	public void setFareDifferentialBase(Double baseFare) {
		this.fareDifferentialBase = baseFare;
	}

}
