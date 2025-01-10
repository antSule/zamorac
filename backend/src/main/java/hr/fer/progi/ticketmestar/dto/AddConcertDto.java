package hr.fer.progi.ticketmestar.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public class AddConcertDto {

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

    public AddConcertDto(LocalDate date, LocalTime time){
        this.date = date;
        this.time = time;
    }

    public AddConcertDto(LocalDate date, LocalTime time, String performer, String venue, Double latitude,
                         Double longitude, String url, String city, String event, String imageUrl) {
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public String getVenue() { return venue; }

    public void setVenue(String venue) { this.venue = venue; }

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
}
