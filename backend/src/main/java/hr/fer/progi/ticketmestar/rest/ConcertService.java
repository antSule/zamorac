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

import java.time.LocalDate;
import java.time.LocalTime;
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

        Concert concert = new Concert(
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
        );
        Concert savedConcert = concertRepository.save(concert);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
    }

    public AddConcertDto getConcertById(Long id, Long userId, boolean isAdmin) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found."));

        if(!isAdmin && !concert.getPerformerId().equals(userId)){
            throw new IllegalStateException("You are not allowed to see this concert information.");
        }

        System.out.println("Concert entity: " + concert);

        AddConcertDto concertDto = new AddConcertDto(
                concert.getDate(),
                concert.getTime(),
                concert.getPerformer(),
                concert.getPerformerId(),
                concert.getVenue(),
                concert.getLatitude(),
                concert.getLongitude(),
                concert.getUrl(),
                concert.getCity(),
                concert.getEvent(),
                concert.getImageUrl()
        );

        System.out.println("Returning DTO: " + concertDto);

        return concertDto;
    }



    public AddConcertDto updateConcert(Long id, AddConcertDto concertDto, Long userId, boolean isAdmin) {
        Concert concert = concertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Concert not found"));


        if(!isAdmin && !concert.getPerformerId().equals(userId)){
            throw new IllegalStateException("You are not allowed to edit this concert");
        }
        concert.setDate(concertDto.getDate());
        concert.setTime(concertDto.getTime());
        concert.setPerformer(concertDto.getPerformer());
        concert.setVenue(concertDto.getVenue());
        concert.setLatitude(concertDto.getLatitude());
        concert.setLongitude(concertDto.getLongitude());
        concert.setUrl(concertDto.getUrl());
        concert.setCity(concertDto.getCity());
        concert.setEvent(concertDto.getEvent());
        concert.setImageUrl(concertDto.getImageUrl());

        concert = concertRepository.save(concert);

        return new AddConcertDto(
                concert.getDate(), concert.getTime(), concert.getPerformer(),
                concert.getPerformerId(), concert.getVenue(), concert.getLatitude(),
                concert.getLongitude(), concert.getUrl(), concert.getCity(),
                concert.getEvent(), concert.getImageUrl()
        );
    }


    /*
    public ResponseEntity<?> addNewConcert(AddConcertDto concertDto) {
        List<Concert> existingConcerts = concertRepository.findConcert(
                concertDto.getDate(),
                concertDto.getPerformer());

        if (!existingConcerts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Concert with the same performer, date, and time already exists.");
        }


        Concert savedConcert = concertRepository.save(concert);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
    }
    */

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

