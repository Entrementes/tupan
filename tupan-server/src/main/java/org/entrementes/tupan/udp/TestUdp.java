package org.entrementes.tupan.udp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class TestUdp {
	public static void main(String args[]) throws Exception {
		Long startTime = System.currentTimeMillis();
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("45.55.139.88");
		byte[] sendData;
		byte[] receiveData = new byte[576];
		String sentence = "[999999999999999,{\"userId\":\"gunisalvo\",\"utlitiesProviderId\":\"INFNET\"}]";
		sendData = sentence.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData,
				sendData.length, IPAddress, 9996);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		clientSocket.setSoTimeout(1000000);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
		System.out.println("FROM SERVER:" + modifiedSentence);
		clientSocket.close();
		Long endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime);
	}
}