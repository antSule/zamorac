package hr.fer.progi.ticketmestar.rest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.service.AppUserDetailsService;


@RestController
@RequestMapping("/api/user")

public class UserInfoFavoritesController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


 
    @GetMapping(value="/error")
    public ResponseEntity<String> getError(@RequestParam(required = false) String name){
        return ResponseEntity.ok("Error");
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("Evo me doma");

    }

    @GetMapping("/user-info")
    public Map<String, Object> userInfo(@AuthenticationPrincipal OAuth2User oAuth2User, Principal principal) {
        Map<String, Object> userInfo = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof AppUser) {
            AppUser currentUser = (AppUser) authentication.getPrincipal();
            userInfo.put("name", currentUser.getUsername());
            userInfo.put("provider", "JWT");
            userInfo.put("roles", currentUser.getRole());
        }
        else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");

            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;

                if ("spotify".equals(oauth2Token.getAuthorizedClientRegistrationId())) {
                    Optional<AppUser> user = appUserRepository.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.SPOTIFY);
                    if (user.isPresent()) {
                        AppUser appUser = user.get();
                        userInfo.put("name", appUser.getUsername());
                        userInfo.put("provider", "SPOTIFY");
                        userInfo.put("roles", appUser.getRole());
                    } else {
                        userInfo.put("name", "Spotify User");
                        userInfo.put("provider", "SPOTIFY");
                        userInfo.put("roles", "No roles found.");
                    }
                }
                else if ("google".equals(oauth2Token.getAuthorizedClientRegistrationId())) {
                    String name = oauth2User.getAttribute("name");
                    userInfo.put("name", name);
                    userInfo.put("provider", "GOOGLE");
                    Optional<AppUser> user = appUserRepository.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.GOOGLE);
                    if (user.isPresent()) {
                        AppUser appUser = user.get();
                        userInfo.put("roles", appUser.getRole());
                    } else {
                        userInfo.put("roles", "No roles found.");
                    }
                }
            }
        } else {
            throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal().getClass());
        }

        return userInfo;
    }


    @PostMapping("/add-favorite")
    public ResponseEntity<String> addFavoriteArtist(@RequestParam Long artistId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthenticationProvider authenticationProvider = null;
            AppUser currentUser = null;
            if (authentication.getPrincipal() instanceof AppUser) {
                currentUser = (AppUser) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");

                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                    String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

                    if ("google".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.GOOGLE;
                    } else if ("spotify".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.SPOTIFY;
                    } else {
                        throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
                    }
                } else {
                    throw new IllegalStateException("Authentication is not an instance of OAuth2AuthenticationToken");
                }

                currentUser = appUserRepository.findByEmailAndAuthenticationProvider(email, authenticationProvider)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            } else {
                throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal());
            }

            appUserDetailsService.addFavoriteArtist(currentUser, artistId);

            return ResponseEntity.ok("Artist added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to add artist: " + e.getMessage());
        }
    }

    @PostMapping("/remove-favorite")
    public ResponseEntity<String> removeFavoriteArtist(@RequestParam Long artistId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AuthenticationProvider authenticationProvider = null;
            AppUser currentUser = null;

            if (authentication.getPrincipal() instanceof AppUser) {
                currentUser = (AppUser) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");

                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                    String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

                    if ("google".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.GOOGLE;
                    } else if ("spotify".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.SPOTIFY;
                    } else {
                        throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
                    }
                } else {
                    throw new IllegalStateException("Authentication is not an instance of OAuth2AuthenticationToken");
                }

                currentUser = appUserRepository.findByEmailAndAuthenticationProvider(email, authenticationProvider)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            } else {
                throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal());
            }
            appUserDetailsService.removeFavoriteArtist(currentUser, artistId);

            return ResponseEntity.ok("Artist removed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to remove artist: " + e.getMessage());
        }
    }

    @GetMapping("/favourites")
    public ResponseEntity<Set<AppUser>> getFavoriteArtists() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            AppUser currentUser = null;
            AuthenticationProvider authenticationProvider = null;

            if (authentication.getPrincipal() instanceof AppUser) {
                currentUser = (AppUser) authentication.getPrincipal();
            } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
                DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");

                if (authentication instanceof OAuth2AuthenticationToken) {
                    OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                    String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

                    if ("google".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.GOOGLE;
                    } else if ("spotify".equalsIgnoreCase(registrationId)) {
                        authenticationProvider = AuthenticationProvider.SPOTIFY;
                    } else {
                        throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
                    }
                } else {
                    throw new IllegalStateException("Authentication is not an instance of OAuth2AuthenticationToken");
                }
                currentUser = appUserRepository.findByEmailAndAuthenticationProvider(email, authenticationProvider)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found."));
            } else {
                throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal());
            }

            Set<AppUser> favoriteArtists = appUserDetailsService.getFavoriteArtists(currentUser);

            return ResponseEntity.ok(favoriteArtists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}

