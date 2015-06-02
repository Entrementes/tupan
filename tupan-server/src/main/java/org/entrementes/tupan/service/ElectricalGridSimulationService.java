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

@Service
public class ElectricalGridSimulationService implements ElectricalGridService{
	
	private TupanInformation configuration;
	
	private Set<InetAddress> subscribers;
	
	private Thread loopbackmonitor;
	
	private Thread gridMonitor;
	
	private GridHistory gridHistory;
	
	@Autowired
	public ElectricalGridSimulationService(TupanInformation configuration) {
		this.configuration = configuration;
		this.gridHistory = new GridHistory(configuration.getHistoryBufferSize());
	}
	
	@PostConstruct
	private void init(){
		this.subscribers = new HashSet<InetAddress>();
		InetAddress loopback = InetAddress.getLoopbackAddress();
		this.subscribers.add(loopback);
		
		this.loopbackmonitor = new Thread(new StreamLoopbackMonitor(this.configuration));
		this.loopbackmonitor.setName("UDP loopback");
		this.loopbackmonitor.start();
		
		this.gridMonitor = new Thread(new ElectricalGridMonitor(this.configuration, this.subscribers, this.gridHistory));
		this.gridMonitor.setName("Grid Monitor");
		this.gridMonitor.start();
	}
	
	@Override
	public void registerDeviceIp(InetAddress address){
		this.subscribers.add(address);
	}

	@Override
	public CostDifferentials getElectricalFareDifferentials() {
		return this.gridHistory.buildHistory();
	}
	
	

}
