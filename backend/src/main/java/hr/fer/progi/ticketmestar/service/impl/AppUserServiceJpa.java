package hr.fer.progi.ticketmestar.service.impl;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Role;
import hr.fer.progi.ticketmestar.service.AppUserDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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


    @Transactional
    @Override
    public void addFavoriteArtist(AppUser currentUser, Long artistId) {
        System.out.println(artistId);
        AppUser artist = appUserRepo.findById(artistId)
                .orElseThrow(() -> new UsernameNotFoundException("Artist not found."));

        if (!artist.getRole().contains(Role.ARTIST)) {
            throw new IllegalArgumentException("The user is not an artist and cannot be added to favorites.");
        }

        if (currentUser.getFavoriteArtists().contains(artist)) {
            throw new IllegalArgumentException("Artist is already in your favorites.");
        }

        currentUser.getFavoriteArtists().add(artist);
        appUserRepo.save(currentUser);
    }

    @Transactional
    @Override
    public void removeFavoriteArtist(AppUser currentUser, Long artistId) {
        AppUser artist = appUserRepo.findById(artistId)
                .orElseThrow(() -> new UsernameNotFoundException("Artist not found."));

        if (!currentUser.getFavoriteArtists().contains(artist)) {
            throw new IllegalArgumentException("Artist is not in your favorites.");
        }

        currentUser.getFavoriteArtists().remove(artist);
        appUserRepo.save(currentUser);
    }

    @Transactional
    @Override
    public Set<AppUser> getFavoriteArtists(AppUser currentUser) {
        try {
            if (currentUser == null) {
                throw new IllegalStateException("User not authenticated");
            }
            Set<AppUser> favoriteArtists = currentUser.getFavoriteArtists();
            if (favoriteArtists == null) {
                throw new IllegalStateException("No favorite artists found");
            }

            return favoriteArtists;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching favorite artists: " + e.getMessage());
        }
    }
}
