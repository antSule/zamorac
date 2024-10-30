package hr.fer.progi.ticketmestar.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public class AppUser {

    @Id
    @GeneratedValue
    private Long id;

    @Email(message = "Email should be in a correct form")
    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private boolean isArtist;


    //private Boolean locked = false;
    //private Boolean enabled = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isArtist=" + isArtist +
                '}';
    }
}
