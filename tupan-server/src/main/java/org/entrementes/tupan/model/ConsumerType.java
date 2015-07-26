package org.entrementes.tupan.model;

public enum ConsumerType {
	
	RESIDENTIAL_A(1.1), INDUSTRIAL(1.0), SOCIAL(0.8)
	;
	
	private final double taxMultiplier;
	
	private ConsumerType(double taxMultiplier) {
		this.taxMultiplier = taxMultiplier;
	}
	
	public double getTaxMultiplier() {
		return taxMultiplier;
	}

}
