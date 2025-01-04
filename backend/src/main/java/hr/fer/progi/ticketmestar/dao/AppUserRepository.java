package hr.fer.progi.ticketmestar.dao;

import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>  {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByVerificationCode(String verificationCode);

    Optional<AppUser> findByEmailAndAuthenticationProvider(String email, AuthenticationProvider authenticationProvider);
    Optional<AppUser> findByUsernameAndAuthenticationProvider(String username, AuthenticationProvider authenticationProvider);

}