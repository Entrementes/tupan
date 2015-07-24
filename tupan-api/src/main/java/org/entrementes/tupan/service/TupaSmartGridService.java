package org.entrementes.tupan.service;

import org.entrementes.tupan.expection.SmartGridNotAvailableExecption;
import org.entrementes.tupan.expection.UtilitiesProviderNotFoundException;
import org.entrementes.tupan.expection.UserNotFoundException;
import org.entrementes.tupan.model.ConsumptionReport;
import org.entrementes.tupan.model.SmartApplianceRegistration;
import org.entrementes.tupan.model.SmartGridReport;
import org.entrementes.tupan.model.SmartGridReportRequest;

/**
 * Tupã is a communication framework intended to provide a efficient
 * message exchange system between Smart Electrical Grids and  computer 
 * systems embedded in home appliances. It is build on concepts like Internet
 * of Things and TCP/IP network model.
 * 
 * @author Gunisalvo
 */
public interface TupaSmartGridService {
	
	/**
	 * This method provides the main service call of the communication system.
	 * 
	 * @param request, contains the user identification so the implementation
	 * can evaluate the price policy and Electrical Grid state according to the
	 * Electrical Utilities provider information.
	 * 
	 * @return a report with all the information required to automate the decision
	 * process of a smart home appliance.
	 * 
	 * @throws UtilitiesProviderNotFoundException
	 * @throws UserNotFoundException
	 */
	SmartGridReport queryGridState(SmartGridReportRequest request) throws UtilitiesProviderNotFoundException, 
																			UserNotFoundException,
																			SmartGridNotAvailableExecption;
	
	/**
	 * This method provides a way to account for all the smart appliances connected to the system.
	 * This feedback is an important part of the SmartGrid communication model, albeit not essential.
	 * 
	 * @param registration, this value object contains the smart appliance's core information. It is
	 * be used to provide a data set for specialized tools, like machine learning.
	 * 
	 * @throws UtilitiesProviderNotFoundException
	 * @throws UserNotFoundException
	 */
	void registerSmartAppliance(SmartApplianceRegistration registration) throws UtilitiesProviderNotFoundException, 
																			UserNotFoundException;
	
	/**
	 * Another, more fine grained, method to collect electrical consumption feedback. The method let's
	 * smart appliances report their electrical consumption .
	 * 
	 * @param report contains the electrical consumption report. It can be a work unit based inform, from the 
	 * moment the appliance was turned on to the moment it went off, or an ongoing operation inform for longer
	 * operational times.
	 * 
	 * @throws UtilitiesProviderNotFoundException
	 * @throws UserNotFoundException
	 */
	void registerConsumptionReport(ConsumptionReport report) throws UtilitiesProviderNotFoundException, 
																			UserNotFoundException;

}
