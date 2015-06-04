package org.entrementes.tupan.service;

import java.net.InetAddress;
import java.util.Calendar;

import org.entrementes.tupan.entity.CustomerEntity;
import org.entrementes.tupan.entity.DeviceRegistrationEntity;
import org.entrementes.tupan.entity.FareFlagEntity;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.repositories.CustomerRepository;
import org.entrementes.tupan.repositories.DeviceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRemoteService implements CustomerService{

	private ElectricalGridService gridService;
	
	private CustomerRepository customerRepository;
	
	private DeviceRegistrationRepository deviceRegistrationRepository;
	
	private FlagService flagService;
	
	@Autowired
	public CustomerRemoteService(ElectricalGridService gridService,
			CustomerRepository customerRepository,
			DeviceRegistrationRepository deviceRegistrationRepository,
			FlagService flagService) {
		this.gridService = gridService;
		this.customerRepository = customerRepository;
		this.deviceRegistrationRepository = deviceRegistrationRepository;
		this.flagService = flagService;
	}
	
	@Override
	public Fare loadCustomerFare(String customerCode) {
		Fare result = lookupCustomer(customerCode);
		CostDifferentials currentDifferentials = this.gridService.getElectricalFareDifferentials();
		result.setCostDifferentials(currentDifferentials);
		return result;
	}

	private Fare lookupCustomer(String customerCode) {
		CustomerEntity customer = this.customerRepository.findByCode(customerCode);
		if(customer == null){
			throw new TupanException(TupanExceptionCode.NOT_FOUND);
		}
		Fare result = new Fare();
		result.setBaseCost(customer.getBaseFare());
		result.setCostDifferentials(gridService.getElectricalFareDifferentials());
		FareFlagEntity currentFlag = this.flagService.getCurrentFlag();
		result.setFlag(currentFlag.getFlag());
		result.setMultiplier(currentFlag.getMultiplier());
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
		
		DeviceRegistrationEntity registration = new DeviceRegistrationEntity();
		registration.setCustomerCode(customerCode);
		registration.setIpAddress(deviceAddress.getCanonicalHostName());
		registration.setManufacturer(connectedDevice.getManufacturerCode());
		registration.setModelNumber(connectedDevice.getModelNumber());
		registration.setSerialNumber(connectedDevice.getSerialNumber());
		registration.setRegistrationTime(Calendar.getInstance().getTime());
		
		this.deviceRegistrationRepository.save(registration);
		
		return result;
	}

}
