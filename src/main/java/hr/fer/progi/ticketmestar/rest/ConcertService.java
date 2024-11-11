package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.Concert;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertService  implements  UserDetailsService{

    ConcertRepository concertRepository;

    @Autowired
    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> concertList() {
        return concertRepository.findAll();
    }

    public ResponseEntity<?> addConcert(Concert concert) {
        if (concertRepository.findByPerformerAndDateAndTime(
                concert.getPerformer(),
                concert.getDate(),
                concert.getTime()).isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Concert with the same performer, date, and time already exists.");
        }

        Concert savedConcert = concertRepository.save(concert);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}

