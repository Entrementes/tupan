package org.entrementes.tupan.entities;

import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.springframework.data.annotation.Id;

public class SmartAppliance extends SmartApplianceRegistration{
	
	@Id
	@Override
	public String getEquipamentId() {
		return super.getEquipamentId();
	}

}
