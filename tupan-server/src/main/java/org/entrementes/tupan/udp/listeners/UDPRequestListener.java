package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.entrementes.tupan.expection.EntityNotFoundException;
import org.entrementes.tupan.expection.SmartGridNotAvailableExecption;
import org.entrementes.tupan.expection.UserNotFoundException;
import org.entrementes.tupan.expection.UtilitiesProviderNotFoundException;
import org.entrementes.tupan.services.TupanSmartGridService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UDPRequestListener implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(UDPRequestListener.class);

	protected DatagramSocket socket;
	
	protected TupanSmartGridService service;

	protected ObjectMapper objectMapper;

	public UDPRequestListener(DatagramSocket socket, TupanSmartGridService service, ObjectMapper objectMapper) {
		this.socket = socket;
		this.service = service;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public void run() {
        LOGGER.info("initializing UDP listener thread.");
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true){                   
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				socket.receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			try {
				sendData = processRequestBody(receiveData);
			} catch (JsonParseException e1) {
				e1.printStackTrace();
			} catch (JsonMappingException e1) {
				e1.printStackTrace();
                sendData = "[400,\"Bad Request Body syntax\"]".getBytes();
			} catch (IOException e1) {
				e1.printStackTrace();
                sendData = "[500,\"Could not read Datagram\"]".getBytes();
			}catch (EntityNotFoundException e1){
				e1.printStackTrace();
                sendData = ("["+e1.getErrorCode()+",\""+e1.getMessage()+"\"]").getBytes();
			}catch (SmartGridNotAvailableExecption e1){
				e1.printStackTrace();
                sendData = "[502,\"Error connecting to SmartGrid\"]".getBytes();
			}
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);                   
			try {
				socket.send(sendPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	protected abstract byte[] processRequestBody(byte[] receiveDataØ) throws JsonParseException, JsonMappingException, IOException;
}
