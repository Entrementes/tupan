package org.entrementes.tupan.model;

import java.time.LocalDate;

/**
 * This value Object is the main message of the Tupã API.
 * It should contain all the information needed for a smart
 * electronic device to make autonomous decisions like best
 * time to activate/deactivate or pool the network for the 
 * next report.
 * 
 * @author Gunisalvo
 */
public class SmartGridReport {
	
	private double electricalFare;
	
	private String consumerType;
	
	private String fareCode;
	
	private String systemStateCode;
	
	private LocalDate lastUpdate;
	
	private LocalDate nextUpdate;

	public SmartGridReport() {
	}
	
	/**
	 * @return current cost of the KW/h for a specific consumer.
	 */
	public double getElectricalFare() {
		return electricalFare;
	}

	public void setElectricalFare(double electricalFare) {
		this.electricalFare = electricalFare;
	}

	/**
	 * @return a specific classification code used to differentiate
	 * the price policy enforced to evaluate the electrical fare.
	 */
	public String getConsumerType() {
		return consumerType;
	}

	public void setConsumerType(String consumerType) {
		this.consumerType = consumerType;
	}

	/**
	 * @return a specific classification code used to identify time
	 * ranges where the Electrical Distribution Grid has a surplus or
	 * deficit of available energy.
	 */
	public String getFareCode() {
		return fareCode;
	}

	public void setFareCode(String fareCode) {
		this.fareCode = fareCode;
	}

	/**
	 * @return the state of the Electrical Distribution Grid. This
	 * value is be used to provide a event based communication system
	 * between the smart electrical device and the Grid's control System.
	 */
	public String getSystemStateCode() {
		return systemStateCode;
	}
	
	public void setSystemStateCode(String systemStateCode) {
		this.systemStateCode = systemStateCode;
	}
	
	/**
	 * @return the last time the system modified this report information
	 * for the smart device's region, customer type or time of the day.
	 * This change could be caused by a system's event, like a power shortage,
	 * or a periodical price update.
	 */
	public LocalDate getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(LocalDate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @return the next scheduled time for a periodic system status update.
	 * this value is used to help prevent pooling devices from overflowing
	 * the server with unnecessary status requests.
	 */
	public LocalDate getNextUpdate() {
		return nextUpdate;
	}

	public void setNextUpdate(LocalDate nextUpdate) {
		this.nextUpdate = nextUpdate;
	}

}
