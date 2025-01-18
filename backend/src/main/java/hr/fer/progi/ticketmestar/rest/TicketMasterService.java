package hr.fer.progi.ticketmestar.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.ticketmestar.domain.Concert;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketMasterService {
    private final String apiKey = "bYD4jWdQu2ENtPHcwET6c7XhNd28ngVU";

    public List<Concert> searchConcerts(String date, String artist, String latitude, String longitude, String radius) {
        StringBuilder apiUrl = new StringBuilder("https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey + "&classificationName=Music");

        if (date != null && !date.isEmpty()) {
            apiUrl.append("&startDateTime=").append(date).append("T00:00:00Z")
                    .append("&endDateTime=").append(date).append("T23:59:59Z");
        }

        if (artist != null && !artist.isEmpty()) {
            apiUrl.append("&keyword=").append(artist);
        }

        if (latitude != null && !latitude.isEmpty() && longitude != null && !longitude.isEmpty()) {
            apiUrl.append("&latlong=").append(latitude).append(",").append(longitude);

            if (radius == null || radius.isEmpty()) {
                radius = "20";
            }
        }

        if (radius != null && !radius.isEmpty()) {
            apiUrl.append("&radius=").append(radius).append("&unit=km");
        }

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(apiUrl.toString(), String.class);

        List<Concert> concerts = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode eventsNode = rootNode.path("_embedded").path("events");
            if (eventsNode.isArray()) {
                for (JsonNode eventNode : eventsNode) {

                    String eventName = eventNode.path("name").asText();
                    String eventUrl = eventNode.path("url").asText();
                    String venueName = eventNode.path("_embedded").path("venues").get(0).path("name").asText();
                    String cityName = eventNode.path("_embedded").path("venues").get(0).path("city").path("name").asText();
                    String startDateTime = eventNode.path("dates").path("start").path("dateTime").asText();
                    Double latitudeLoc = eventNode.path("_embedded").path("venues").get(0).path("location").path("latitude").asDouble();
                    Double longitudeLoc = eventNode.path("_embedded").path("venues").get(0).path("location").path("longitude").asDouble();

                    JsonNode attractionsNode = eventNode.path("_embedded").path("attractions");

                    String performerName = "Unknown Performer";
                    if (attractionsNode.isArray() && !attractionsNode.isEmpty()) {
                        performerName = attractionsNode.get(0).path("name").asText();
                    }

                    JsonNode imagesNode = eventNode.path("images");
                    String imageUrl = "";
                    if (imagesNode.isArray() && imagesNode.size() > 0) {
                        imageUrl = imagesNode.get(0).path("url").asText();
                    }

                    LocalDate dateParsed = LocalDate.parse(startDateTime.substring(0, 10));
                    LocalTime timeParsed = LocalTime.parse(startDateTime.substring(11, 16));

                    Concert concert = new Concert(dateParsed, timeParsed, performerName, venueName, latitudeLoc, longitudeLoc, eventUrl, cityName, eventName, imageUrl);
                    concerts.add(concert);
                }
            } else {
                System.out.println("Nema koncerata za taj datum.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return concerts;
    }
}
