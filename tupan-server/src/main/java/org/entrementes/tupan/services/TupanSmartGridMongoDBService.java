package org.entrementes.tupan.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.entrementes.tupan.entities.Consumption;
import org.entrementes.tupan.entities.SmartAppliance;
import org.entrementes.tupan.entities.User;
import org.entrementes.tupan.expection.SmartApplianceRegistrationConflicException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "1.0")
public class TupanSmartGridMongoDBService implements TupanSmartGridService{

	private static final Logger LOGGER = LoggerFactory.getLogger(TupanSmartGridMongoDBService.class);
	
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
		LOGGER.info("tupan service up");
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
		LOGGER.info("query done for {}/{}",request.getUtlitiesProviderId(),request.getUserId());
		return report;
	}

	private User loadUser(String userId, String utlitiesProviderId) {
		if(this.userRespository.countByUtlitiesProviderId(utlitiesProviderId) > 0) {
			User user = this.userRespository.findByUserIdAndUtlitiesProviderId(userId, utlitiesProviderId);
			if (user == null) {
				LOGGER.warn("tupan service: user not found {}/{}", utlitiesProviderId, userId);
				throw new UserNotFoundException("user not found {}".replace("{}", userId));
			}
			return user;
		}else{
			throw new UtilitiesProviderNotFoundException("utilities provider not found {}".replace("{}", utlitiesProviderId));
		}
	}

    private SmartApplianceRegistration loadDevice(String userId, String utlitiesProviderId, String equipmetId) {
        if(this.userRespository.countByUtlitiesProviderId(utlitiesProviderId) > 0) {
            User user = this.userRespository.findByUserIdAndUtlitiesProviderId(userId, utlitiesProviderId);
            if (user == null) {
                LOGGER.warn("tupan service: user not found {}/{}", utlitiesProviderId, userId);
                throw new UserNotFoundException("user not found {}".replace("{}", userId));
            }
            return this.applianceRepository.findByUserIdAndUtlitiesProviderIdAndEquipmentId(userId,utlitiesProviderId,equipmetId);
        }else{
            throw new UtilitiesProviderNotFoundException("utilities provider not found {}".replace("{}", utlitiesProviderId));
        }
    }

	@Override
	public void registerSmartAppliance(SmartApplianceRegistration registration) throws UtilitiesProviderNotFoundException, 
																						UserNotFoundException,
                                                                                        SmartApplianceRegistrationConflicException {

        SmartAppliance existingDevice =
            this.applianceRepository.findByUserIdAndUtlitiesProviderIdAndEquipmentId(
                registration.getUserId(),
                registration.getUtlitiesProviderId(),
                registration.getEquipmentId());
        if(existingDevice != null){
            throw new SmartApplianceRegistrationConflicException("appliance {} already registred for this user.".replace("{}", registration.getEquipmentId()));
        }
        loadUser(registration.getUserId(), registration.getUtlitiesProviderId());
		this.applianceRepository.save(this.mapper.map(registration, SmartAppliance.class));
		LOGGER.info("appliance {} registred for {}/{}", registration.getEquipmentId(), registration.getUtlitiesProviderId(), registration.getUserId());
	}

	@Override
	public void registerConsumptionReport(ConsumptionReport report)	throws UtilitiesProviderNotFoundException, 
																			UserNotFoundException {
		loadDevice(report.getUserId(), report.getUtlitiesProviderId(), report.getEquipmentId());
		this.consumptionRespository.save(this.mapper.map(report, Consumption.class));
		LOGGER.info("consumption report registred for {}/{}/{}", report.getUtlitiesProviderId(), report.getUserId(), report.getEquipmentId());
		
	}

	@Override
	public void reportGridUpdate() {
		List<SmartAppliance> bidirectionalEnabledAppliances = this.applianceRepository.findByReturnSocketIsNotNull();
		LOGGER.info("reporting change in grid state for {} devices", bidirectionalEnabledAppliances.size());
		for(SmartApplianceRegistration app : bidirectionalEnabledAppliances){
			SmartGridReportRequest request = new SmartGridReportRequest(app.getUtlitiesProviderId(), app.getUserId());
			SmartGridReport report = queryGridState(request);
			this.sender.sendReport(report, app.getReturnSocket());
		}
	}

    @Override
    public boolean hasCacheExpired(LocalDateTime lastQueryDate) {
        return this.smartGridConnection.getLastUpdate().compareTo(lastQueryDate) > 0;
    }

}
