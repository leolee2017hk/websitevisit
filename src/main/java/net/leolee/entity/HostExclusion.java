package net.leolee.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HostExclusion {
	
	private String host;
	private String excludedSince;
	private String excludedTill;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getExcludedSince() {
		return excludedSince;
	}
	public void setExcludedSince(String excludedSince) {
		this.excludedSince = excludedSince;
	}
	public String getExcludedTill() {
		return excludedTill;
	}
	public void setExcludedTill(String excludedTill) {
		this.excludedTill = excludedTill;
	}

	
	
}
