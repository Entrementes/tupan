package org.entrementes.tupan.resources;

import java.time.LocalDateTime;

import org.entrementes.tupan.services.MockGridConnection;
import org.entrementes.tupan.services.TupanSmartGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/scada")
public class ScadaResources {
	
	private MockGridConnection gridConnection;
	
	private TupanSmartGridService tupanService;
	
	@Autowired
	public ScadaResources(MockGridConnection gridState) {
		this.gridConnection = gridState;
	}
	
	@RequestMapping(value="/state",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody MockGridConnection checkGridState(){
		return this.gridConnection;
	}
	
	@RequestMapping(value="/state",method=RequestMethod.POST, consumes={"application/json"}, produces={"application/json","application/xml","text/plain"})
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody MockGridConnection changeGridState(@RequestBody MockGridConnection stateChange){
		this.gridConnection = new MockGridConnection();
		this.gridConnection.setLastUpadate(LocalDateTime.now());
		this.gridConnection.setBaseFare(stateChange.getBaseFare());
		this.gridConnection.setFlag(stateChange.getFlag());
		this.gridConnection.setSystemMessage(stateChange.getSystemMessage());
		this.gridConnection.setUpdateInterval(stateChange.getUpdateInterval());
		
		this.tupanService.reportGridUpdate();
		
		return this.gridConnection;
	}

}
