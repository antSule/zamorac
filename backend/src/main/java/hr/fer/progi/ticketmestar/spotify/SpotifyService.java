package hr.fer.progi.ticketmestar.spotify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class SpotifyService {

    @Value("${spring.security.oauth2.client.provider.spotify.authorization-uri}")
    private String authorizeLink;

    @Value("${spring.security.oauth2.client.registration.spotify.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.spotify.client-secret}")
    private String secretId;

    @Value("${spring.security.oauth2.client.registration.spotify.redirect-uri}")
    private String redirect_Uri;

    @Value("${spotify.scope}")
    private String scope;

    @Value("${spring.security.oauth2.client.provider.spotify.token-uri}")
    private String tokenUrl;

    @Value("${spotify.searchUrl}")
    private String searchUrl;

    @Value("${spotify.accessToken}")
    private String accessToken;


    public String getAccessToken(String code) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = this.createTokenRequestBody(code);

            final String body = restTemplate.postForEntity(this.buildTokenUrl(), request, String.class).getBody();

            return body;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String buildTokenUrl(){
        return tokenUrl;
    }

    public HttpEntity<String> createTokenRequestBody(String code) {
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secretId).getBytes()));
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=authorization_code&code=" + code + "&redirect_uri=" + redirect_Uri;
        System.out.println("Request Body: " + body);

        return new HttpEntity<>(body, header);
    }


    public String getSpotifyUserId(String accessToken) {
        return getSpotifyUserInfo(accessToken).get("id");
    }

    public String getSpotifyUserName(String accessToken) {
        return getSpotifyUserInfo(accessToken).get("display_name");
    }

    public String getSpotifyUserEmail(String accessToken) {
        return getSpotifyUserInfo(accessToken).get("email");
    }

    private Map<String, String> getSpotifyUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return (Map<String, String>) response.getBody();
    }

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiBaseUrl = "https://api.spotify.com/v1";


    public Map<String, Object> getUserAlbums(String accessToken) {
        String url = apiBaseUrl + "/me/albums";
        return fetchSpotifyData(url, accessToken);
    }

    public Map<String, Object> getUserPlaylists(String accessToken) {
        String url = apiBaseUrl + "/me/playlists";
        return fetchSpotifyData(url, accessToken);
    }

    public Map<String, Object> search(String query, String type, String accessToken) {
        String url = apiBaseUrl + "/search?q=" + query + "&type=" + type;
        return fetchSpotifyData(url, accessToken);
    }

    public Map<String, Object> getUserFollowing(String accessToken) {
        String url = apiBaseUrl + "/me/following?type=artist";
        return fetchSpotifyData(url, accessToken);
    }

    private Map<String, Object> fetchSpotifyData(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        return response.getBody();
    }
}
