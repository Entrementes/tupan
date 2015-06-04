package org.entrementes.tupan.resources;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.entrementes.tupan.configuration.ApiInformation;
import org.entrementes.tupan.configuration.ServerInformation;
import org.entrementes.tupan.configuration.TupanInformation;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.expection.TupanExceptionCode;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.GridSimulation;
import org.entrementes.tupan.model.StateChange;
import org.entrementes.tupan.service.ElectricalGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SystemResources {
	
	private ApiInformation apiInfo;
	
	private ServerInformation serverInfo;
	
	private TupanInformation tupanInfo;
	
	private ElectricalGridService service;
	
	@Autowired
	public SystemResources(	ApiInformation apiInfo, 
							ServerInformation serverInfo,
							TupanInformation tupanInfo,
							ElectricalGridService service ) {
		this.apiInfo = apiInfo;
		this.serverInfo = serverInfo;
		this.tupanInfo = tupanInfo;
		this.service = service;
	}
	
	@RequestMapping(value="/info",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain","*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Map<String,Object> info(){
		Map<String, Object> result = buildSystemMap();
		return result;
	}

	public Map<String, Object> buildSystemMap() {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("contextPath", serverInfo.getContextPath());
		result.put("httpPort", serverInfo.getPort());
		result.put("apiId", apiInfo.getId());
		result.put("apiVersion", apiInfo.getVersion());
		result.put("streamPort", tupanInfo.getStreamPort());
		result.put("fareVariance", tupanInfo.getFareVariance());
		result.put("baseFare", tupanInfo.getFareDifferentialBase());
		result.put("historyBufferSize", tupanInfo.getHistoryBufferSize());
		result.put("poolingInterval", tupanInfo.getPoolingInterval());
		return result;
	}
	
	@RequestMapping(value="/info",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain","*"}, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Map<String,Object> updateSimulation(@Valid @RequestBody GridSimulation updated, BindingResult validationResult){
		if(validationResult.hasErrors()){
			throw new TupanException(TupanExceptionCode.BAD_REQUEST);
		}
		this.tupanInfo.update(updated);
		Map<String,Object> result = buildSystemMap();
		return result;
	}
	
	@RequestMapping(value="/status",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain","*"}, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody CostDifferentials getStatus(){
		return this.service.getElectricalFareDifferentials();
	}
	
	@RequestMapping(value="/status",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain","*"}, consumes={"*/*"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody CostDifferentials setStatus(@RequestBody StateChange change){
		return this.service.setState(change);
	}

}
