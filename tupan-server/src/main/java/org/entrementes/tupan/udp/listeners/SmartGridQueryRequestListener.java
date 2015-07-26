package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramSocket;

import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;
import org.entrementes.tupan.services.TupanSmartGridService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SmartGridQueryRequestListener extends UDPRequestListener implements Runnable {

	public SmartGridQueryRequestListener(	DatagramSocket socket,
											TupanSmartGridService service, 
											ObjectMapper objectMapper) {
		super(socket, service, objectMapper);
	}

	@Override
	protected byte[] processRequestBody(byte[] receiveData)
												throws JsonParseException, 
														JsonMappingException, 
														IOException {
		SmartGridReportRequest reportRequest = this.objectMapper.readValue(receiveData, SmartGridReportRequest.class);
		SmartGridReport result = this.service.queryGridState(reportRequest);
		return this.objectMapper.writeValueAsBytes(result);

	}

}
