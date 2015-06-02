package org.entrementes.tupan.service;

import java.net.InetAddress;

import org.entrementes.tupan.model.CostDifferentials;

public interface ElectricalGridService {

	void registerDeviceIp(InetAddress address);
	
	CostDifferentials getElectricalFareDifferentials();

}
