package org.entrementes.tupan.repositories;

import java.util.List;

import org.entrementes.tupan.entities.SmartAppliance;
import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmartApplianceRepository extends MongoRepository<SmartAppliance, String>{

	List<SmartAppliance> findByReturnSocketIsNotNull();

	SmartAppliance findByUserIdAndUtlitiesProviderIdAndEquipmentId(String userId, String utilitiesProviderId, String equipmentId);

}
