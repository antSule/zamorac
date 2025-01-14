package hr.fer.progi.ticketmestar.rest;

import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class RegistrationController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;




    @GetMapping(value="/error")
    public ResponseEntity<String> getError(@RequestParam(required = false) String name){
        return ResponseEntity.ok("Error");
    }

    @GetMapping(value="/getuser")
    public UserDetails getUser(@RequestParam(required = false) String name){
        if (name == null) {
            throw new IllegalArgumentException("Name parameter is required");
        }
        return appUserService.loadUserByUsername(name);
    }

    @GetMapping(value="/allusers")
    public List<AppUser> getAllUsers(){
        return appUserRepository.findAll();
    }

    @GetMapping("/home")
    public ResponseEntity<String> home() {

        return ResponseEntity.ok("Evo me doma");

    }

    @GetMapping("/artist")
    public ResponseEntity<String> artist() {

        return ResponseEntity.ok("Evo me artist");

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


}