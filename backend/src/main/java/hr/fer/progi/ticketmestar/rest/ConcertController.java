package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.Concert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    //DODANO
    @Autowired
    private TicketMasterService ticketMasterService;

    @GetMapping("/concerts")
    public ResponseEntity<List<Concert>> getConcerts(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude,
            @RequestParam(required = false) String radius) {

        List<Concert> concerts = ticketMasterService.searchConcerts(date, artist, latitude, longitude, radius);

        return ResponseEntity.ok(concerts);
    }

    //DODANO

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
