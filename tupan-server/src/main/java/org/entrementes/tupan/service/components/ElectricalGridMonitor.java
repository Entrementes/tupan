package org.entrementes.tupan.service.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import org.entrementes.tupan.configuration.TupanInformation;
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

	public ElectricalGridMonitor(TupanInformation configuration, 
								Set<InetAddress> udpSubscribers, 
								Set<InetAddress> tcpSubscribers, 
								GridHistory gridHistory,
								RestTemplate tcpClientsDispatcher) {
		this.configuration = configuration;
		this.udpSubscribers = udpSubscribers;
		this.tcpSubscribers = tcpSubscribers;
		this.generator = new Random(Calendar.getInstance().getTimeInMillis());
		this.gridHistory = gridHistory;
		this.tcpClientsDispatcher = tcpClientsDispatcher;
	}

	@Override
	public void run() {
		try( DatagramSocket clientSocket = new DatagramSocket() ) {
			byte[] payload = new byte[1024];
			while(true){
				Float electricalDifferential = this.generator.nextFloat() * this.configuration.getFareVariance();
				this.gridHistory.add(electricalDifferential);
				payload = this.gridHistory.buildPayload();
				LOGGER.info("notifing subscribers");
				for(InetAddress subscriber : this.udpSubscribers){
					LOGGER.debug(subscriber.getCanonicalHostName());
					DatagramPacket sendPacket = new DatagramPacket(payload, payload.length, subscriber, this.configuration.getStreamPort());
					clientSocket.send(sendPacket);
				}
				for(InetAddress subscriber : this.tcpSubscribers){
					LOGGER.debug(subscriber.getCanonicalHostName());
					try{
						this.tcpClientsDispatcher.postForLocation(this.configuration.getHookUrl().replace("{device-ip}", subscriber.getCanonicalHostName()), this.gridHistory.buildHistory());
					}catch(ResourceAccessException ex){
						//server booting up
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
