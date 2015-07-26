package org.entrementes.tupan.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.entrementes.tupan.model.SmartGridReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UDPSender {

	private ObjectMapper objectMapper;
	
	@Autowired
	public UDPSender(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public void sendReport(SmartGridReport report, String socketValue){
		int port = getPort(socketValue);
		String host = getHost(socketValue);
		try(DatagramSocket clientSocket = new DatagramSocket()){
			InetAddress IPAddress = InetAddress.getByName(host);
			byte[] sendData = new byte[576];
			sendData = this.objectMapper.writeValueAsBytes(report);
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, IPAddress, port);
			clientSocket.send(sendPacket);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private String getHost(String socketValue) {
		if(socketValue == null || socketValue.isEmpty() || !socketValue.contains(":")){
			throw new IllegalArgumentException("invalid socket value.");
		}
		return socketValue.split(":")[0];
	}

	private int getPort(String socketValue) {
		if(socketValue == null || socketValue.isEmpty() || !socketValue.contains(":")){
			throw new IllegalArgumentException("invalid socket value.");
		}
		return Integer.parseInt(socketValue.split(":")[1]);
	}
}
