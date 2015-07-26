package org.entrementes.tupan.entities;

public enum ConsumptionFlag {
	
	WHITE(0.8), ORANGE(1.1), RED(2.0);

	private final double incentive;
	
	private ConsumptionFlag(double incentive) {
		this.incentive = incentive;
	}
	
	public double getIncentive() {
		return this.incentive;
	}

}
