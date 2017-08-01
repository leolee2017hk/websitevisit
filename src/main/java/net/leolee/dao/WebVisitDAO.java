package net.leolee.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.util.SiteVisitHelper;

@Repository
@Qualifier("webVisitDao")
public class WebVisitDAO {
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	SiteVisitHelper siteVisitHelper;
	
	public List<WebVisit> findAll() {
        List <WebVisit> webVisitRecs = jdbcTemplate.query("SELECT * FROM web_visit", 
        			new BeanPropertyRowMapper(WebVisit.class));
        return webVisitRecs;
    }
	
	public String getLatestFileStamp(){
		String sql = "SELECT max(file_timestamp) FROM web_visit";
		String maxFileTimestamp = jdbcTemplate.queryForObject(sql, String.class); 
		return maxFileTimestamp;
		
	}
	
	
	public List<WebVisit> findTopNVisitCount(final String fileTimeStamp, final LocalDate recordDate, final int topN, 
													final Map<String, HostExclusion> exclusionMap) {
		String sql = "SELECT record_date, web_site, visit_count, file_timestamp "
					 + " FROM web_visit where file_timestamp = ? and "
					+  " record_date = ? order by visit_count desc ";
        List<WebVisit> webVisitRecs = jdbcTemplate.query(sql, 
        			new Object[] { fileTimeStamp, recordDate },  
        			new ResultSetExtractor<List<WebVisit>>(){  
            	@Override  
             public List<WebVisit> extractData(ResultSet rs) throws SQLException,  
                    DataAccessException {  
            	return siteVisitHelper.filterResultSet(rs, recordDate, 
									exclusionMap, topN);
            }});  
          
        return webVisitRecs;
    }
	
	
	public void addWebVisit(final WebVisit visitCount) {
        jdbcTemplate.update("INSERT INTO web_visit (record_date, web_site, visit_count, file_timestamp) "
        		+ " VALUES (?, ?, ?, ?)",
        		visitCount.getRecordDate(), 
        		visitCount.getWebSite(), 
        		visitCount.getVisitCount(),
        		visitCount.getFileTimestamp());
        System.out.println("record Added!!");
    }
}
