package net.leolee.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import net.leolee.dao.WebVisitDAO;
import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.entity.WebVisitDisplay;
import net.leolee.util.SiteVisitHelper;


@Component
public class DataSourceService {

	@Autowired
	WebVisitDAO webVisitDAO;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	SiteVisitHelper siteVisitHelper;

	private static final Logger log = LoggerFactory.getLogger(DataSourceService.class);
	
	
	public List<WebVisit> pollDirectory(final String filePath){
		Path sourcePath = Paths.get(filePath);
		
		if (Files.notExists(sourcePath)){
			log.info(String.format("File [%s] does not exist !", sourcePath.toString()));
			return null;
		}
		List<WebVisit> webVisitRecs = new ArrayList<>();
		
        List<String> list = new ArrayList<>();
        
        try (BufferedReader br = Files.newBufferedReader(sourcePath)) {

			//br returns as stream and convert it into a List
			list = br.lines().collect(Collectors.toList());
			String fileTimeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
			
			int count = 0;
			for (String line: list){
				count++;
				if (count == 1){ //skip the header
					continue;
				}
				
				WebVisit wv = siteVisitHelper.buildWebVisitEntity(line, fileTimeStamp);
				webVisitDAO.addWebVisit(wv);
				webVisitRecs.add(wv);
				
			}

		} catch (IOException e) {
			log.error("IO Error", e);
		}
        
        try{
        	removeSource(sourcePath);
        }catch (IOException e) {
			log.error("IO Error", e);
		}
        
        return webVisitRecs;
	}
	
	public void removeSource(final Path sourcePath) throws IOException{
		Files.delete(sourcePath);
		log.info(String.format("File [%s] is deleted !", sourcePath.toString()));
	}
	
	public List<WebVisitDisplay> queryTopN(final String recordDate, final List<HostExclusion> hostExclusionList){
		int topN = Integer.parseInt(env.getRequiredProperty("query.top.N"));
		String fileTimeStamp = webVisitDAO.getLatestFileStamp();
		
		Map<String, HostExclusion> hostExclusionMap = new HashMap<>();
		
		for (HostExclusion he: hostExclusionList){
			hostExclusionMap.put(he.getHost(), he);
		}
		
		LocalDate ldate = LocalDate.parse(recordDate, 
				DateTimeFormatter.ISO_LOCAL_DATE);
		
		List<WebVisit> webVisitList = webVisitDAO.findTopNVisitCount(fileTimeStamp, ldate, 
												topN, hostExclusionMap);
		
		List<WebVisitDisplay> webVisitDisplayList = new ArrayList<>();
		
		webVisitList.stream().forEach(e -> 
							webVisitDisplayList.add(siteVisitHelper.convertToDisplay(e)));
		return webVisitDisplayList;

	}
	
	public List<HostExclusion> getHostExclusion(){
		ResponseEntity<List<HostExclusion>> rateResponse =
		        restTemplate.exchange(env.getRequiredProperty("exclusion.url"),
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<HostExclusion>>() {
		            });
		return rateResponse.getBody();
	}

}
