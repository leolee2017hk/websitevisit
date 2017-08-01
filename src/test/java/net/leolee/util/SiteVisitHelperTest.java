package net.leolee.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.entity.WebVisitDisplay;
import net.leolee.util.SiteVisitHelper;

@RunWith(MockitoJUnitRunner.class)
public class SiteVisitHelperTest {
	
	@Test
	public void testsearchHostExclusion(){
		SiteVisitHelper util = new SiteVisitHelper();
		Map<String, HostExclusion> exclusionMap = new HashMap<>();
		
		HostExclusion he1 = new HostExclusion();
		he1.setHost("facebook.com");
		
		exclusionMap.put("facebook.com", he1);
		
		HostExclusion he2 = new HostExclusion();
		he1.setHost("youtube.com");
		
		exclusionMap.put("youtube.com", he2);
		
		assertNull(util.searchHostExclusion("com", exclusionMap));
		assertNull(util.searchHostExclusion("www.facebook1.com", exclusionMap));
		assertNotNull(util.searchHostExclusion("www.facebook.com", exclusionMap));
		assertNotNull(util.searchHostExclusion("facebook.com", exclusionMap));
		assertNull(util.searchHostExclusion("www.facebook.com.hk", exclusionMap));
		
		assertNotNull(util.searchHostExclusion("www.abc.facebook.com", exclusionMap));
		
		assertNotNull(util.searchHostExclusion("youtube.com", exclusionMap));
		
	}

	@Test
	public void testFilterHost(){
		SiteVisitHelper helper = new SiteVisitHelper();
		
		Map<String, HostExclusion> exclusionMap = new HashMap<>();
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd");
		LocalDate recordDate = LocalDate.parse("2016-01-01", DateTimeFormatter.ISO_LOCAL_DATE);
		
		HostExclusion he1 = new HostExclusion();
		assertFalse(helper.filterHost(recordDate, he1));

		he1.setExcludedSince("2017-01-01");
		assertFalse(helper.filterHost(recordDate, he1));
		
		he1.setExcludedSince("2015-01-01");
		assertTrue(helper.filterHost(recordDate, he1));
		
		he1.setExcludedTill("2015-06-01");
		assertFalse(helper.filterHost(recordDate, he1));
		
		he1.setExcludedTill("2016-06-01");
		assertTrue(helper.filterHost(recordDate, he1));
		
		he1.setExcludedSince(null);
		assertTrue(helper.filterHost(recordDate, he1));
		
		he1.setExcludedTill("2015-06-01");
		assertFalse(helper.filterHost(recordDate, he1));
		
	}
	
	@Mock 
	ResultSet rs;
	
	@Test
	public void testFilterResultSet() throws SQLException{
		
		when(rs.next()).thenReturn(true, true, true, true, false);
		when(rs.getObject(1, LocalDate.class)).thenReturn(
												parse("2016-01-03"), 
												parse("2016-01-01"), 
												parse("2016-01-03"), 
												parse("2016-01-01"));
		when(rs.getString(2)).thenReturn("www.facebook.com", 
										 "www.youtube.com", 
										 "www.microsoft.com", 
										 "www.apple.com");
		
		
		SiteVisitHelper helper = new SiteVisitHelper();
		
		LocalDate recordDate = parse("2016-01-01");
		
		int topN = 2;
		
		Map<String, HostExclusion> exclusionMap = new HashMap<>();
		
		HostExclusion he1 = new HostExclusion();
		he1.setHost("youtube.com");
		he1.setExcludedSince("2016-01-01");
		
		exclusionMap.put("youtube.com", he1);
		
		HostExclusion he2 = new HostExclusion();
		he2.setHost("microsoft.com");
		he2.setExcludedSince("2016-02-01");
		
		exclusionMap.put("microsoft.com", he2);
		
		List<WebVisit> wvList = helper.filterResultSet(rs, recordDate, 
				exclusionMap, topN);
		
		assertEquals(2, wvList.size());
		
		assertEquals("www.facebook.com", wvList.get(0).getWebSite());
		assertEquals("www.microsoft.com", wvList.get(1).getWebSite());
		assertEquals("recordDate: 2016-01-03;webSite: www.facebook.com;visitCount: 0;fileTimestamp: null;", wvList.get(0).toString());

	}
	
	@Test
	public void testBuildWebVisitEntity(){
		SiteVisitHelper helper = new SiteVisitHelper();
		
		String line = "2016-01-06|www.bing.com|14065457";
		WebVisit wv = helper.buildWebVisitEntity(line, "20170730010101");
		
		assertNotNull(wv);
		assertEquals("www.bing.com", wv.getWebSite());
		assertEquals(parse("2016-01-06"), wv.getRecordDate());
		assertEquals(14065457, wv.getVisitCount().intValue());
		assertEquals("20170730010101", wv.getFileTimestamp());
		
	}
	
	@Test
	public void testConvertToDisplay(){
		
		SiteVisitHelper helper = new SiteVisitHelper();
		
		String line = "2016-01-06|www.bing.com|14065457";
		WebVisit wv = helper.buildWebVisitEntity(line, "20170730010101");

		WebVisitDisplay wvd = helper.convertToDisplay(wv);
		assertEquals(wv.getWebSite(), wvd.getWebSite());
		assertEquals(wv.getVisitCount(), wvd.getVisitCount());
		assertEquals(wv.getFileTimestamp(), wvd.getFileTimestamp());
		assertEquals(wv.getRecordDate().format(DateTimeFormatter.ISO_LOCAL_DATE), wvd.getRecordDate());
		
		assertEquals("recordDate: 2016-01-06;webSite: www.bing.com;visitCount: 14065457;fileTimestamp: 20170730010101;", wvd.toString());

	}
	
	private LocalDate parse(String dateString){
		LocalDate recordDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
		return recordDate;
	}
	
}
