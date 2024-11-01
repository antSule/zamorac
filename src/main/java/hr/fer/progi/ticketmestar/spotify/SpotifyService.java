package hr.fer.progi.ticketmestar.spotify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import java.util.Base64;

@Service
public class SpotifyService {
    @Value("${spotify.authorize}")
    private String authorizeLink;

    @Value("${spotify.clientId}")
    private String clientId;

    @Value("${spotify.secret}")
    private String secretId;

    @Value("${spotify.redirect_uri}")
    private String redirect_Uri;

    @Value("${spotify.scope}")
    private String scope;

    @Value("${spotify.tokenUrl}")
    private String tokenUrl;

    @Value("${spotify.searchUrl}")
    private String searchUrl;

    @Value("${spotify.accessToken}")
    private String accessToken;

    public String buildAuthorizationUrl() {
        return authorizeLink +
                "?client_id=" + clientId +
                "&response_type=code" + "&redirect_uri=" +
                redirect_Uri +
                "&show_dialog=true" +
                "&scope=" + scope;
    }

    public String buildTokenUrl(){
        return tokenUrl;
    }

    public String buildSearchUrl(String query, String type){
        return searchUrl + "/search?q=" + query + "&type=" + type;
    }

    public String buildUserAlbumsUrl(){
        return searchUrl + "/me/albums";
    }

    public String buildUserFollowingUrl(){
        return searchUrl + "/me/following?type=artist";
    }

    public HttpHeaders createAuthHeader(){
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Bearer " + accessToken);
        return header;
    }

    public HttpEntity<String> createTokenRequestBody(String code){
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + secretId).getBytes()));
        header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + code + "&redirect_uri=" + redirect_Uri + "&grant_type=authorization_code";
        return new HttpEntity<>(body,header);
    }
}
