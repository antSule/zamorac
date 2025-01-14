package hr.fer.progi.ticketmestar.rest;


import hr.fer.progi.ticketmestar.dao.AppUserRepository;
import hr.fer.progi.ticketmestar.domain.AppUser;
import hr.fer.progi.ticketmestar.domain.AuthenticationProvider;
import hr.fer.progi.ticketmestar.domain.Role;
import hr.fer.progi.ticketmestar.responses.LoginResponse;
import hr.fer.progi.ticketmestar.spotify.SpotifyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AppUserRepository userRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final SpotifyService spotifyService;

    public CustomOAuth2SuccessHandler(AppUserRepository userRepository,
                                      OAuth2AuthorizedClientService authorizedClientService,
                                      SpotifyService spotifyService) {
        this.userRepository = userRepository;
        this.authorizedClientService = authorizedClientService;
        this.spotifyService = spotifyService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauth2AuthenticationToken.getPrincipal();
        String registrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        if ("google".equals(registrationId)) {
            System.out.println("Google login");
            handleGoogleLogin(oAuth2User, response);
        } else if ("spotify".equals(registrationId)) {
            System.out.println("Spotify login");
            handleSpotifyLogin(oauth2AuthenticationToken, response);
        } else {
            response.sendRedirect("http://https://ticketmestarfrontend-c9vl.onrender.com/home");
        }
    }

    private void handleGoogleLogin(OAuth2User oAuth2User, HttpServletResponse response) throws IOException {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Optional<AppUser> user = userRepository.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.GOOGLE);
        AppUser appUser;

        if (user.isEmpty()) {
            appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setUsername(name);
            appUser.setRole(Set.of(Role.NULL_USER));
            appUser.setEnabled(true);
            appUser.setPassword("dummy_password");
            appUser.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
            userRepository.save(appUser);
        } else {
            appUser = user.get();
        }

        updateSecurityContext(appUser, oAuth2User);

        if (appUser.getRole().contains(Role.NULL_USER)) {
            response.sendRedirect("http://https://ticketmestarfrontend-c9vl.onrender.com/select-role");
        } else {
            response.sendRedirect("http://https://ticketmestarfrontend-c9vl.onrender.com/home");
        }
    }

    private void handleSpotifyLogin(OAuth2AuthenticationToken authenticationToken, HttpServletResponse response) throws IOException {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        if (client == null) {
            response.sendRedirect("/error");
            return;
        }

        OAuth2AccessToken accessToken = client.getAccessToken();
        String tokenValue = accessToken.getTokenValue();

        String spotifyUserId = spotifyService.getSpotifyUserId(tokenValue);
        String name = spotifyService.getSpotifyUserName(tokenValue);
        String email = spotifyService.getSpotifyUserEmail(tokenValue);

        Optional<AppUser> user = userRepository.findByEmailAndAuthenticationProvider(email, AuthenticationProvider.SPOTIFY);
        AppUser appUser;

        if (user.isEmpty()) {
            appUser = new AppUser();
            appUser.setEmail(email);
            appUser.setUsername(name);
            appUser.setRole(Set.of(Role.NULL_USER, Role.USER));
            appUser.setEnabled(true);
            appUser.setPassword("dummy_password");
            appUser.setSpotifyUserId(spotifyUserId);
            appUser.setAuthenticationProvider(AuthenticationProvider.SPOTIFY);
            userRepository.save(appUser);
        } else {
            appUser = user.get();
            appUser.setSpotifyUserId(spotifyUserId);
            userRepository.save(appUser);
        }

        List<GrantedAuthority> mergedAuthorities = new ArrayList<>(authenticationToken.getAuthorities());
        mergedAuthorities.addAll(appUser.getAuthorities());

        OAuth2AuthenticationToken updatedAuth = new OAuth2AuthenticationToken(
                authenticationToken.getPrincipal(),
                mergedAuthorities,
                authenticationToken.getAuthorizedClientRegistrationId()
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuth);

        response.sendRedirect("http://https://ticketmestarfrontend-c9vl.onrender.com/home");
    }

    private void updateSecurityContext(AppUser appUser, OAuth2User oAuth2User) {
        OAuth2AuthenticationToken updatedAuth = new OAuth2AuthenticationToken(
                oAuth2User,
                appUser.getAuthorities(),
                "google"
        );

        SecurityContextHolder.getContext().setAuthentication(updatedAuth);
    }
}
