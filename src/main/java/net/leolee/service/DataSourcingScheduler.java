package net.leolee.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataSourcingScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(DataSourcingScheduler.class);
	
	@Autowired
	private Environment env;

	
	@Autowired
	DataSourceService dsService;
	
	@Scheduled(fixedDelay = 10000)
    public void readNewFile() {
		String filePath = env.getRequiredProperty("source.path");
        log.info("Polling the directory [%s]... ", filePath);
        dsService.pollDirectory(filePath);
        
        
    }
        
        
        
}

