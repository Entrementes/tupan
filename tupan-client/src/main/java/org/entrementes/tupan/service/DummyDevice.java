package org.entrementes.tupan.service;

import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.Fare;
import org.entrementes.tupan.model.Flag;
import org.entrementes.tupan.model.TupanState;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class DummyDevice {

	private GpioController gpio;
	
	private GpioPinDigitalOutput greenFlag;
	
	private GpioPinDigitalOutput yellowFlag;
	
	private GpioPinDigitalOutput redFlag;
	
	private GpioPinDigitalOutput systemOk;
	
	private GpioPinDigitalOutput systemWarn;
	
	private GpioPinDigitalOutput systemFail;
	
	private GpioPinDigitalInput deviceSwitch;
	
	public DummyDevice() {
		this.gpio = GpioFactory.getInstance();
        this.greenFlag = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW);
        this.yellowFlag = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);
        this.redFlag = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW);
        this.systemOk = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_12, PinState.LOW);
        this.systemWarn = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_13, PinState.LOW);
        this.systemFail = this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_14, PinState.LOW);
        this.deviceSwitch  = this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_01);
	}
	
	public void setFlag(Flag flag){
		switch (flag) {
		case GREEN:
			this.greenFlag.setState(PinState.HIGH);
			this.yellowFlag.setState(PinState.LOW);
			this.redFlag.setState(PinState.LOW);
			break;
			
		case YELLOW:
			this.greenFlag.setState(PinState.LOW);
			this.yellowFlag.setState(PinState.HIGH);
			this.redFlag.setState(PinState.LOW);
			break;

		case RED:
			this.greenFlag.setState(PinState.LOW);
			this.yellowFlag.setState(PinState.LOW);
			this.redFlag.setState(PinState.HIGH);
			break;
			
		default:
			this.greenFlag.setState(PinState.HIGH);
			this.yellowFlag.setState(PinState.HIGH);
			this.redFlag.setState(PinState.HIGH);
			break;
		}
	}
	
	public void setState(TupanState state){
		switch (state) {
		case OK:
			this.systemOk.setState(PinState.HIGH);
			this.systemWarn.setState(PinState.LOW);
			this.systemFail.setState(PinState.LOW);
			break;
			
		case WARN:
			this.systemOk.setState(PinState.LOW);
			this.systemWarn.setState(PinState.HIGH);
			this.systemFail.setState(PinState.LOW);
			break;
			
		case FAIL:
			this.systemOk.setState(PinState.LOW);
			this.systemWarn.setState(PinState.LOW);
			this.systemFail.setState(PinState.HIGH);
			break;

		default:
			this.systemOk.setState(PinState.HIGH);
			this.systemWarn.setState(PinState.HIGH);
			this.systemFail.setState(PinState.HIGH);
			break;
		}
	}

	public void checkFare(Fare currentFare, CostDifferentials differentials) {
		// TODO Auto-generated method stub
		
	}
	
}
