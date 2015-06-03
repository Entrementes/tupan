package org.entrementes.tupan.entity;

import org.springframework.data.annotation.Id;

public class CustomerEntity {
	
	@Id
	private String code;
	
	private String name;
	
	private Double baseFare;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getBaseFare() {
		return baseFare;
	}
	
	public void setBaseFare(Double baseFare) {
		this.baseFare = baseFare;
	}

}
