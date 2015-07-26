package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.entrementes.tupan.expection.SmartGridNotAvailableExecption;
import org.entrementes.tupan.expection.UserNotFoundException;
import org.entrementes.tupan.expection.UtilitiesProviderNotFoundException;
import org.entrementes.tupan.services.TupanSmartGridService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class UDPRequestListener implements Runnable {

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
		byte[] receiveData = new byte[576];
		byte[] sendData = new byte[576];
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
			} catch (IOException e1) {
				e1.printStackTrace();
			}catch (UserNotFoundException e1){
				e1.printStackTrace();
			}catch (UtilitiesProviderNotFoundException e1){
				e1.printStackTrace();
			}catch (SmartGridNotAvailableExecption e1){
				e1.printStackTrace();
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
