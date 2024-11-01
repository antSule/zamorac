package hr.fer.progi.ticketmestar.spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
public class SpotifyController {

    @Autowired
    private SpotifyService spotifyService;

    @GetMapping("/")
    public RedirectView authorizeSpotify() {
        return new RedirectView(spotifyService.buildAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<String> authorized(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(
                spotifyService.buildTokenUrl(),
                spotifyService.createTokenRequestBody(code),
                String.class
        );
    }

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam("query") String query, @RequestParam("type") String type) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildSearchUrl(query, type),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader()),
                String.class
        );
    }

    @GetMapping("/albums")
    public ResponseEntity<String> getUserAlbums() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildUserAlbumsUrl(),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader()),
                String.class
        );
    }

    @GetMapping("/following")
    public ResponseEntity<String> getUserFollowing() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildUserFollowingUrl(),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader()),
                String.class
        );
    }
}
