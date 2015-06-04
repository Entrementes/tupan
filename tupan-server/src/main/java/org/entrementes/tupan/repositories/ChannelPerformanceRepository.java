package org.entrementes.tupan.repositories;

import java.math.BigInteger;

import org.entrementes.tupan.entity.ChannelPerformanceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelPerformanceRepository extends MongoRepository<ChannelPerformanceEntity, BigInteger>{

}
