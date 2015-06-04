package org.entrementes.tupan.resources;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.CustomerService;
import org.entrementes.tupan.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="customers")
public class CustomerResources {
	
	private CustomerService service;
	
	private ReportService reports;
	
	@Autowired
	private CustomerResources(CustomerService service, ReportService reports){
		this.service = service;
		this.reports = reports;
	}
	
	@RequestMapping(value="/{customer-code}",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Fare loadCustomerFare(@PathVariable(value="customer-code") String customerCode, HttpServletRequest request){
		Long clockOn = System.currentTimeMillis();
		Fare result = this.service.loadCustomerFare(customerCode);
		Long clockOff = System.currentTimeMillis();
		this.reports.registerPerformance(clockOn,clockOff,"POOLING",request.getRemoteAddr(), clockOff - clockOn);
		return result;
	}
	
	@RequestMapping(value="/{customer-code}",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain"}, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Fare loadCustomerFare( @PathVariable(value="customer-code") String customerCode, 
												@Valid @RequestBody Device connectedDevice, 
												HttpServletRequest request, 
												BindingResult validationResult) throws UnknownHostException {
		if(validationResult.hasErrors()){
			throw new TupanException(TupanExceptionCode.BAD_REQUEST);
		}
		InetAddress requestAddress = InetAddress.getByName(request.getRemoteHost());
		return this.service.connectDevice(customerCode, connectedDevice, requestAddress);
	}

}
