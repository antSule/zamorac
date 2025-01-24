package hr.fer.progi.ticketmestar;

import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.dto.AddConcertDto;
import hr.fer.progi.ticketmestar.rest.ConcertService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@TestPropertySource(locations = "classpath:test.properties")
@TestPropertySource(properties = "spring.sql.init.mode=NEVER")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ComponentTests {
    private static final Logger logger = LoggerFactory.getLogger(ComponentTests.class);

    @MockBean
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertService concertService;


    @Test
    void testAddConcertSuccessfully() {
        AddConcertDto concertDto = new AddConcertDto(
                LocalDate.of(2025, 6, 18),
                LocalTime.of(20,0),
                "Najbolja grupa",
                5L, null, null, null, null, null, null, null
        );
        logger.info("Created AddConcertDto with following parameter: {}", concertDto);
        logger.info("Mock return when checking that this concert doesn't already exist");
        when(concertRepository.findByPerformerIdAndDateAndTime(
                concertDto.getPerformerId().toString(),
                concertDto.getDate(),
                concertDto.getTime())
        ).thenReturn(Optional.empty());

        logger.info("Mock return when creating the concert");
        when(concertRepository.save(any(Concert.class))).thenReturn(new Concert(
                concertDto.getDate(),
                concertDto.getTime(),
                concertDto.getPerformer(),
                concertDto.getPerformerId(),
                concertDto.getVenue(),
                concertDto.getLatitude(),
                concertDto.getLongitude(),
                concertDto.getUrl(),
                concertDto.getCity(),
                concertDto.getEvent(),
                concertDto.getImageUrl()
        ));

        logger.info("Calling the addConcert method");
        ResponseEntity<?> response = concertService.addConcert(concertDto);

        logger.info("Checking that the addConcert method returns HttpStatus.CREATED");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        logger.info("Checking that the addConcert method doesn't return null for the saved concert data");
        assertNotNull(response.getBody());
        logger.info("Checking the concert was saved exactly one time.");
        verify(concertRepository, times(1)).save(any(Concert.class));
        logger.info("Test passed");
    }

    @Test
    void testDeleteConcertSuccessfully() {
        Long concertId = 5L;
        logger.info("Initializing concertId: {}", concertId);

        logger.info("Mock return that the concert with the given id exists");
        when(concertRepository.existsById(concertId)).thenReturn(true);

        logger.info("Calling the deleteConcert method");
        concertService.deleteConcert(concertId);

        logger.info("Checking that the concert is being deleted exactly one time.");
        verify(concertRepository, times(1)).deleteById(concertId);
        logger.info("Test passed");
    }

    @Test
    void testDeleteConcertThrowException() {
        Long concertId = 5L;
        logger.info("Initializing concertId: {}", concertId);

        logger.info("Mock return that the concert with the given id does NOT exist");
        when(concertRepository.existsById(concertId)).thenReturn(false);

        logger.info("Checking that deleteConcert method throws an exception");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> concertService.deleteConcert(concertId));

        logger.info("Checking that the exception has the right message 'Concert not found'");
        assertEquals("Concert not found", exception.getMessage());

        logger.info("Checking that the method didnt try to erase a concert with the given id.");
        verify(concertRepository, never()).deleteById(concertId);
        logger.info("Test passed");

    }

    @Test
    void testDeleteConcertIdMaxValue() {
        Long concertId = Long.MAX_VALUE;
        logger.info("Initializing concertId: {}", concertId);

        logger.info("Mock return that the concert with the given id exists");
        when(concertRepository.existsById(concertId)).thenReturn(true);

        logger.info("Calling the deleteConcert method");
        concertService.deleteConcert(concertId);

        logger.info("Checking that the concert is being deleted exactly one time.");
        verify(concertRepository, times(1)).deleteById(concertId);
        logger.info("Test passed");
    }
}
