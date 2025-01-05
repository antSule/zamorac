package hr.fer.progi.ticketmestar.spotify;


import hr.fer.progi.ticketmestar.spotify.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private final SpotifyService spotifyService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public SpotifyController(SpotifyService spotifyService, OAuth2AuthorizedClientService authorizedClientService) {
        this.spotifyService = spotifyService;
        this.authorizedClientService = authorizedClientService;
    }

    private String getAccessToken() {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("User is not authenticated.");
        }

        String registrationId = authentication.getAuthorizedClientRegistrationId();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                registrationId, authentication.getName());

        if (client == null) {
            throw new IllegalStateException("No authorized client found for the current user.");
        }

        return client.getAccessToken().getTokenValue();
    }

    @GetMapping("/me/albums")
    public ResponseEntity<Map<String, Object>> getUserAlbums() {
        try {
            String accessToken = getAccessToken();
            Map<String, Object> albums = spotifyService.getUserAlbums(accessToken);
            return ResponseEntity.ok(albums);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me/playlists")
    public ResponseEntity<Map<String, Object>> getUserPlaylists() {
        try {
            String accessToken = getAccessToken();
            Map<String, Object> playlists = spotifyService.getUserPlaylists(accessToken);
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> search(@RequestParam("query") String query, @RequestParam("type") String type) {
        try {
            String accessToken = getAccessToken();
            Map<String, Object> searchResults = spotifyService.search(query, type, accessToken); // Corrected to match service method
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me/following")
    public ResponseEntity<Map<String, Object>> getUserFollowing() {
        try {
            String accessToken = getAccessToken();
            Map<String, Object> following = spotifyService.getUserFollowing(accessToken);
            return ResponseEntity.ok(following);
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }
}
