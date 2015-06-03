package org.entrementes.tupan.service;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.entrementes.tupan.configuration.TupanInformation;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.service.components.ElectricalGridMonitor;
import org.entrementes.tupan.service.components.GridHistory;
import org.entrementes.tupan.service.components.StreamLoopbackMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ElectricalGridSimulationService implements ElectricalGridService{
	
	private TupanInformation configuration;
	
	private Set<InetAddress> udpSubscribers;
	
	private Set<InetAddress> tcpSubscribers;
	
	private Thread loopbackmonitor;
	
	private Thread gridMonitor;
	
	private GridHistory gridHistory;
	
	private RestTemplate dispatcher;
	
	@Autowired
	public ElectricalGridSimulationService(TupanInformation configuration, RestTemplate dispatcher) {
		this.configuration = configuration;
		this.dispatcher = dispatcher;
		this.gridHistory = new GridHistory(configuration.getHistoryBufferSize());
	}
	
	@PostConstruct
	private void init(){
		this.udpSubscribers = new HashSet<InetAddress>();
		this.tcpSubscribers = new HashSet<InetAddress>();
		InetAddress loopback = InetAddress.getLoopbackAddress();
		this.udpSubscribers.add(loopback);
		this.tcpSubscribers.add(loopback);
		
		this.loopbackmonitor = new Thread(new StreamLoopbackMonitor(this.configuration));
		this.loopbackmonitor.setName("UDP loopback");
		this.loopbackmonitor.start();
		
		this.gridMonitor = new Thread(new ElectricalGridMonitor(this.configuration,
																this.udpSubscribers,
																tcpSubscribers,
																this.gridHistory,
																this.dispatcher));
		this.gridMonitor.setName("Grid Monitor");
		this.gridMonitor.start();
	}
	
	@Override
	public void registerDeviceIp(InetAddress deviceAddress){
		this.udpSubscribers.add(deviceAddress);
	}

	@Override
	public CostDifferentials getElectricalFareDifferentials() {
		return this.gridHistory.buildHistory();
	}

	@Override
	public void registerWebhookIp(InetAddress deviceAddress) {
		this.udpSubscribers.add(deviceAddress);
	}
	
}
