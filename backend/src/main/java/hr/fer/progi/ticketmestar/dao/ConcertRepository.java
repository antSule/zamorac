package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.Concert;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT c FROM Concert c WHERE " +
            "(COALESCE(:date, c.date) = c.date) AND " +
            "(COALESCE(:performer, c.performer) = c.performer) AND " +
            "(:latitude IS NULL OR :longitude IS NULL OR " +
            "(6371 * ACOS(COS((PI() / 180) * (:latitude)) * COS((PI() / 180) * (c.latitude)) * " +
            "COS((PI() / 180) * (c.longitude - (PI() / 180) * (:longitude))) + " +
            "SIN((PI() / 180) * (:latitude)) * SIN((PI() / 180) * (c.latitude)))) <= :radius)")
    List<Concert> findConcert(
            @Param("date") LocalDate date,
            @Param("performer") String performer,
            @Param("latitude") Double latitude,
            @Param("longitude") Double longitude,
            @Param("radius") Double radius
    );


    List<Concert> findByPerformerId(Long performerId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Concert c WHERE c.performerId = :performerId")
    void deleteByPerformerId(Long performerId);
}