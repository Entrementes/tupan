package org.entrementes.tupan.repositories;

import org.entrementes.tupan.entity.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<CustomerEntity, String> {

    public CustomerEntity findByCode(String customerCode);

}