package hr.fer.progi.ticketmestar.rest;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.dto.AddConcertDto;
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
import java.util.Optional;

@Service
public class ConcertService{

    ConcertRepository concertRepository;

    @Autowired
    public ConcertService(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    public List<Concert> concertList() {
        return concertRepository.findAll();
    }

    public ResponseEntity<?> addConcert(AddConcertDto concertDto) {
        if (concertRepository.findByPerformerIdAndDateAndTime(
                concertDto.getPerformerId().toString(),
                concertDto.getDate(),
                concertDto.getTime()).isPresent()) {

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Concert with the same performer, date, and time already exists.");
        }

        Concert concert = new Concert(concertDto.getDate(), concertDto.getTime(),concertDto.getPerformer(), concertDto.getPerformerId());
        Concert savedConcert = concertRepository.save(concert);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
    }

    public List<Concert> findConcertsByUserId(Long userId){
        return concertRepository.findByPerformerId(userId);
    }

    public Optional<Concert> findConcertById(Long id){
        return concertRepository.findById(id);
    }

    public void deleteConcert(Long id){
        if (!concertRepository.existsById(id)) {
            throw new IllegalArgumentException("Concert not found");
        }
        concertRepository.deleteById(id);
    }
}

