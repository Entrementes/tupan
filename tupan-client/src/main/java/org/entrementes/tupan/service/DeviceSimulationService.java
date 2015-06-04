package org.entrementes.tupan.service;

import org.entrementes.tupan.configuration.TupanClientInformation;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.component.FareMonitor;
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
	
	private Thread farePoolingThread;
	
	@Autowired
	public DeviceSimulationService(TupanClientInformation configuration, RestTemplate dispatcher){
		this.configuration = configuration;
		this.dispatcher = dispatcher;
	}
	
	@Override
	public void connect() {
		String tupanserverUrl = this.configuration.getTupanServerUrl();
		LOGGER.info("connecting to: " + tupanserverUrl);
		Device smartDevice = this.configuration.buildDevice();
		LOGGER.info("current fare: " + this.currentFare);
		switch(this.configuration.getStrattegy()){
		case POOLING:
			break;
		case STREAM:
			this.currentFare = this.dispatcher.postForObject(tupanserverUrl, smartDevice, Fare.class);
			this.monitoringThread = new Thread(new StreamMonitor(configuration, this));
			this.monitoringThread.start();
			break;
		case WEB_HOOK:
			this.currentFare = this.dispatcher.postForObject(tupanserverUrl, smartDevice, Fare.class);
			break;	
		default:
			throw new TupanException(TupanExceptionCode.BAD_REQUEST);
		}
		this.farePoolingThread = new Thread(new FareMonitor(this.dispatcher, this.configuration, this));
		this.farePoolingThread.start();
	}

	@Override
	public void processStream(String payload) {
		LOGGER.debug(payload);
	}

	@Override
	public void processStream(CostDifferentials differentials) {
		LOGGER.debug(differentials.toString());
		
	}

	public void loadFare(Fare currentFare) {
		this.currentFare = currentFare;
		processStream(currentFare.getCostDifferentials());
	}

}
