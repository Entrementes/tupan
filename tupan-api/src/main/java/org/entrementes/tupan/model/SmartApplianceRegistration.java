package org.entrementes.tupan.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This is the feedback message sent to the server by the appliance.
 * It provides a data collection functionality enabling integration with
 * several pattern recognition tools. 
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
	private String equipmentId;

	@Size(min=4, max=25)
	private String manufacturer;

	@Size(min=4, max=25)
	private String applianceCategory;
	
	@Size(min=4, max=10)
	private String firmware;
	
	@Size(min=4, max=25)
	private String returnSocket;
	
	public SmartApplianceRegistration() {
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

	/**
	 * @return the unique code identifying the appliance individually.
	 * The specification suggests using the device's serial number but
	 * the decision is up to individual implementations.
	 */
	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	/**
	 * @return the smart appliance manufacturer's code.
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return this field is used to classify the appliance by
	 * type, ex: smart wall-plug.
	 */
	public String getApplianceCategory() {
		return applianceCategory;
	}

	public void setApplianceCategory(String applianceCategory) {
		this.applianceCategory = applianceCategory;
	}

	/**
	 * @return the appliance's embedded software system version. 
	 */
	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	/**
	 * @return this string contains the UDP return socket that allows the
	 * server to establish a return path to the appliance. 
	 */
	public String getReturnSocket() {
		return returnSocket;
	}

	public void setReturnSocket(String returnSocket) {
		this.returnSocket = returnSocket;
	}
	
}
