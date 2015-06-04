package org.entrementes.tupan.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.annotation.Id;

@XmlRootElement
public class ChannelPerformanceEntity {

	@Id
	private BigInteger id;
	
	private String ip;
	
	private String channelName;
	
	private Date startTime;
	
	private Date endTime;

	private Long timeDelta;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getTimeDelta() {
		return timeDelta;
	}
	
	public void setTimeDelta(Long timeDelta) {
		this.timeDelta = timeDelta;
	}
	
}
