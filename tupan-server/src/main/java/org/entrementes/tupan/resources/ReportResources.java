package org.entrementes.tupan.resources;

import java.util.List;

import org.entrementes.tupan.entity.ChannelPerformanceEntity;
import org.entrementes.tupan.entity.DeviceRegistrationEntity;
import org.entrementes.tupan.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="reports")
public class ReportResources {
	
	private ReportService service;
	
	@Autowired
	public ReportResources(ReportService service){
		this.service = service;
	}
	
	@RequestMapping(value="/registrations",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain","*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody List<DeviceRegistrationEntity> registrations(){
		return this.service.listRegistrations();
	}
	
	@RequestMapping(value="/performances",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain","*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody List<ChannelPerformanceEntity> performances(){
		return this.service.listChannelPerformance();
	}

}
