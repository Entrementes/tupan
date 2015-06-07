package org.entrementes.tupan.service;

import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Fare;

public interface DeviceService {
	
	public void connect();

	public void processStream(String payload);

	public void processStream(CostDifferentials differentials);

	public Fare getFare();

}
