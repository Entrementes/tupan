package org.entrementes.tupan.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This value Object is used to request for the main API's
 * message. It contains all the required information to
 * identify the smart device's owner pricing policy.
 * 
 * @author Gunisalvo
 */
public class SmartGridReportRequest {
	
	@NotNull
	@Size(min=4, max=125)
	private String userId;
	
	@NotNull
	@Size(min=4, max=125)
	private String utlitiesProviderId;
	
	public SmartGridReportRequest() {
	}
	
	public SmartGridReportRequest(String utlitiesProviderId, String userId) {
		this.utlitiesProviderId = utlitiesProviderId;
		this.userId = userId;
	}

	/**
	 * @return the device's owner identification code in the
	 * electrical utilities service provider system.
	 */
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the electrical utilities service provider identification
	 * code. This information enables the construction of regional
	 * SmartGrid communication System where multiple utilities companies'
	 * data can be used.
	 */
	public String getUtlitiesProviderId() {
		return utlitiesProviderId;
	}
	
	public void setUtlitiesProviderId(String utlitiesProviderId) {
		this.utlitiesProviderId = utlitiesProviderId;
	}

}
