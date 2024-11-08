package hr.fer.progi.ticketmestar.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private boolean isArtist;


    //private Boolean locked = false;
    //private Boolean enabled = false;


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isArtist=" + isArtist +
                '}';
    }
}
