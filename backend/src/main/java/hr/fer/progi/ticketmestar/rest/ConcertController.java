package hr.fer.progi.ticketmestar.rest;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.dao.ConcertRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Concert;
import hr.fer.progi.ticketmestar.dto.AddConcertDto;

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

    //@GetMapping("/concerts/artist/{artistId}")
    //public List<Concert> getConcertsByArtist(@PathVariable String artistId) {
    //return concertService.findConcertsByArtist(artistId);
//}

    @Autowired
    private TicketMasterService ticketMasterService;

    @PreAuthorize("hasRole('USER') or hasRole('ARTIST') or hasRole('ADMIN')")
    @GetMapping("/concerts")
    public ResponseEntity<List<Concert>> getConcerts(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) String latitude,
            @RequestParam(required = false) String longitude,
            @RequestParam(required = false) String radius,
            Principal principal) {

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

        if (latitude != null && longitude != null) {
            radius = String.valueOf(20);
        }

        List<Concert> ticketmasterConcerts = ticketMasterService.searchConcerts(date, artist, latitude, longitude, radius);

        LocalDate dateLocal = null;

        if (date != null && !date.isBlank()) {
            try {
                dateLocal = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
        }

        Double latitudeDouble = null;
        Double longitudeDouble = null;
        Double radiusDouble = null;

        if (latitude != null && !latitude.isBlank()) {
            latitudeDouble = Double.parseDouble(latitude);
        }
        if (longitude != null && !longitude.isBlank()) {
            longitudeDouble = Double.parseDouble(longitude);
        }
        if (radius != null && !radius.isBlank()) {
            try {
                radiusDouble = Double.parseDouble(radius);
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Collections.emptyList());
            }
        } else if (latitudeDouble != null && longitudeDouble != null) {
            radiusDouble = 20.0;
        }


        List<Concert> databaseConcerts = concertRepository.findConcert(dateLocal, artist, latitudeDouble, longitudeDouble, radiusDouble);

        List<Concert> concerts = new ArrayList<>();
        concerts.addAll(ticketmasterConcerts);
        concerts.addAll(databaseConcerts);
        return ResponseEntity.ok(concerts);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ARTIST') or hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Concert>> getAllConcerts(Principal principal) {
        List<Concert> concerts = concertService.concertList();
        if (concerts.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(concerts);
    }

    @PreAuthorize("hasRole('ARTIST') or hasRole('ADMIN')")
    @PostMapping(value="/add", consumes="application/json")
    public ResponseEntity<?> addConcert(@RequestBody AddConcertDto concertDto, Principal principal){

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

        concertDto.setPerformerId(userId);

        return concertService.addConcert(concertDto);
    }


    @PreAuthorize("hasRole('ARTIST') or hasRole('ADMIN')")
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
        System.out.println(userId);
        List<Concert> myConcerts = concertService.findConcertsByUserId(userId);
        System.out.println(myConcerts);
        return ResponseEntity.ok(myConcerts);
    }

    @PreAuthorize("hasRole('ARTIST') or hasRole('ADMIN')")
    @PostMapping(value="/delete")
    public ResponseEntity<List<Concert>> removeConcert(@RequestParam Long concertId, Principal principal){
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


    @PreAuthorize("hasRole('SPOTIFY') or hasRole('USER') or hasRole('ARTIST') or hasRole('ADMIN')")
    @GetMapping("/artist")
    public List<Concert> getConcertsByArtists(
            @RequestParam(name = "artist", required = true) String artist){
            return ticketMasterService.searchConcerts(null, artist, null, null, null);
    }


    @PreAuthorize("hasRole('ARTIST') or hasRole('ADMIN')")
    @GetMapping("/concert-info")
    public ResponseEntity<AddConcertDto> getConcertById(@RequestParam Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId;
        AuthenticationProvider authProvider = null;

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

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
        System.out.println(userId);
        AddConcertDto concertDto = concertService.getConcertById(id, userId, isAdmin);
        System.out.println(concertDto);
        return ResponseEntity.ok(concertDto);
    }

    @PreAuthorize("hasRole('ARTIST') or hasRole('ADMIN')")
    @PutMapping("/edit-concert")
    public ResponseEntity<AddConcertDto> updateConcert(@RequestParam Long id, @RequestBody AddConcertDto concertDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId;
        AuthenticationProvider authProvider = null;

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

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
        System.out.println(userId);

        AddConcertDto updatedConcert = concertService.updateConcert(id, concertDto, userId, isAdmin);
        return ResponseEntity.ok(updatedConcert);
    }

}
