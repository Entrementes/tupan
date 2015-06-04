package org.entrementes.tupan.service;

import org.entrementes.tupan.configuration.TupanClientInformation;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CommunicationStrattegy;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.component.StreamMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeviceSimulationService implements DeviceService{

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSimulationService.class);
	
	private TupanClientInformation configuration;
	
	private RestTemplate dispatcher;
	
	private Fare currentFare;
	
	private Thread monitoringThread;
	
	@Autowired
	public DeviceSimulationService(TupanClientInformation configuration, RestTemplate dispatcher){
		this.configuration = configuration;
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void connect() {
		String tupanserverUrl = getTupanServerUrl();
		LOGGER.info("connecting to: " + tupanserverUrl);
		Device smartDevice = buildDevice();
		this.currentFare = this.dispatcher.postForObject(tupanserverUrl, smartDevice, Fare.class);
		LOGGER.info("current fare: " + this.currentFare);
		this.monitoringThread = new Thread(new StreamMonitor(configuration, this));
		this.monitoringThread.start();
	}

	private String getTupanServerUrl() {
		switch(this.configuration.getStrattegy()){
		case POOLING:
			return this.configuration.getTupanServerAddress().replace("{customer-id}", this.configuration.getPooler());
		case STREAM:
			return this.configuration.getTupanServerAddress().replace("{customer-id}", this.configuration.getStreamer());
		case WEB_HOOK:
			return this.configuration.getTupanServerAddress().replace("{customer-id}", this.configuration.getHook());	
		default:
			throw new TupanException(TupanExceptionCode.BAD_REQUEST);
		}
	}

	private Device buildDevice() {
		Device result = new Device();
		result.setStreamCapable(CommunicationStrattegy.STREAM.equals(this.configuration.getStrattegy()));
		result.setManufacturerCode(this.configuration.getManufacturerCode());
		result.setModelNumber(this.configuration.getModelNumber());
		result.setSerialNumber(this.configuration.getSerialNumber());
		return result;
	}

	@Override
	public void processStream(String payload) {
		LOGGER.debug(payload);
	}

}
