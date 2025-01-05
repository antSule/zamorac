package hr.fer.progi.ticketmestar.authControllers;


import com.nimbusds.oauth2.sdk.Response;
import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Role;
import hr.fer.progi.ticketmestar.dto.LoginUserDto;
import hr.fer.progi.ticketmestar.dto.RegisterUserDto;
import hr.fer.progi.ticketmestar.dto.VerifyUserDto;
import hr.fer.progi.ticketmestar.responses.LoginResponse;
import hr.fer.progi.ticketmestar.responses.SignupResponse;
import hr.fer.progi.ticketmestar.service.impl.AuthenticationService;
import hr.fer.progi.ticketmestar.service.impl.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final AppUserRepository userRepository;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, AppUserRepository userRepository) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }


    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> register(@RequestBody RegisterUserDto registerUserDto){
        SignupResponse signupResponse = authenticationService.signUp(registerUserDto);
        return ResponseEntity.ok(signupResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
        AppUser authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto){
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account verified successfully");
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email){
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent.");
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('NULL_USER')")
    @GetMapping("/setrole")
    public ResponseEntity<?> chooseOauth2Roles(@RequestParam Set<Role> roles, Authentication authentication) {
        if (roles == null || roles.isEmpty()) {
            return ResponseEntity.badRequest().body("Roles cannot be empty.");
        }

        if (!(authentication instanceof OAuth2AuthenticationToken oauth2Token)) {
            return ResponseEntity.badRequest().body("Invalid authentication type. Expected OAuth2.");
        }

        DefaultOAuth2User oauth2User = (DefaultOAuth2User) oauth2Token.getPrincipal();
        String email = oauth2User.getAttribute("email");

        String registrationId = oauth2Token.getAuthorizedClientRegistrationId();

        if (registrationId == null || registrationId.isEmpty()) {
            return ResponseEntity.badRequest().body("Authentication provider not recognized.");
        }

        AuthenticationProvider authProvider = determineAuthProvider(registrationId);

        AppUser currentUser = userRepository.findByEmailAndAuthenticationProvider(email, authProvider)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        currentUser.setRole(roles);
        userRepository.save(currentUser);

        updateOAuth2SecurityContext(oauth2Token, currentUser);

        return ResponseEntity.ok("Roles updated successfully.");
    }


    private AuthenticationProvider determineAuthProvider(String registrationId) {
        if ("google".equalsIgnoreCase(registrationId)) {
            return AuthenticationProvider.GOOGLE;
        } else if ("spotify".equalsIgnoreCase(registrationId)) {
            return AuthenticationProvider.SPOTIFY;
        } else {
            throw new IllegalStateException("Unsupported authentication provider: " + registrationId);
        }
    }

    private void updateOAuth2SecurityContext(OAuth2AuthenticationToken oauth2Token, AppUser currentUser) {
        List<SimpleGrantedAuthority> updatedAuthorities = currentUser.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();

        OAuth2AuthenticationToken updatedAuth = new OAuth2AuthenticationToken(
                oauth2Token.getPrincipal(),
                updatedAuthorities,
                oauth2Token.getAuthorizedClientRegistrationId()
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
    }


}
