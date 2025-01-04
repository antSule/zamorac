package hr.fer.progi.ticketmestar.dto;


import java.time.LocalDate;
import java.time.LocalTime;

public class AddConcertDto {

    private LocalDate date;

    private LocalTime time;

    private String performer;

    private Long performerId;

    public AddConcertDto(LocalDate date, LocalTime time){
        this.date = date;
        this.time = time;
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
}
