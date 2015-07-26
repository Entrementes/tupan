package org.entrementes.tupan.repositories;

import java.math.BigInteger;

import org.entrementes.tupan.entities.Consumption;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsumptionRepository extends MongoRepository<Consumption, BigInteger>{

}
