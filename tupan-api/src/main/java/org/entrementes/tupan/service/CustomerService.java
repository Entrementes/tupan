package org.entrementes.tupan.service;

import java.net.InetAddress;

import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;

public interface CustomerService {
	
	Fare loadCustomerFare(String customerCode);

	Fare connectDevice(String customerCode, Device connectedDevice, InetAddress deviceAddress);

}
