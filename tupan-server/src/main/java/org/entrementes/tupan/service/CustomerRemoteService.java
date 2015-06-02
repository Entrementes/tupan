package org.entrementes.tupan.service;

import java.net.InetAddress;

import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Customer;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRemoteService implements CustomerService{

	private ElectricalGridService gridService;
	
	@Autowired
	public CustomerRemoteService(ElectricalGridService gridService) {
		this.gridService = gridService;
	}
	
	@Override
	public Fare loadCustomerFare(String customerCode) {
		Fare result = lookupCustomer(customerCode);
		CostDifferentials currentDifferentials = this.gridService.getElectricalFareDifferentials();
		result.setCostDifferentials(currentDifferentials);
		return result;
	}

	private Fare lookupCustomer(String customerCode) {
		Customer found = new Customer();
		found.setCode(customerCode);
		Fare result = new Fare();
		return result;
	}

	@Override
	public Fare connectDevice(String customerCode, Device connectedDevice, InetAddress deviceAddress) {
		Fare result = lookupCustomer(customerCode);
		if(connectedDevice.getStreamCapable()){
			this.gridService.registerDeviceIp(deviceAddress);
		}else{
			this.gridService.registerWebhookIp(deviceAddress);
		}
		return result;
	}

	@Override
	public CostDifferentials loadDifferentialFeed(String customerCode) {
		 lookupCustomer(customerCode);
		return this.gridService.getElectricalFareDifferentials();
	}

}
