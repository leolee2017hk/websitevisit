package net.leolee;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import net.leolee.entity.HostExclusion;
import net.leolee.entity.WebVisit;
import net.leolee.entity.WebVisitDisplay;
import net.leolee.service.DataSourceService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class WebController {
	
	@Autowired
	DataSourceService dsService;
		
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }
	
	@RequestMapping(value = "/queryTopNWebVisit/{recordDate}", method = RequestMethod.GET)
    public List<WebVisitDisplay> queryTopN(@PathVariable("recordDate") String recordDate) {
		List<HostExclusion> hostExclusionList = dsService.getHostExclusion();
		
		return dsService.queryTopN(recordDate, hostExclusionList);
    }
}
