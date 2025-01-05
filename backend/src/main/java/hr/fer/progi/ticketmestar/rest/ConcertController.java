package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.dto.AddConcertDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/concerts")

public class ConcertController {

    ConcertService concertService;
    ConcertRepository concertRepository;
    AppUserRepository userRepository;

    public ConcertController(ConcertService concertService, ConcertRepository concertRepository, AppUserRepository userRepository) {
        this.concertService = concertService;
        this.concertRepository = concertRepository;
        this.userRepository = userRepository;
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
    @PreAuthorize("hasRole('USER') or hasRole('ARTIST')")
    @GetMapping("/all")
    public ResponseEntity<List<Concert>> getAllConcerts(Principal principal) {
        List<Concert> concerts = concertService.concertList();
        if (concerts.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(concerts);
    }

    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping(value="/add", consumes="application/json")
    public ResponseEntity<?> addConcert(@RequestBody AddConcertDto concertDto, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser currentUser = (AppUser) authentication.getPrincipal();
        concertDto.setPerformer(currentUser.getUsername());
        concertDto.setPerformerId(currentUser.getId());
        return concertService.addConcert(concertDto);
    }


    @PreAuthorize("hasRole('ARTIST')")
    @GetMapping(value = "/me")
    public ResponseEntity<List<Concert>> getMyConcerts(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId;
        AuthenticationProvider authProvider = null;

        if (authentication.getPrincipal() instanceof AppUser) {
            AppUser currentUser = (AppUser) authentication.getPrincipal();
            userId = currentUser.getId();
        } else if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            System.out.println(email);

            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

                if ("google".equalsIgnoreCase(registrationId)) {
                    authProvider = AuthenticationProvider.GOOGLE;
                } else if ("spotify".equalsIgnoreCase(registrationId)) {
                    authProvider = AuthenticationProvider.SPOTIFY;
                } else {
                    throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
                }
            } else {
                throw new IllegalStateException("Authentication is not an instance of OAuth2AuthenticationToken");
            }

            AppUser currentUser = userRepository.findByEmailAndAuthenticationProvider(email, authProvider)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            userId = currentUser.getId();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal().getClass());
        }

        List<Concert> myConcerts = concertService.findConcertsByUserId(userId);
        return ResponseEntity.ok(myConcerts);
    }

    @PreAuthorize("hasRole('ARTIST')")
    @PostMapping(value="/delete")
    public ResponseEntity<List<Concert>> removeConcert(Long concertId, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId;
        AuthenticationProvider authenticationProvider = null;
        if(authentication.getPrincipal() instanceof AppUser){
            AppUser currentUser = (AppUser) authentication.getPrincipal();
            userId = currentUser.getId();
        } else if (authentication.getPrincipal() instanceof DefaultOAuth2User){
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");
            System.out.println(email);
            if(authentication instanceof OAuth2AuthenticationToken){
                OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
                String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

                if("google".equalsIgnoreCase(registrationId)){
                    authenticationProvider = AuthenticationProvider.GOOGLE;
                } else if ("spotify".equalsIgnoreCase(registrationId)){
                    authenticationProvider = AuthenticationProvider.SPOTIFY;
                } else {
                    throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
                }
            } else {
                throw new IllegalStateException("Authentication is not an instance of OAuth2AuthenticationToken");
            }

            AppUser currentUser= userRepository.findByEmailAndAuthenticationProvider(email, authenticationProvider).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            userId = currentUser.getId();
        } else {
            throw new IllegalStateException("Unexpected principal type: " + authentication.getPrincipal());
        }

        Concert concert = concertService.findConcertById(concertId).orElseThrow(() -> new IllegalArgumentException("Concert not found."));

        if(!concert.getPerformerId().equals(userId)){
            throw new SecurityException("You are not authorized to delete this concert.");
        }

        concertService.deleteConcert(concertId);

        return ResponseEntity.ok(concertService.concertList());
    }

}
