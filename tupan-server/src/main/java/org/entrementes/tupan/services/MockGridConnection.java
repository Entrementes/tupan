package org.entrementes.tupan.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.entrementes.tupan.entities.ConsumptionFlag;
import org.entrementes.tupan.model.ConsumerType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MockGridConnection implements SmartGridConnection{

	private ConsumptionFlag flag;
	
	private String systemMessage;

	private int updateInterval;
	
	private double baseFare;

	private LocalDateTime lastUpadate;

	@Override
	public String getSystemMessage() {
		return this.systemMessage;
	}

	@JsonIgnore
	@Override
	public LocalDateTime getNextUpdate() {
		return this.lastUpadate.truncatedTo(ChronoUnit.MINUTES).plusMinutes(this.updateInterval);
	}

	@JsonIgnore
	@Override
	public LocalDateTime getLastUpdate() {
		return this.lastUpadate;
	}

	@JsonIgnore
	@Override
	public double evaluateFare(ConsumerType userType) {
		return this.baseFare * userType.getTaxMultiplier() * this.flag.getIncentive();
	}

	@JsonIgnore
	@Override
	public String getFareCode(){
		return this.flag.name().toString();
	}
	
	public ConsumptionFlag getFlag() {
		return flag;
	}

	public void setFlag(ConsumptionFlag flag) {
		this.flag = flag;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(double baseFare) {
		this.baseFare = baseFare;
	}

	public LocalDateTime getLastUpadate() {
		return lastUpadate;
	}

	public void setLastUpadate(LocalDateTime lastUpadate) {
		this.lastUpadate = lastUpadate;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	
}
