package org.entrementes.tupan.service.component;

import org.entrementes.tupan.configuration.TupanClientInformation;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.DeviceSimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class FareMonitor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(FareMonitor.class);
	
	private TupanClientInformation configuration;
	
	private DeviceSimulationService deviceSimulationService;

	private RestTemplate dispatcher;
	
	public FareMonitor(RestTemplate dispatcher, TupanClientInformation configuration,DeviceSimulationService deviceSimulationService) {
		this.configuration = configuration;
		this.deviceSimulationService = deviceSimulationService;
		this.dispatcher = dispatcher;
	}

	@Override
	public void run() {
		while(true){
			Fare currentFare = this.dispatcher.getForObject(this.configuration.getTupanServerUrl(), Fare.class);
			this.deviceSimulationService.loadFare(currentFare);
			LOGGER.info("current fare: "+ currentFare.toString());
			try {
				Thread.sleep(this.configuration.getPoolingInterval());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
