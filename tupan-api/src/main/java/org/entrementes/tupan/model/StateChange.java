package org.entrementes.tupan.model;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StateChange {
	
	@NotNull
	@XmlElement
	private TupanState state;

	public TupanState getState() {
		return state;
	}

	public void setState(TupanState status) {
		this.state = status;
	}

}
