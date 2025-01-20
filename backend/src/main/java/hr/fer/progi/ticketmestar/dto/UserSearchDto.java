package hr.fer.progi.ticketmestar.dto;

import java.util.List;

public class UserSearchDto {

    private String username;
    private List<String> roles;
    private String provider;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
