package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appusers")
public class AppUserController {

    @Autowired
    private AppUserDetailsService appUserService;

    @GetMapping("")
    public List<AppUser> listAppUsers() {
        return appUserService.listAll();
    }

    @PostMapping("")
    @Secured("ROLE_ADMIN")
    public AppUser createAppUser(@RequestBody AppUser appUser) {
        return appUserService.createAppUser(appUser);
    }
}
