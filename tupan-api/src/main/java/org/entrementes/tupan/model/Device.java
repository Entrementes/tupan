package org.entrementes.tupan.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="device")
@XmlAccessorType(XmlAccessType.FIELD)
public class Device {
	
	@XmlElement(name="manufacturer-code")
	private String manufacturerCode;
	
	@XmlElement(name="model-number")
	private String modelNumber;
	
	@XmlElement(name="serial-number")
	private String serialNumber;
	
	@XmlElement(name="stream-capable")
	private Boolean streamCapable;

	public String getManufacturerCode() {
		return manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
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

	public Boolean getStreamCapable() {
		return streamCapable;
	}

	public void setStreamCapable(Boolean streamCapable) {
		this.streamCapable = streamCapable;
	}

	@Override
	public String toString() {
		return "Device [manufacturerCode=" + manufacturerCode
				+ ", modelNumber=" + modelNumber + ", serialNumber="
				+ serialNumber + ", streamCapable=" + streamCapable + "]";
	}
	
}
