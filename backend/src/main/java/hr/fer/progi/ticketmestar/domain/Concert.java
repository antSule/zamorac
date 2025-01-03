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

    public Concert(){}

    public Concert(Long id, LocalDate date, LocalTime time, String performer) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.performer = performer;
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

    public void setPerformer(String performer) {
        this.performer = performer;
    }

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

    @Override
    public String toString() {
        return "Concert{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", performer='" + performer + '\'' +
                '}';
    }
}
