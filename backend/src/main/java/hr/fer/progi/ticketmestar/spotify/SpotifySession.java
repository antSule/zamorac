package hr.fer.progi.ticketmestar.spotify;

import jakarta.servlet.http.Cookie;

public class SpotifySession {

    public static Cookie getCookie(final SpotifyTokenResponse tokenResponse) {
        Cookie cookie = new Cookie("access_token", tokenResponse.getAccessToken());
        //cookie.setAttribute("created_at", LocalDateTime.now());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(tokenResponse.getExpiresIn());
        cookie.setPath("/");

        return cookie;
    }
}