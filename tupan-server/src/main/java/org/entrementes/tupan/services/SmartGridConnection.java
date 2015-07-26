package org.entrementes.tupan.services;

import java.time.LocalDateTime;

import org.entrementes.tupan.model.ConsumerType;

public interface SmartGridConnection {

	String getSystemMessage();
	
	String getFareCode();

	LocalDateTime getNextUpdate();

	LocalDateTime getLastUpdate();

	double evaluateFare(ConsumerType userType);

}
