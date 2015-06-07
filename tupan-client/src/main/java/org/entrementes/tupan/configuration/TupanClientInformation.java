package org.entrementes.tupan.configuration;

import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CommunicationStrattegy;
import org.entrementes.tupan.model.Device;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tupan")
public class TupanClientInformation {
	
	private Integer streamPort;
	
	private String streamer;
	
	private String pooler;
	
	private String hook;
	
	private CommunicationStrattegy strattegy;

	private String tupanServerAddress;

	private String serialNumber;

	private String modelNumber;

	private String manufacturerCode;

	private Long poolingInterval;

	private Double fareLimit;
	
	public Integer getStreamPort() {
		return streamPort;
	}

	public void setStreamPort(Integer streamPort) {
		this.streamPort = streamPort;
	}

	public String getStreamer() {
		return streamer;
	}

	public void setStreamer(String streamer) {
		this.streamer = streamer;
	}

	public String getPooler() {
		return pooler;
	}

	public void setPooler(String pooler) {
		this.pooler = pooler;
	}

	public String getHook() {
		return hook;
	}

	public void setHook(String hook) {
		this.hook = hook;
	}

	public CommunicationStrattegy getStrattegy() {
		return strattegy;
	}

	public void setStrattegy(CommunicationStrattegy strattegy) {
		this.strattegy = strattegy;
	}

	public String getTupanServerAddress() {
		return this.tupanServerAddress;
	}
	
	public void setTupanServerAddress(String tupanServerAddress) {
		this.tupanServerAddress = tupanServerAddress;
	}

	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}
	
	public String getModelNumber() {
		return this.modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}
	
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public Long getPoolingInterval() {
		return poolingInterval;
	}

	public void setPoolingInterval(Long poolingInterval) {
		this.poolingInterval = poolingInterval;
	}

	public String getTupanServerUrl() {
		switch(getStrattegy()){
		case POOLING:
			return getTupanServerAddress().replace("{customer-id}", getPooler());
		case STREAM:
			return getTupanServerAddress().replace("{customer-id}", getStreamer())+"?pooling-subscriber=false";
		case WEB_HOOK:
			return getTupanServerAddress().replace("{customer-id}", getHook())+"?pooling-subscriber=false";	
		default:
			throw new TupanException(TupanExceptionCode.BAD_REQUEST);
		}
	}
	
	public Device buildDevice() {
		Device result = new Device();
		result.setStreamCapable(CommunicationStrattegy.STREAM.equals(getStrattegy()));
		result.setManufacturerCode(getManufacturerCode());
		result.setModelNumber(getModelNumber());
		result.setSerialNumber(getSerialNumber());
		return result;
	}

	public Double getFareLimit() {
		return this.fareLimit;
	}
	
	public void setFareLimit(Double fareLimit) {
		this.fareLimit = fareLimit;
	}
}
