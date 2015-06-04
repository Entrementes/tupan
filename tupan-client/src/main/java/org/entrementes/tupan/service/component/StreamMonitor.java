package org.entrementes.tupan.service.component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.entrementes.tupan.configuration.TupanClientInformation;
import org.entrementes.tupan.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamMonitor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamMonitor.class);
	
	private TupanClientInformation config;

	private DeviceService service;

	public StreamMonitor(TupanClientInformation config, DeviceService service) {
		this.config = config;
		this.service = service;
	}

	@Override
	public void run() {
		DatagramSocket serverSocket;
		try {
			serverSocket = new DatagramSocket(this.config.getStreamPort());
			while (true) {
				byte[] receiveData = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				String payload = new String(receivePacket.getData());
				InetAddress address = receivePacket.getAddress();
				int port = receivePacket.getPort();
				LOGGER.info("Stream Channel[" + address.getCanonicalHostName() + ":" + port + "]:" + payload);
				this.service.processStream(payload);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
