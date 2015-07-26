package org.entrementes.tupan.entities;

import java.math.BigInteger;

import org.entrementes.tupan.model.ConsumptionReport;
import org.springframework.data.annotation.Id;

public class Consumption extends ConsumptionReport{
	
	@Id
	private BigInteger id;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

}
