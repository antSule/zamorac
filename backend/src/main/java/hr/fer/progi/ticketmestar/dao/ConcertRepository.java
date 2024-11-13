package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE c.performer = :performer AND c.date = :date AND c.time = :time")
    Optional<Concert> findByPerformerAndDateAndTime(
            String performer, LocalDate date, LocalTime time
    );
}