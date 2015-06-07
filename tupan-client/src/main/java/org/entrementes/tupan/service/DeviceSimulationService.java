package org.entrementes.tupan.service;

import java.io.IOException;

import org.entrementes.tupan.configuration.TupanClientInformation;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.model.TupanState;
import org.entrementes.tupan.service.component.FareMonitor;
import org.entrementes.tupan.service.component.StreamMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeviceSimulationService implements DeviceService{

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceSimulationService.class);
	
	private TupanClientInformation configuration;
	
	private RestTemplate dispatcher;
	
	private Fare currentFare;
	
	private Thread monitoringThread;
	
	private Thread farePoolingThread;
	
	private DummyDevice device;
	
	@Autowired
	public DeviceSimulationService(TupanClientInformation configuration, RestTemplate dispatcher){
		this.configuration = configuration;
		this.dispatcher = dispatcher;
		try{
			this.device = new DummyDevice();
			LOGGER.info("Dummy Device Initialized");
		}catch(Error ex){
			ex.printStackTrace();
			LOGGER.info("Running in simmulation mode");
		}
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
		ObjectMapper mapper = new ObjectMapper();
		try {
			CostDifferentials differentials = mapper.readValue(payload, CostDifferentials.class);
			processStream(differentials);
		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new TupanException(TupanExceptionCode.UNMAPPED);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new TupanException(TupanExceptionCode.UNMAPPED);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TupanException(TupanExceptionCode.UNMAPPED);
		}
	}

	@Override
	public void processStream(CostDifferentials differentials) {
		LOGGER.debug(differentials.toString());
		if(this.device != null){
			TupanState state  = TupanState.valueOf(differentials.getSystemMessage());
			this.device.setState(state);
			this.device.checkFare(this.currentFare,differentials);
		}
	}

	public void loadFare(Fare currentFare) {
		this.currentFare = currentFare;
		if(this.device != null){
			this.device.setFlag(currentFare.getFlag());
		}
		processStream(currentFare.getCostDifferentials());
	}

	@Override
	public Fare getFare() {
		return this.currentFare;
	}

}
