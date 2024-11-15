package hr.fer.progi.ticketmestar.rest;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
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


    @PostMapping(value="/registration", consumes="application/json")
    public ResponseEntity<?> createUser(@RequestBody AppUser user){

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(user.getEmail());

        if (!matcher.matches()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email format");
        }

        if(appUserRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username is already taken");
        }

        if(appUserRepository.findByEmail(user.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already taken");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = appUserRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping(value="/error")
    public ResponseEntity<String> getError(@RequestParam String name){
        return ResponseEntity.ok("Error");
    }

    @GetMapping(value="/getuser")
    public UserDetails getUser(@RequestParam String name){
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
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal){
        return principal.getAttributes();
    }



    @PostMapping("/pick-role")
    public void pickRole(@RequestBody String role, Authentication authentication, HttpServletResponse response) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Optional<AppUser> userOptional = appUserService.findByEmail(email);

        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            user.setRole(role.equalsIgnoreCase("ARTIST") ? "ARTIST" : "USER");
            appUserService.saveUser(user);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
            OAuth2User updatedOAuth2User = new DefaultOAuth2User(
                    authorities,
                    oAuth2User.getAttributes(),
                    "email"
            );

            OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(updatedOAuth2User, authorities, "google");
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            response.sendRedirect("/home");
        }
        else {
            response.sendRedirect("/error");
        }
    }


}
