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
	private Float fareVariance;

	@NotNull
	@XmlElement(name="base-fare")
	private Float baseFare;

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

	public Float getBaseFare() {
		return this.baseFare;
	}
	
	public void setBaseFare(Float baseFare) {
		this.baseFare = baseFare;
	}

}
