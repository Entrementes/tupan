package org.entrementes.tupan.service.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.entrementes.tupan.configuration.TupanInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamLoopbackMonitor implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamLoopbackMonitor.class);
	
	private TupanInformation config;

	public StreamLoopbackMonitor(TupanInformation config) {
		this.config = config;
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
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
