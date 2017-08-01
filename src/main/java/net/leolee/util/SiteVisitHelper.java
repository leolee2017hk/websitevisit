package net.leolee.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.entity.WebVisitDisplay;

@Component
public class SiteVisitHelper {
	
	public WebVisit buildWebVisitEntity(String line, String fileTimeStamp){
		String[] fields = line.split("\\|");
		LocalDate ldate = LocalDate.parse(fields[0], 
								DateTimeFormatter.ISO_LOCAL_DATE);
	
		WebVisit wv = new WebVisit();
		
		wv.setRecordDate(ldate);
		wv.setVisitCount(Integer.parseInt(fields[2]));
		wv.setWebSite(fields[1]);
		wv.setFileTimestamp(fileTimeStamp);
		
		return wv;
	}
	
	public HostExclusion searchHostExclusion(String webSite, Map<String, HostExclusion> exclusionMap){
		String[] parts =  webSite.split("\\.");
		
		int size = parts.length;
		int i = 0;
		String host = "";
		while (i < size){
			host = parts[size -i -1] + host;
			if (exclusionMap.containsKey(host)){
				return exclusionMap.get(host);
			}
			host = "." + host;
			i++;
		}
		return null;
	}
	
	public List<WebVisit> filterResultSet(final ResultSet rs, final LocalDate recordDate, 
									final Map<String, HostExclusion> exclusionMap,
									final int topN) throws SQLException{
		int resultCnt = 0;
        List<WebVisit> list=new ArrayList<>();  
        while(rs.next()){  
        	
            LocalDate recodDate = rs.getObject(1, LocalDate.class);
            String webSite = rs.getString(2);
            
            HostExclusion exclusion = searchHostExclusion(webSite, exclusionMap);
            if (exclusion != null){
            	if (filterHost(recordDate, exclusion)){
            		continue;
            	}
            }
            WebVisit e= new WebVisit();  
            e.setRecordDate(recodDate);
            
            e.setWebSite(webSite);  
            e.setVisitCount(rs.getInt(3));  
            e.setFileTimestamp(rs.getString(4)); 
            list.add(e); 
            resultCnt++;
        	if (resultCnt >= topN){
        		break;
        	}
        }  
        return list;  
 
		
	}
	
	public boolean filterHost(LocalDate recordDate, HostExclusion exclusion){
		
		LocalDate excludedSince = null;
		LocalDate excludedTill = null;
		
		if (exclusion.getExcludedSince() != null){
			excludedSince = LocalDate.parse(exclusion.getExcludedSince(), DateTimeFormatter.ISO_LOCAL_DATE);
		}
		
		if (exclusion.getExcludedTill() != null){
			excludedTill = LocalDate.parse(exclusion.getExcludedTill(), DateTimeFormatter.ISO_LOCAL_DATE);
		}
		
		if ((excludedSince != null) && (excludedTill != null)){
			if ((recordDate.isAfter(excludedSince)) 
					&& (recordDate.isBefore(excludedTill))){
				return true;
			}
		}
		
		if ((excludedSince == null) && (excludedTill == null)){
			return false;
		}
		
		if ((excludedSince == null) && (!recordDate.isAfter(excludedTill))){
			return true;
		}
		
		if ((excludedTill == null) && (!recordDate.isBefore(excludedSince))){
			return true;
		}
		
		return false;
	}

	public WebVisitDisplay convertToDisplay(WebVisit wv){
		WebVisitDisplay wvd = new WebVisitDisplay();
		wvd.setWebSite(wv.getWebSite());
		wvd.setRecordDate(wv.getRecordDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
		wvd.setFileTimestamp(wv.getFileTimestamp());
		wvd.setVisitCount(wv.getVisitCount());
		
		return wvd;
		
	}
}
