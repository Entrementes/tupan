package org.entrementes.tupan.resources;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.entrementes.tupan.configuration.ApiInformation;
import org.entrementes.tupan.configuration.ServerInformation;
import org.entrementes.tupan.configuration.TupanInformation;
import org.entrementes.tupan.expection.TupanException;
import org.entrementes.tupan.model.GridSimulation;
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
	
	@Autowired
	public SystemResources(	ApiInformation apiInfo, 
							ServerInformation serverInfo,
							TupanInformation tupanInfo ) {
		this.apiInfo = apiInfo;
		this.serverInfo = serverInfo;
		this.tupanInfo = tupanInfo;
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
		result.put("baseFare", tupanInfo.getBaseFare());
		result.put("historyBufferSize", tupanInfo.getHistoryBufferSize());
		result.put("poolingInterval", tupanInfo.getPoolingInterval());
		return result;
	}
	
	@RequestMapping(value="/info",method=RequestMethod.POST, produces={"application/json","application/xml","text/plain","*"}, consumes={"application/json","application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody Map<String,Object> updateSimulation(@Valid @RequestBody GridSimulation updated, BindingResult validationResult){
		if(validationResult.hasErrors()){
			throw new TupanException();
		}
		this.tupanInfo.update(updated);
		Map<String,Object> result = buildSystemMap();
		return result;
	}

}
