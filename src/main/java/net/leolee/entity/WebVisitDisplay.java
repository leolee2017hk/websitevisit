package net.leolee.entity;

import java.time.LocalDate;

public class WebVisitDisplay {
	
	private String recordDate;
	private String webSite;
	private Integer visitCount;
	private String fileTimestamp;
	
	public String getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getWebSite() {
		return webSite;
	}
	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}
	public Integer getVisitCount() {
		return visitCount;
	}
	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}
	
	public String getFileTimestamp() {
		return fileTimestamp;
	}
	public void setFileTimestamp(String fileTimestamp) {
		this.fileTimestamp = fileTimestamp;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("recordDate: " + this.recordDate).append(";");
		sb.append("webSite: " + this.webSite).append(";");
		sb.append("visitCount: " + this.visitCount).append(";");
		sb.append("fileTimestamp: " + this.fileTimestamp).append(";");
		
		return sb.toString();
		
	}
	
	
}
