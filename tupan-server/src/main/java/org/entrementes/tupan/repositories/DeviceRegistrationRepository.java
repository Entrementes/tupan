package org.entrementes.tupan.repositories;

import org.entrementes.tupan.entity.DeviceRegistrationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRegistrationRepository extends MongoRepository<DeviceRegistrationEntity, Long> {

}
