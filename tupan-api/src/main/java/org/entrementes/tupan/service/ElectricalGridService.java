package org.entrementes.tupan.service;

import java.net.InetAddress;

import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.StateChange;

public interface ElectricalGridService {

	void registerDeviceIp(InetAddress address);
	
	CostDifferentials getElectricalFareDifferentials();

	void registerWebhookIp(InetAddress deviceAddress);
	
	void start();

	CostDifferentials setState(StateChange change);

}
