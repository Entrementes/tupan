package org.entrementes.tupan.resources;

import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceEndpoint {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceEndpoint.class);
	
	private DeviceService service;
	
	@Autowired
	public DeviceEndpoint(DeviceService service){
		this.service = service;
	}
	
	@RequestMapping(value="/fare",method=RequestMethod.GET, produces={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public Fare getFare(){
		LOGGER.info("loading current fare");
		return this.service.getFare();
	}

}
