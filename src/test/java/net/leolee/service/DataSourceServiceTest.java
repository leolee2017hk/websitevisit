package net.leolee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyInt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import net.leolee.dao.WebVisitDAO;
import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.entity.WebVisitDisplay;
import net.leolee.util.SiteVisitHelper;

@RunWith(MockitoJUnitRunner.class)
public class DataSourceServiceTest {
	
	@InjectMocks
	DataSourceService dsSrc;
	
	@Mock
	WebVisitDAO webVisitDAO;
	
	@Mock
	private Environment env;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	SiteVisitHelper siteVisitHelper;
	
	
	@Test
	public void testPollDirectory() throws IOException{
		
		String testFile = "src/test/resources/TestData.txt";
		
		List<String> testData = new ArrayList();
		testData.add("date|website|visits");
		testData.add("2016-01-06|www.wikipedia.org|13246531");
		testData.add("2016-01-27|www.ebay.com.au|23154653");
		testData.add("2016-01-06|au.yahoo.com|11492756");
		
		Files.write(Paths.get(testFile), testData);
		
		List<WebVisit> resultWVList = dsSrc.pollDirectory(testFile);
		
		assertNotNull(resultWVList);
		assertEquals(testData.size() - 1, resultWVList.size());
	}
	
	@Test
	public void testFileNotExist() throws IOException{
		String testFile = "src/test/resources/TestData.txt";
		
		List<WebVisit> resultWVList = dsSrc.pollDirectory(testFile);
		
		assertNull(resultWVList);
	}
	
	@Test
	public void testQueryTopN() throws IOException{
		String testFile = "src/test/resources/TestData.txt";
		String recordDate = "2016-01-01";
		LocalDate localRecordDate = parse(recordDate);
		int topN = 2;

		List<HostExclusion> hostExclusionList = new ArrayList<>();
		
		HostExclusion he1 = new HostExclusion();
		he1.setHost("facebook.com");
		he1.setExcludedSince("2016-01-01");
		
		HostExclusion he2 = new HostExclusion();
		he2.setHost("youtube.com");
		
		hostExclusionList.add(he1);
		hostExclusionList.add(he2);
		
		List<WebVisit> wvList = new ArrayList<>();
	
		WebVisit wv1 = new WebVisit();
		wv1.setWebSite("google.com");
		wvList.add(wv1);
		
		String latestTimestamp = "20170101122430";
		when(env.getRequiredProperty("query.top.N")).thenReturn(String.valueOf(topN));
		when(webVisitDAO.getLatestFileStamp()).thenReturn(latestTimestamp);
		when(webVisitDAO.findTopNVisitCount(anyString(), any(), anyInt(), any())).thenReturn(wvList);
		
		List<WebVisitDisplay> resultWVList = dsSrc.queryTopN(recordDate, hostExclusionList);
		
		assertEquals(1, resultWVList.size());
	}
	
	private LocalDate parse(String dateString){
		LocalDate recordDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
		return recordDate;
	}
}
