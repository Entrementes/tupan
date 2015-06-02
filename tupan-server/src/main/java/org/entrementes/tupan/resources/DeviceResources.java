package org.entrementes.tupan.resources;

import org.entrementes.tupan.model.CostDifferentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceResources {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceResources.class);
	
	@RequestMapping(value="/device",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain","*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody String loopback(@RequestParam CostDifferentials differentials){
		LOGGER.info(differentials.toString());
		return "loopback ok.";
	}

}
