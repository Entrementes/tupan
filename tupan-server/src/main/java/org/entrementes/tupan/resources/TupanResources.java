package org.entrementes.tupan.resources;

import org.entrementes.tupan.expection.InvalidRegistrationException;
import org.entrementes.tupan.model.ConsumptionReport;
import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;
import org.entrementes.tupan.services.DateUtils;
import org.entrementes.tupan.services.ServiceCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/smart-grid")
public class TupanResources {
	
	private ServiceCatalog catalog;
	
	@Autowired
	public TupanResources(ServiceCatalog catalog) {
		this.catalog = catalog;
	}
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}",method=RequestMethod.GET, produces={"application/json","application/xml","text/plain"})
	public @ResponseBody ResponseEntity<SmartGridReport> queryGridState(@RequestHeader(value = "Tupan-Version", required = false)
				Optional<String> version, @RequestHeader(value = "If-Modified-Since", required = false) Optional<String> dateString,
                @PathVariable(value="utlities-provider-id") String utlitiesProviderId, @PathVariable(value="user-id") String userId){
        if(dateString.isPresent()){
            LocalDateTime lastQueryDate = DateUtils.parseWebDate(dateString.get());
            if(this.catalog.getServiceByVersion(version).hasCacheExpired(lastQueryDate)){
                return newStateQuery(version, utlitiesProviderId, userId);
            }else {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
            }
        }else {
            return newStateQuery(version, utlitiesProviderId, userId);
        }
	}

    private ResponseEntity<SmartGridReport> newStateQuery(Optional<String> version, String utlitiesProviderId, String userId){
        SmartGridReport currentReport = catalog.getServiceByVersion(version).queryGridState(new SmartGridReportRequest(utlitiesProviderId, userId));
        return new ResponseEntity<>(currentReport, HttpStatus.OK);
    }
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}",method=RequestMethod.POST, consumes={"application/json","application/xml"})
	public ResponseEntity<String> registerSmartAppliance(@RequestHeader(value = "Tupan-Version", required = false)
					Optional<String> version, @Valid @RequestBody SmartApplianceRegistration connectedDevice, BindingResult validationResult) {

		if(validationResult.hasErrors()){
			throw new InvalidRegistrationException("Problems registering appliance: " + parseValidationErrors(validationResult.getFieldErrors()));
		}
		this.catalog.getServiceByVersion(version).registerSmartAppliance(connectedDevice);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{utlities-provider-id}/{user-id}/{equipment-id}",method=RequestMethod.POST)
	public ResponseEntity<String> registerConsumptionReport(@RequestHeader(value = "Tupan-Version", required = false)
					Optional<String> version, @Valid @RequestBody ConsumptionReport report, BindingResult validationResult) {
		if(validationResult.hasErrors()){
			throw new InvalidRegistrationException("Problems registering consumption: " + parseValidationErrors(validationResult.getFieldErrors()));
		}
		this.catalog.getServiceByVersion(version).registerConsumptionReport(report);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	private String parseValidationErrors(List<FieldError> errors) {
        return errors.stream().map(e -> e.getField() + " : " + e.getCode() + " , value : " + e.getRejectedValue())
				.collect(Collectors.joining("; "));
	}

}
