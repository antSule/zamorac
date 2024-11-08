package hr.fer.progi.ticketmestar.rest
        ;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RegistrationController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping(value="/registration", consumes="application/json")
    public AppUser createUser(@RequestBody AppUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }

    @GetMapping(value="/getuser")
    public UserDetails getUser(@RequestParam String name){
        return appUserService.loadUserByUsername(name);
    }

    @GetMapping(value="/allusers")
    public List<AppUser> getAllUsers(){
        return appUserRepository.findAll();
    }


}
