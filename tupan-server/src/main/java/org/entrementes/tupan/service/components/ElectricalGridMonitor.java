package org.entrementes.tupan.service.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import org.entrementes.tupan.configuration.TupanInformation;
import org.entrementes.tupan.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class ElectricalGridMonitor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ElectricalGridMonitor.class);
	
	private TupanInformation configuration;

	private Set<InetAddress> udpSubscribers;
	
	private Set<InetAddress> tcpSubscribers;

	private Random generator;

	private GridHistory gridHistory;
	
	private RestTemplate tcpClientsDispatcher;
	
	private ReportService service;

	public ElectricalGridMonitor(TupanInformation configuration, 
								Set<InetAddress> udpSubscribers, 
								Set<InetAddress> tcpSubscribers, 
								GridHistory gridHistory,
								RestTemplate tcpClientsDispatcher,
								ReportService service) {
		this.configuration = configuration;
		this.udpSubscribers = udpSubscribers;
		this.tcpSubscribers = tcpSubscribers;
		this.generator = new Random(Calendar.getInstance().getTimeInMillis());
		this.gridHistory = gridHistory;
		this.tcpClientsDispatcher = tcpClientsDispatcher;
		this.service = service;
	}

	@Override
	public void run() {
		try( DatagramSocket clientSocket = new DatagramSocket() ) {
			byte[] payload = new byte[1024];
			while(true){
				int drift = 1;
				if(this.generator.nextBoolean()){
					drift *= -1;
				}
				Double electricalDifferential = this.configuration.getFareDifferentialBase() + ( drift * this.generator.nextFloat() * this.configuration.getFareVariance() );
				this.gridHistory.add(electricalDifferential);
				payload = this.gridHistory.buildPayload();
				LOGGER.info("notifing subscribers");
				for(InetAddress subscriber : this.udpSubscribers){
//					if(subscriber.isLoopbackAddress()){
//						DatagramPacket sendPacket = new DatagramPacket(payload, payload.length, subscriber, this.configuration.getStreamPort());
//						clientSocket.send(sendPacket);
//					}else{
						LOGGER.debug(subscriber.getCanonicalHostName());
						Long clockOn = System.currentTimeMillis();
						DatagramPacket sendPacket = new DatagramPacket(payload, payload.length, subscriber, this.configuration.getStreamPort());
						clientSocket.send(sendPacket);
						Long clockOff = System.currentTimeMillis();
						this.service.registerPerformance(clockOn, clockOff, "UDP", subscriber.getCanonicalHostName(),clockOff - clockOn);
//					}
					
				}
				for(InetAddress subscriber : this.tcpSubscribers){
					LOGGER.debug(subscriber.getCanonicalHostName());
					try{
						Long clockOn = System.currentTimeMillis();
						this.tcpClientsDispatcher.postForLocation(this.configuration.getHookUrl().replace("{device-ip}", subscriber.getCanonicalHostName()), this.gridHistory.buildHistory());
						Long clockOff = System.currentTimeMillis();
						this.service.registerPerformance(clockOn, clockOff, "WEB-HOOK", subscriber.getCanonicalHostName(), clockOff - clockOn);
					}catch(ResourceAccessException ex){
						ex.printStackTrace();
					}catch(HttpServerErrorException ex){
						ex.printStackTrace();
					}catch(HttpClientErrorException ex){
						ex.printStackTrace();
					}
				}
				Thread.sleep(this.configuration.getPoolingInterval());
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
