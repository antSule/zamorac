package hr.fer.progi.ticketmestar.service.impl;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserServiceJpa implements AppUserDetailsService {

    @Autowired
    private AppUserRepository appUserRepo;

    @Override
    public List<AppUser> listAll() {
        List<AppUser> users = new ArrayList<>();
        appUserRepo.findAll().forEach(users::add);
        return users;
    }

    @Override
    public AppUser createAppUser(AppUser appUser) {
        Assert.notNull(appUser, "AppUser object must be given");
        Assert.isNull(appUser.getId(), "AppUser ID must be null, not" + appUser.getId());
        return appUserRepo.save(appUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return appUserRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found for this username: " + username));
    }

    @Override
    public UserDetails loadUserByEmailAndAuthProvider(String email) {
        return appUserRepo.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.JWT)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsernameAndAuthProvider(String username) {
        return appUserRepo.findByUsernameAndAuthenticationProvider(username, AuthenticationProvider.JWT)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
