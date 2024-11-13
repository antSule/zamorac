package hr.fer.progi.ticketmestar.service.impl;

import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertServiceJpa implements ConcertService {

    @Autowired
    private ConcertRepository concertRepo;

    @Override
    public List<Concert> listAll() {
        return concertRepo.findAll();
    }

}
