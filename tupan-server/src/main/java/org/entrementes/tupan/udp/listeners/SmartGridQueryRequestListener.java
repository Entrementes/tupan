package org.entrementes.tupan.udp.listeners;

import java.io.IOException;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.util.Map;

import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;
import org.entrementes.tupan.services.DateUtils;
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
												throws IOException {
		Object[] requestBody = this.objectMapper.readValue(receiveData, Object[].class);
		LocalDateTime lastRequestTime = DateUtils.asLocalDateTimeFromMillisseconds(requestBody[0]);
        if(lastRequestTime != null &&
                this.service.hasCacheExpired(lastRequestTime)){
            Map<String,Object> parameters = (Map) requestBody[1];
            SmartGridReportRequest reportRequest = new SmartGridReportRequest();
            reportRequest.setUserId((String) parameters.get("userId"));
            reportRequest.setUtlitiesProviderId((String) parameters.get("utlitiesProviderId"));
            SmartGridReport result = this.service.queryGridState(reportRequest);
            return this.objectMapper.writeValueAsBytes(result);
        }else{
            return this.objectMapper.writeValueAsBytes("[304]");
        }
	}

}
