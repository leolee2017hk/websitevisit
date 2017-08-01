package net.leolee.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.util.SiteVisitHelper;

@RunWith(MockitoJUnitRunner.class)
public class WebVisitDAOTest {
	
	@InjectMocks
	WebVisitDAO dao;
	
	@Mock
    JdbcTemplate jdbcTemplate;
	
	@Mock
	SiteVisitHelper siteVisitHelper;

	@Test
	public void testFindTopNVisitCount() throws SQLException{
		List<WebVisit> wvList = new ArrayList<>();
		
		WebVisit wv1 = new WebVisit();
		wv1.setWebSite("google.com");
		wvList.add(wv1);
		
		when(siteVisitHelper.filterResultSet( any(), any(), any(), anyInt())).thenReturn(wvList);
		when(jdbcTemplate.query( anyString(), any(Object[].class), any(ResultSetExtractor.class))).thenReturn(wvList);
		
		List <WebVisit> webVisitRecs = jdbcTemplate.query("SELECT * FROM web_visit", 
    			new BeanPropertyRowMapper(WebVisit.class));
		
		
		String latestTimestamp = "20170101122430";
		LocalDate recordDate = LocalDate.of(2016, 01, 01);
		int topN = 2;
		
		List<HostExclusion> hostExclusionList = new ArrayList<>();
		
		HostExclusion he1 = new HostExclusion();
		he1.setHost("facebook.com");
		he1.setExcludedSince("2016-01-01");
		
		HostExclusion he2 = new HostExclusion();
		he2.setHost("youtube.com");
		
		hostExclusionList.add(he1);
		hostExclusionList.add(he2);
		
		Map<String, HostExclusion> hostExclusionMap = new HashMap<>();
		
		for (HostExclusion he: hostExclusionList){
			hostExclusionMap.put(he.getHost(), he);
		}
		
		List<WebVisit> resultWvList = dao.findTopNVisitCount(latestTimestamp, recordDate, topN, hostExclusionMap);
		assertEquals(wvList.size(), resultWvList.size());
	
	}
	
	@Test
	public void testAddWebVisit(){
		WebVisit wv1 = new WebVisit();
		wv1.setWebSite("google.com");
		
		dao.addWebVisit(wv1);
		verify(jdbcTemplate).update("INSERT INTO web_visit (record_date, web_site, visit_count, file_timestamp) "
        		+ " VALUES (?, ?, ?, ?)",
        		wv1.getRecordDate(), 
        		wv1.getWebSite(), 
        		wv1.getVisitCount(),
        		wv1.getFileTimestamp());
		
	}
	
	@Test
	public void testFindAll(){
		dao.findAll();
		verify(jdbcTemplate).query(anyString(), any(BeanPropertyRowMapper.class));
		
	}
	
	@Test
	public void testGetLatestFileStamp(){
		dao.getLatestFileStamp();
		verify(jdbcTemplate).queryForObject(anyString(), any(Class.class));
		
	}
	
	
}
