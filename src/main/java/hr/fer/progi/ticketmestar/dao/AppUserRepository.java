package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long>  {
}
