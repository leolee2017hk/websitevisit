package net.leolee;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebControllerTest {
	@Autowired
    private MockMvc mvc;

    @Test
    public void getLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/login").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    
    @Test
    public void getQueryTopN() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/queryTopNWebVisit/2016-01-01").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }
}
