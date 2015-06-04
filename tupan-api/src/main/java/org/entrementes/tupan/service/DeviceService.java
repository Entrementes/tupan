package org.entrementes.tupan.service;

import org.entrementes.tupan.model.CostDifferentials;

public interface DeviceService {
	
	public void connect();

	public void processStream(String payload);

	public void processStream(CostDifferentials differentials);

}
