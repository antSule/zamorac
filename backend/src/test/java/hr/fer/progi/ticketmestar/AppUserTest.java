package hr.fer.progi.ticketmestar;

import hr.fer.progi.ticketmestar.service.impl.AppUserServiceJpa;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AppUserTest {
    private static final Logger logger = LoggerFactory.getLogger(AppUserTest.class);

    @Autowired
    private AppUserServiceJpa appUserServiceJpa;

    @Test
    void appUserIsNullTest() {
        logger.info("Calling createAppUser method and expecting an IllegalArgumentException");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            appUserServiceJpa.createAppUser(null);
        });
        logger.info("Expected exception thrown, checking if the message is 'AppUser object must be given'");
        assertEquals("AppUser object must be given", exception.getMessage());
        logger.info("Test successful.");
    }
}
