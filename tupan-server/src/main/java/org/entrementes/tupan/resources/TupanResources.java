package org.entrementes.tupan.resources;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.entrementes.tupan.model.ConsumptionReport;
import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;
import org.entrementes.tupan.services.TupanSmartGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/grid")
public class TupanResources {
	
	private TupanSmartGridService service;
	
	@Autowired
	public TupanResources(TupanSmartGridService service) {
		this.service = service;
	}
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody SmartGridReport queryGridState(@PathVariable(value="utlities-provider-id") String utlitiesProviderId, @PathVariable(value="user-id") String userId){
		return service.queryGridState(new SmartGridReportRequest(utlitiesProviderId, userId));
	}
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}",method=RequestMethod.POST, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> registerSmartAppliance(@Valid @RequestBody SmartApplianceRegistration connectedDevice, BindingResult validationResult) {
		if(validationResult.hasErrors()){
			//
		}
		this.service.registerSmartAppliance(connectedDevice);
		//System.out.println(connectedDevice);
		return new ResponseEntity<String>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}/{equipment-id}",method=RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<String> registerConsumptionReport( @Valid @RequestBody ConsumptionReport report, BindingResult validationResult) {
		if(validationResult.hasErrors()){
			//
		}
		this.service.registerConsumptionReport(report);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
