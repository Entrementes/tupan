package org.entrementes.tupan.resources;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.entrementes.tupan.model.Device;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	
	@Autowired
	private CustomerResources(CustomerService service){
		this.service = service;
	}
	
	@RequestMapping(value="/{customer-code}",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Fare loadCustomerFare(@PathVariable(value="customer-code") String customerCode){
		return this.service.loadCustomerFare(customerCode);
	}
	
	@RequestMapping(value="/{customer-code}",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain"}, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Fare loadCustomerFare(@PathVariable(value="customer-code") String customerCode, @RequestBody Device connectedDevice, HttpServletRequest request) throws UnknownHostException{
		InetAddress requestAddress = InetAddress.getByName(request.getRemoteHost());
		return this.service.connectDevice(customerCode, connectedDevice, requestAddress);
	}

}
