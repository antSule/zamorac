package hr.fer.progi.ticketmestar;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;

@SpringBootTest
@AutoConfigureMockMvc
public class NonExistingRouteTest {
    private static final Logger logger = LoggerFactory.getLogger(NonExistingRouteTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void nonExistingRoute() throws Exception {
        logger.info("We are trying to reach route '/nonexisting'");
        logger.info("Checking if the route is not found, as it should be.");
        mockMvc.perform(get("/nonexisting").with(oauth2Login())).andExpect(status().isNotFound());
        logger.info("Test successful.");
    }
}
