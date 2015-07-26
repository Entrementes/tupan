package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.List;

import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.entrementes.tupan.services.TupanSmartGridService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SmartApplianceRegistrationListener extends UDPRequestListener{

	public SmartApplianceRegistrationListener(DatagramSocket socket,
												TupanSmartGridService service, 
												ObjectMapper objectMapper) {
		super(socket, service, objectMapper);
	}

	@Override
	protected byte[] processRequestBody(byte[] receiveData) throws JsonParseException, 
																	JsonMappingException, 
																	IOException {
		SmartApplianceRegistration registration = this.objectMapper.readValue(receiveData, SmartApplianceRegistration.class);
		this.service.registerSmartAppliance(registration);
		List<Integer> code = Arrays.asList(201); 
		return this.objectMapper.writeValueAsBytes(code);
	}

}
