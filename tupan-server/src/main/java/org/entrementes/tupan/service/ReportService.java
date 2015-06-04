package org.entrementes.tupan.service;

import java.util.Date;
import java.util.List;

import org.entrementes.tupan.entity.ChannelPerformanceEntity;
import org.entrementes.tupan.entity.DeviceRegistrationEntity;
import org.entrementes.tupan.repositories.ChannelPerformanceRepository;
import org.entrementes.tupan.repositories.DeviceRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {

	private DeviceRegistrationRepository deviceRepository;
	
	private ChannelPerformanceRepository performanceRepository;
	
	@Autowired
	public ReportService(DeviceRegistrationRepository deviceRepository,
			ChannelPerformanceRepository performanceRepository) {
		this.deviceRepository = deviceRepository;
		this.performanceRepository = performanceRepository;
	}
	
	public List<DeviceRegistrationEntity> listRegistrations() {
		return this.deviceRepository.findAll();
	}

	public List<ChannelPerformanceEntity> listChannelPerformance() {
		return this.performanceRepository.findAll();
	}
	
	public ChannelPerformanceEntity registerPerformance(ChannelPerformanceEntity newTestResult){
		ChannelPerformanceEntity result = this.performanceRepository.save(newTestResult);
		return result;
	}

	public ChannelPerformanceEntity registerPerformance(Long clockOn, Long clockOff, String channelName, String remoteAddr, Long timeDelta) {
		ChannelPerformanceEntity newTestResult = new ChannelPerformanceEntity();
		newTestResult.setChannelName(channelName);
		newTestResult.setIp(remoteAddr);
		newTestResult.setStartTime(new Date(clockOn));
		newTestResult.setEndTime(new Date(clockOff));
		newTestResult.setTimeDelta(timeDelta);
		ChannelPerformanceEntity result = this.performanceRepository.save(newTestResult);
		return result;
		
	}

}
