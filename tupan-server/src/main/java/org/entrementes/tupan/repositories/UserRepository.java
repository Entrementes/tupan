package org.entrementes.tupan.repositories;

import org.entrementes.tupan.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{

	User findByUserIdAndUtlitiesProviderId(String userId, String utlitiesProviderId);

	long countByUtlitiesProviderId(String utlitiesProviderId);

}
