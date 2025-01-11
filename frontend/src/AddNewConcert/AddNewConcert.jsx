import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import './concertAdd.css';

const AddNewConcert = () => {
    const navigate = useNavigate();

    const [concertDate, setConcertDate] = useState("");
    const [concertTime, setConcertTime] = useState("");
    const [performerName, setPerformerName] = useState("");
    const [locationDetails, setLocationDetails] = useState("");
    const [eventName, setEventName] = useState("");
    const [venueDetails, setVenueDetails] = useState("");
    const [imageUrl, setImageUrl] = useState("");

    useEffect(() => {
        const queryParams = new URLSearchParams(window.location.search);
        const lat = queryParams.get('lat');
        const lng = queryParams.get('lng');

        if (lat && lng) {
            const locationText = `Lat: ${lat}, Lng: ${lng}`;
            setLocationDetails(locationText);
        }

        const savedDate = localStorage.getItem("concert-date");
        const savedTime = localStorage.getItem("concert-time");
        const savedPerformer = localStorage.getItem("performer-name");
        const savedLocation = localStorage.getItem("concert-location");
        const savedVenue = localStorage.getItem("venue-details");
        const savedEvent = localStorage.getItem("event-name");
        const savedImageUrl = localStorage.getItem("image-url");

        if (savedDate) setConcertDate(savedDate);
        if (savedTime) setConcertTime(savedTime);
        if (savedPerformer) setPerformerName(savedPerformer);
        if (savedLocation) setLocationDetails(savedLocation);
        if (savedVenue) setVenueDetails(savedVenue);
        if (savedEvent) setEventName(savedEvent);
        if (savedImageUrl) setImageUrl(savedImageUrl);
    }, []);

    useEffect(() => {
        if (concertDate) localStorage.setItem("concert-date", concertDate);
        if (concertTime) localStorage.setItem("concert-time", concertTime);
        if (performerName) localStorage.setItem("performer-name", performerName);
        if (locationDetails) localStorage.setItem("concert-location", JSON.stringify({ lat: locationDetails.split(",")[0].split(":")[1].trim(), lng: locationDetails.split(",")[1].split(":")[1].trim() }));
        if (venueDetails) localStorage.setItem("venue-details", venueDetails);
        if (eventName) localStorage.setItem("event-name", eventName);
        if (imageUrl) localStorage.setItem("image-url", imageUrl);
    }, [concertDate, concertTime, performerName, locationDetails, venueDetails, eventName, imageUrl]);

    const openLocationSelector = () => {
        setLocationDetails("");
        localStorage.removeItem("concert-location");

        window.location.href = 'http://localhost:63342/zamorac/frontend/src/GoogleMapsAdd/GoogleMaps.html?_ijt=ml0hnlo0ra6317f2o6s3o373bo&_ij_reload=RELOAD_ON_SAVE';
    };

    const clearLocation = () => {
        setLocationDetails("");
        localStorage.removeItem("concert-location");
    };

    const handleFormSubmit = (e) => {
        e.preventDefault();

        let concertData = {
            date: concertDate,
            time: concertTime,
            performer: performerName,
            venue: venueDetails,
            event: eventName,
            imageUrl: imageUrl
        };

        if (locationDetails) {
            const [latPart, lngPart] = locationDetails.split(',');
            const lat = latPart.split(':')[1].trim();
            const lng = lngPart.split(':')[1].trim();
            concertData = { ...concertData, latitude: lat, longitude: lng };
        }

        const token = localStorage.getItem("token");

        fetch("http://localhost:8080/concerts/add", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(concertData),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add concert.');
            }
            return response.json();
        })
        .then(data => {
            alert('Concert added successfully!');
            navigate('/home');
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    };

    return (
        <div className="concert-add-container">
            <form id="concert-add-form" onSubmit={handleFormSubmit}>
                <div className="naslov">Add Your Concert</div>

                <label htmlFor="concert-date">Select Concert Date:</label>
                <input
                    type="date"
                    id="concert-date"
                    value={concertDate}
                    onChange={(e) => setConcertDate(e.target.value)}
                />

                <label htmlFor="concert-time">Select Concert Time:</label>
                <input
                    type="time"
                    id="concert-time"
                    value={concertTime}
                    onChange={(e) => setConcertTime(e.target.value)}
                />

                <label htmlFor="performer-name">Enter Performer Name:</label>
                <input
                    type="text"
                    id="performer-name"
                    value={performerName}
                    onChange={(e) => setPerformerName(e.target.value)}
                />

                <label htmlFor="concert-location">Select Location:</label>
                <input
                    type="text"
                    id="concert-location"
                    readOnly
                    value={locationDetails}
                />
                <div className="button-group">
                    <button type="button" onClick={openLocationSelector}>Select Location</button>
                    <button type="button" id="button-clear-location" onClick={clearLocation}>Clear Location</button>
                </div>

                <label htmlFor="venue">Venue Name:</label>
                <input
                    type="text"
                    id="venue"
                    value={venueDetails}
                    onChange={(e) => setVenueDetails(e.target.value)}
                />

                <label htmlFor="event-name">Event Name:</label>
                <input
                    type="text"
                    id="event-name"
                    value={eventName}
                    onChange={(e) => setEventName(e.target.value)}
                />

                <label htmlFor="image-url">Image URL (Optional):</label>
                <input
                    type="url"
                    id="image-url"
                    value={imageUrl}
                    onChange={(e) => setImageUrl(e.target.value)}
                />

                <button type="submit" id = "button-addConcert">Add Concert</button>
            </form>
        </div>
    );
};

export default AddNewConcert;