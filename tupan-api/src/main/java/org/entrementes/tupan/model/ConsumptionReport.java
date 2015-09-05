package org.entrementes.tupan.model;


/**
 * This feedback message allows the discrimination of the power consumption of
 * each smart appliance.
 * 
 * @author Gunisalvo
 */
public class ConsumptionReport {
	
	private String userId;
	
	private String utlitiesProviderId;
	
	private String equipmentId;
	
	private double electricalConsumption;
	
    private Long operationTime;
    
    public ConsumptionReport() {
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
	 * @return the number of Watts consumed by the device.
	 */
	public double getElectricalConsumption() {
		return electricalConsumption;
	}

	public void setElectricalConsumption(double electricalConsumption) {
		this.electricalConsumption = electricalConsumption;
	}

	/**
	 * @return the duration of time the device spent to complete the operation,
	 * the time unit is millisecond.
	 */
	public Long getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Long operationTime) {
		this.operationTime = operationTime;
	}

}
