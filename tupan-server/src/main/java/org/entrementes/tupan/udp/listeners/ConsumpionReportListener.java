package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.List;

import org.entrementes.tupan.model.ConsumptionReport;
import org.entrementes.tupan.services.TupanSmartGridService;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConsumpionReportListener extends UDPRequestListener{

	public ConsumpionReportListener(DatagramSocket socket,
									TupanSmartGridService service, 
									ObjectMapper objectMapper) {
		super(socket, service, objectMapper);
	}

	@Override
	protected byte[] processRequestBody(byte[] receiveData) throws JsonParseException, 
																	JsonMappingException, 
																	IOException {
		ConsumptionReport report = this.objectMapper.readValue(receiveData, ConsumptionReport.class);
		this.service.registerConsumptionReport(report);
		List<Integer> code = Arrays.asList(200); 
		return this.objectMapper.writeValueAsBytes(code);
	}

}
