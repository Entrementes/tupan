package org.entrementes.tupan.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Gunisalvo
 */
public class SmartApplianceRegistration {
	
	@NotNull
	@Size(min=4, max=125)
	private String userId;
	
	@NotNull
	@Size(min=4, max=125)
	private String utlitiesProviderId;
	
	@NotNull
	@Size(min=4, max=125)
	private String equipamentId;
	
	@NotNull
	@Size(min=4, max=25)
	private String manufacturer;
	
	@NotNull
	@Size(min=4, max=25)
	private String applianceCategory;
	
	@Size(min=4, max=10)
	private String firmware;
	
	@Size(min=4, max=25)
	private String retornSocket;
	
	public SmartApplianceRegistration() {
	}

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

	public String getEquipamentId() {
		return equipamentId;
	}

	public void setEquipamentId(String equipamentId) {
		this.equipamentId = equipamentId;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getApplianceCategory() {
		return applianceCategory;
	}

	public void setApplianceCategory(String applianceCategory) {
		this.applianceCategory = applianceCategory;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public String getRetornSocket() {
		return retornSocket;
	}

	public void setRetornSocket(String retornSocket) {
		this.retornSocket = retornSocket;
	}
	
}
