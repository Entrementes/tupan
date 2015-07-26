package org.entrementes.tupan.repositories;

import org.entrementes.tupan.entities.SmartAppliance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmartApplianceRepository extends MongoRepository<SmartAppliance, String>{

}
