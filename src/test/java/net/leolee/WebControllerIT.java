package net.leolee;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static java.util.Arrays.asList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebControllerIT {
	@LocalServerPort
    private int port;

    //private URL base;
    
    @Autowired
	private Environment env;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
     //   this.base = new URL("http://localhost:" + port + "/webvisit/queryTopNWebVisit/2016-01-06");
    }


    @Test
    public void testLoginSearchLogout() throws Exception {
    	String exePath = "bin/chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", exePath);
		WebDriver driver = new ChromeDriver();
		driver.get("http://localhost:" + port + "/webvisit/");
		
	    driver.findElement(By.id("username")).sendKeys(env.getRequiredProperty("web.username"));
	    driver.findElement(By.id("password")).sendKeys(env.getRequiredProperty("web.password"));  
	    driver.findElement(By.id("username")).submit();
	    
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    driver.findElement(By.id("date")).sendKeys("2016-01-06");
	    driver.findElement(By.id("report")).click();
	    
	    try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    List<WebElement> rows = driver.findElements(By.tagName("tr"));
	    List<List<String>> rowList = new ArrayList<>();
	    for (WebElement row: rows) {
	    	List<WebElement> cells = row.findElements(By.tagName("td"));
	    	List<String> cellTextList = new ArrayList<>();

	        for (WebElement cell: cells) {
	        	//System.out.println(cell.getTagName());
	        	if (!cell.getText().equals("")){
	        		cellTextList.add(cell.getText());
	        		//System.out.println(cell.getText());
	        	} 
	        }
	        if (cellTextList.size() > 0){
	        	rowList.add(cellTextList);    
	        }
	         
	    }
	    assertEquals(5, rowList.size());
	    
	    assertEquals(asList("2016-01-06", "www.google.com.au", "151749278"), rowList.get(0));
	    assertEquals(asList("2016-01-06", "www.facebook.com", "104346720"), rowList.get(1));
	    assertEquals(asList("2016-01-06", "www.youtube.com", "59811438"), rowList.get(2));
	    assertEquals(asList("2016-01-06", "www.google.com", "26165099"), rowList.get(3));
	    assertEquals(asList("2016-01-06", "ninemsn.com.au", "21734381"), rowList.get(4));
		  
	    
	    driver.findElement(By.id("logout")).click();

	    String text = driver.findElement(By.xpath("//form/p")).getText();
	    
	    assertEquals("You have been logged out.", text);

    }

}
