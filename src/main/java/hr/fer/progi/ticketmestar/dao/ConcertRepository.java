package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

}
