package org.entrementes.tupan.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;

@XmlRootElement
public class DeviceRegistrationEntity {
	
	@Id
	private BigInteger id;
	
	private String customerCode;
	
	private String manufacturer;
	
	private String modelNumber;
	
	private String serialNumber;
	
	private String ipAddress;
	
	private Date registrationTime;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}

}
