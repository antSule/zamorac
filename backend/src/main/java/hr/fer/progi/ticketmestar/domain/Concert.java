package hr.fer.progi.ticketmestar.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String performer;
    private Long performerId;
    private String venue;
    private Double latitude;
    private Double longitude;
    private String url;
    private String city;
    private String event;
    private String imageUrl;

    public Concert(){}

    public Concert(LocalDate date, LocalTime time, String performer, Long performerId) {
        this.date = date;
        this.time = time;
        this.performer = performer;
        this.performerId = performerId;
    }

    public Concert(LocalDate date, LocalTime time, String performer, Long performerId, String venue, Double latitude, Double longitude, String url, String city, String event, String imageUrl) {
        this.date = date;
        this.time = time;
        this.performer = performer;
        this.performerId = performerId;
        this.venue = venue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.city = city;
        this.event = event;
        this.imageUrl = imageUrl;
    }

    public Concert(LocalDate date, LocalTime time, String performer, String venue, Double latitude, Double longitude, String url, String city, String event, String imageUrl) {
        this.date = date;
        this.time = time;
        this.performer = performer;
        this.venue = venue;
        this.latitude = latitude;
        this.longitude = longitude;
        this.url = url;
        this.city = city;
        this.event = event;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) { this.performer = performer; }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVenue() { return venue; }

    public void setVenue(String venue) {this.venue = venue;}

    public Double getLatitude() { return latitude; }

    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }

    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getEvent() { return event; }

    public void setEvent(String event) { this.event = event; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    @Override
    public String toString() {
        return "Concert{" +
                ", date=" + date +
                ", time=" + time +
                ", performer='" + performer + '\'' +
                ", venue='" + venue + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", url='" + url + '\'' +
                ", city=" + city +
                ", event=" + event +
                '}';
    }
}
