package org.entrementes.tupan.services;

import java.util.List;

import org.dozer.Mapper;
import org.entrementes.tupan.entities.Consumption;
import org.entrementes.tupan.entities.SmartAppliance;
import org.entrementes.tupan.entities.User;
import org.entrementes.tupan.expection.SmartGridNotAvailableExecption;
import org.entrementes.tupan.expection.UserNotFoundException;
import org.entrementes.tupan.expection.UtilitiesProviderNotFoundException;
import org.entrementes.tupan.model.ConsumptionReport;
import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;
import org.entrementes.tupan.repositories.ConsumptionRepository;
import org.entrementes.tupan.repositories.SmartApplianceRepository;
import org.entrementes.tupan.repositories.UserRepository;
import org.entrementes.tupan.udp.UDPSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TupanSmartGridMongoDBService implements TupanSmartGridService{

	private Mapper mapper;
	
	private SmartApplianceRepository applianceRepository;

	private ConsumptionRepository consumptionRespository;

	private UserRepository userRespository;

	private SmartGridConnection smartGridConnection;
	
	private UDPSender sender;
	
	@Autowired
	public TupanSmartGridMongoDBService(	Mapper mapper,
											SmartApplianceRepository applianceRepository,
											ConsumptionRepository consumptionRespository,
											UserRepository userRespository,
											SmartGridConnection smartGridConnection,
											UDPSender sender) {
		this.mapper = mapper;
		this.applianceRepository = applianceRepository;
		this.consumptionRespository = consumptionRespository;
		this.userRespository = userRespository;
		this.smartGridConnection = smartGridConnection;
		this.sender = sender;
	}
	
	@Override
	public SmartGridReport queryGridState(SmartGridReportRequest request) throws UtilitiesProviderNotFoundException, 
																					UserNotFoundException,
																					SmartGridNotAvailableExecption {
		User user = loadUser(request.getUserId(), request.getUtlitiesProviderId());
		SmartGridReport report = new SmartGridReport();
		report.setConsumerType(user.getUserType().toString());
		report.setElectricalFare(this.smartGridConnection.evaluateFare(user.getUserType()));
		report.setFareCode(this.smartGridConnection.getFareCode());
		report.setSystemStateCode(this.smartGridConnection.getSystemMessage());
		report.setLastUpdate(DateUtils.asDate(this.smartGridConnection.getLastUpdate()));
		report.setNextUpdate(DateUtils.asDate(this.smartGridConnection.getNextUpdate()));
		return report;
	}

	private User loadUser(String userId, String utlitiesProviderId) {
		User user = this.userRespository.findByUserIdAndUtlitiesProviderId(userId, utlitiesProviderId);
		if(user == null){
			throw new UserNotFoundException();
		}
		return user;
	}

	@Override
	public void registerSmartAppliance(SmartApplianceRegistration registration) throws UtilitiesProviderNotFoundException, 
																						UserNotFoundException {
		loadUser(registration.getUserId(), registration.getUtlitiesProviderId());
		this.applianceRepository.save(this.mapper.map(registration, SmartAppliance.class));
		
	}

	@Override
	public void registerConsumptionReport(ConsumptionReport report)	throws UtilitiesProviderNotFoundException, 
																			UserNotFoundException {
		loadUser(report.getUserId(), report.getUtlitiesProviderId());
		this.consumptionRespository.save(this.mapper.map(report, Consumption.class));
		
	}

	@Override
	public void reportGridUpdate() {
		List<SmartApplianceRegistration> bidirectionalEnabledAppliances = this.applianceRepository.findByReturnSocketIsNotNull();
		for(SmartApplianceRegistration app : bidirectionalEnabledAppliances){
			SmartGridReportRequest request = new SmartGridReportRequest(app.getUtlitiesProviderId(), app.getUserId());
			SmartGridReport report = queryGridState(request);
			this.sender.sendReport(report, app.getReturnSocket());
		}
	}

}
