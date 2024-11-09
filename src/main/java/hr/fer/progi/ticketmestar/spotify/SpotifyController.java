package hr.fer.progi.ticketmestar.spotify;

import hr.fer.progi.ticketmestar.rest.AppUserController;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@RestController
@RequestMapping
public class SpotifyController {

    private final SpotifyService spotifyService;

    private final AppUserController appUserController;

    @Autowired
    public SpotifyController(final SpotifyService spotifyService,
                             final AppUserController appUserController) {
        this.spotifyService = spotifyService;
        this.appUserController = appUserController;
    }

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") String state,
                         HttpServletResponse response) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        final String body = restTemplate.postForEntity(
                        spotifyService.buildTokenUrl(),
                        spotifyService.createTokenRequestBody(code),
                        String.class)
                .getBody();

        final Cookie cookie = spotifyService.authorize(body);

        response.addCookie(cookie);
        response.sendRedirect("/home");

    }
    @GetMapping("/error")
    public ResponseEntity<Void> error(@RequestBody ErrorBody errorBody) {
        System.out.println(errorBody);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }



    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam("query") String query, @RequestParam("type") String type,
                                         final HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildSearchUrl(query, type),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader(getAccessToken(request))),
                String.class
        );
    }

    @GetMapping("/albums")
    public ResponseEntity<String> getUserAlbums(final HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildUserAlbumsUrl(),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader(getAccessToken(request))),
                String.class
        );
    }

    @GetMapping("/following")
    public ResponseEntity<String> getUserFollowing(final HttpServletRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                spotifyService.buildUserFollowingUrl(),
                HttpMethod.GET,
                new HttpEntity<>(spotifyService.createAuthHeader(getAccessToken(request))),
                String.class
        );
    }

    private String getAccessToken(final HttpServletRequest request) {
        String token = null;

        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                token = cookie.getValue();
            }
        }
        return token;
    }


}
