package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concerts")
public class ConcertController {

    ConcertService concertService;
    ConcertRepository concertRepository;

    public ConcertController(ConcertService concertService, ConcertRepository concertRepository) {
        this.concertService = concertService;
        this.concertRepository = concertRepository;
    }

    @GetMapping(value="/all")
    public List<Concert> listConcerts() {
        return concertService.concertList();
    }

    @PostMapping(value="/add", consumes="application/json")
    public ResponseEntity<?> addConcert(@RequestBody Concert concert){
        return concertService.addConcert(concert);
    }



/*

    @PreAuthorize("hasAnyRole('USER', 'ARTIST')")
    @GetMapping("/all")
    public String listConcerts() {
        return "user and artist";
    }


    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping("/add")
    public String addConcert(@RequestBody Concert concert) {
        return "accessible only by artist";
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user-concerts")
    public String viewUserConcerts() {
        return "accessible only by user";
    }

 */
}
