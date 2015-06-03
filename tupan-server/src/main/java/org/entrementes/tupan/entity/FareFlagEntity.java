package org.entrementes.tupan.entity;

import org.entrementes.tupan.model.Flag;
import org.springframework.data.annotation.Id;

public class FareFlagEntity {
	
	@Id
	private Flag flag;
	
	private Double multiplier;

	public Flag getFlag() {
		return flag;
	}

	public void setFlag(Flag flag) {
		this.flag = flag;
	}

	public Double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Double multiplier) {
		this.multiplier = multiplier;
	}

}
