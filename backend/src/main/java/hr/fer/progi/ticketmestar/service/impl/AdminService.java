package hr.fer.progi.ticketmestar.service.impl;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.Role;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AdminService {

    private final AppUserRepository appUserRepository;
    private final ConcertRepository concertRepository;

    public AdminService(AppUserRepository appUserRepository, ConcertRepository concertRepository){
        this.appUserRepository = appUserRepository;
        this.concertRepository = concertRepository;
    }

    public List<AppUser> allUsers(){
        return appUserRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId){
        if(!appUserRepository.existsById(userId)){
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }
        concertRepository.deleteByPerformerId(userId);
        appUserRepository.deleteById(userId);
    }

    @Transactional
    public AppUser changeUserRoles(Long id, Set<Role> roles){
        AppUser user = appUserRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with ID " + id + " does not exist."));
        user.setRole(roles);
        return appUserRepository.save(user);
    }

    @Transactional
    public void deleteConcert(Long id){
        if(!concertRepository.existsById(id)){
            throw new IllegalArgumentException("Concert with ID " + id + " does not exist.");
        }
        concertRepository.deleteById(id);
    }
}
