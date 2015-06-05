package org.entrementes.tupan.resources;

import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookEndpoint {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebhookEndpoint.class);
	
	private DeviceService service;
	
	@Autowired
	public WebhookEndpoint(DeviceService service){
		this.service = service;
	}
	
	@RequestMapping(value="/postMeasurement",method=RequestMethod.POST, consumes={"application/json","application/xml"}, produces={"*/*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String loopback(@RequestBody CostDifferentials differentials){
		LOGGER.info(differentials.toString());
		this.service.processStream(differentials);
		return "loopback ok.";
	}

}
