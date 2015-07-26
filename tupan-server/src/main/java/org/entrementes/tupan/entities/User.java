package org.entrementes.tupan.entities;

import org.entrementes.tupan.model.ConsumerType;
import org.springframework.data.annotation.Id;

public class User {
	
	@Id
	private String userId;
	
	private String utlitiesProviderId;
	
	private ConsumerType userType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUtlitiesProviderId() {
		return utlitiesProviderId;
	}

	public void setUtlitiesProviderId(String utlitiesProviderId) {
		this.utlitiesProviderId = utlitiesProviderId;
	}

	public ConsumerType getUserType() {
		return userType;
	}

	public void setUserType(ConsumerType userType) {
		this.userType = userType;
	}

}
