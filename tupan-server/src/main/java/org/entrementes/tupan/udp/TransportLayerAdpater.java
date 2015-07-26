package org.entrementes.tupan.udp;

import java.net.DatagramSocket;
import java.net.SocketException;

import javax.annotation.PostConstruct;

import org.entrementes.tupan.configurations.TransportLayerInformation;
import org.entrementes.tupan.services.TupanSmartGridService;
import org.entrementes.tupan.udp.listeners.ConsumpionReportListener;
import org.entrementes.tupan.udp.listeners.SmartApplianceRegistrationListener;
import org.entrementes.tupan.udp.listeners.SmartGridQueryRequestListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class TransportLayerAdpater {
	
	private Thread smartgridQueryThread;
	
	private Thread smartApplianceRegistrationThread;
	
	private Thread consumptionReportThread;
	
	private TupanSmartGridService service;

    private ObjectMapper objectMapper;

	private TransportLayerInformation config;
	
	@Autowired
	public TransportLayerAdpater(TupanSmartGridService service, ObjectMapper objectMapper, TransportLayerInformation config) {
		this.service = service;
		this.objectMapper = objectMapper;
		this.config = config;
	}
	
	@PostConstruct
	private void init() throws SocketException{
		this.smartgridQueryThread = new Thread(new SmartGridQueryRequestListener(new DatagramSocket(this.config.getQueryPort()), this.service, this.objectMapper));
		this.smartApplianceRegistrationThread = new Thread(new SmartApplianceRegistrationListener(new DatagramSocket(this.config.getRegisterPort()), this.service, this.objectMapper));
		this.consumptionReportThread = new Thread(new ConsumpionReportListener(new DatagramSocket(this.config.getConsumptionPort()), this.service, this.objectMapper));
		
		this.smartgridQueryThread.start();
		this.smartApplianceRegistrationThread.start();
		this.consumptionReportThread.start();
	}

}
