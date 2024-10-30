package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    @GetMapping("")
    public List<Concert> listConcerts() {
        return concertService.listAll();
    }
}
