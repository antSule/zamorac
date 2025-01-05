package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.Concert;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c FROM Concert c WHERE c.performerId = :performerId AND c.date = :date AND c.time = :time")
    Optional<Concert> findByPerformerIdAndDateAndTime(
            String performerId, LocalDate date, LocalTime time
    );

    List<Concert> findByPerformerId(Long performerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Concert c WHERE c.performerId = :performerId")
    void deleteByPerformerId(Long performerId);
}