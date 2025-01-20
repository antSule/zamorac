package hr.fer.progi.ticketmestar.service;

import hr.fer.progi.ticketmestar.domain.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface AppUserDetailsService {
    List<AppUser> listAll();


    /**
     * Creates new appUser in the system
     * @param appUser object to create, with ID set to null
     * @return created appUser object in the system with ID set
     * @throws IllegalArgumentException if given appUser is null or its ID is not null
     */
    AppUser createAppUser(AppUser appUser);

    UserDetails loadUserByUsername(String username);

    UserDetails loadUserByEmailAndAuthProvider(String email);
    UserDetails loadUserByUsernameAndAuthProvider(String username);

    void addFavoriteArtist(AppUser currentUser, Long artistId);
    void removeFavoriteArtist(AppUser currentUser, Long artistId);
    Set<AppUser> getFavoriteArtists(AppUser currentUser);
}
