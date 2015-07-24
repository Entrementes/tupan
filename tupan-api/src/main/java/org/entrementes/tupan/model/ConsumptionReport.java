package org.entrementes.tupan.model;

import java.time.Period;

/**
 * 
 * @author Gunisalvo
 */
public class ConsumptionReport {
	
	private String userId;
	
	private String utlitiesProviderId;
	
	private String equipamentId;
	
	private double electricalConsumption;
	
    private Period operationTime;
    
    private Boolean finished;
    
    public ConsumptionReport() {
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

	public double getElectricalConsumption() {
		return electricalConsumption;
	}

	public void setElectricalConsumption(double electricalConsumption) {
		this.electricalConsumption = electricalConsumption;
	}

	public Period getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Period operationTime) {
		this.operationTime = operationTime;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

}
