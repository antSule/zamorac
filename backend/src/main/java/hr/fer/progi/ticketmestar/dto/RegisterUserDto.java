package hr.fer.progi.ticketmestar.dto;


import hr.fer.progi.ticketmestar.domain.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RegisterUserDto {

    private String email;
    private String password;
    private String username;
    private List<String> role;

    public RegisterUserDto(String email, String password, String username, List<String> role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public Set<Role> getRolesAsEnum() {
        return role.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
