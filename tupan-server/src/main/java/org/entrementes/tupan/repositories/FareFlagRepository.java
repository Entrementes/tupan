package org.entrementes.tupan.repositories;

import org.entrementes.tupan.entity.FareFlagEntity;
import org.entrementes.tupan.model.Flag;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FareFlagRepository extends MongoRepository<FareFlagEntity, Flag> {

}
