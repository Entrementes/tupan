package org.entrementes.tupan.service;

import java.util.Calendar;

import org.entrementes.tupan.entity.FareFlagEntity;
import org.entrementes.tupan.model.Flag;
import org.entrementes.tupan.repositories.FareFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlagService {

	private FareFlagRepository repository;
	
	private Flag[] dummyDayModel = new Flag[]{	Flag.WHITE, Flag.WHITE, Flag.WHITE, 
												Flag.WHITE, Flag.WHITE, Flag.GREEN, 
												Flag.GREEN, Flag.GREEN, Flag.YELLOW, 
												Flag.YELLOW, Flag.RED, Flag.RED, 
												Flag.RED, Flag.YELLOW, Flag.YELLOW,
												Flag.YELLOW, Flag.YELLOW, Flag.RED, 
												Flag.RED, Flag.RED, Flag.RED, 
												Flag.YELLOW, Flag.GREEN, Flag.GREEN };
	
	@Autowired
	public FlagService(FareFlagRepository repository) {
		this.repository = repository;
	}
	
	public FareFlagEntity getCurrentFlag() {
		Calendar now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		return this.repository.findOne(this.dummyDayModel[hour]);
	}

}
