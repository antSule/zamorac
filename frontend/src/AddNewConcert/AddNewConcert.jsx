import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Link as RouterLink } from "react-router-dom";
import './concertAdd.css';

const AddNewConcert = () => {
    const navigate = useNavigate();

    const [concertDate, setConcertDate] = useState("");
    const [concertTime, setConcertTime] = useState("");
    const [performerName, setPerformerName] = useState("");
    const [city, setCity] = useState("");
    const [locationDetails, setLocationDetails] = useState("");
    const [eventName, setEventName] = useState("");
    const [venueDetails, setVenueDetails] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const [hasAccess, setHasAccess] = useState(false);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const headers = token
            ? {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`,
            }
            : undefined;

        axios
            .get('http://localhost:8080/user-info', { withCredentials: true, headers })
            .then((response) => {
                const userRoles = response.data.roles || [];
                if (userRoles.includes('ADMIN') || userRoles.includes('ARTIST')) {
                    setHasAccess(true);
                } else {
                    setHasAccess(false);
                }
            })
            .catch((err) => {
                console.error(err);
            })
    }, []);

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
        const savedCity = localStorage.getItem("city");
        const savedLocation = localStorage.getItem("concert-location");
        const savedVenue = localStorage.getItem("venue-details");
        const savedEvent = localStorage.getItem("event-name");
        const savedImageUrl = localStorage.getItem("image-url");

        if (savedDate) setConcertDate(savedDate);
        if (savedTime) setConcertTime(savedTime);
        if (savedPerformer) setPerformerName(savedPerformer);
        if (savedCity) setCity(savedCity);
        if (savedLocation) {
            const location = JSON.parse(savedLocation);
            if (location.lat && location.lng) {
                setLocationDetails(`Lat: ${location.lat}, Lng: ${location.lng}`);
            }
        }
        if (savedVenue) setVenueDetails(savedVenue);
        if (savedEvent) setEventName(savedEvent);
        if (savedImageUrl) setImageUrl(savedImageUrl);
    }, []);

    useEffect(() => {
        if (concertDate) localStorage.setItem("concert-date", concertDate);
        if (concertTime) localStorage.setItem("concert-time", concertTime);
        if (performerName) localStorage.setItem("performer-name", performerName);
        if (city) localStorage.setItem("city", city);
        if (locationDetails) localStorage.setItem("concert-location", JSON.stringify({ lat: locationDetails.split(",")[0].split(":")[1].trim(), lng: locationDetails.split(",")[1].split(":")[1].trim() }));
        if (venueDetails) localStorage.setItem("venue-details", venueDetails);
        if (eventName) localStorage.setItem("event-name", eventName);
        if (imageUrl) localStorage.setItem("image-url", imageUrl);
    }, [concertDate, concertTime, performerName, city, locationDetails, venueDetails, eventName, imageUrl]);

    const openLocationSelector = () => {
        setLocationDetails("");
        localStorage.removeItem("concert-location");

        window.location.href = 'http://localhost:3000/google-maps';
    };

    const clearLocation = () => {
        setLocationDetails("");
        localStorage.removeItem("concert-location");
    };

    const handleFormSubmit = (e) => {
        e.preventDefault();

        const token = localStorage.getItem("token");
        const headers = {
            'Content-Type': 'application/json',
            ...(token && { 'Authorization': `Bearer ${token}` }),
        };

        let concertData = {
            date: concertDate,
            time: concertTime,
            performer: performerName,
            city: city,
            venue: venueDetails,
            event: eventName,
            imageUrl: imageUrl
        };

        if (locationDetails && locationDetails.includes(',') && locationDetails.includes(':')) {
            const [latPart, lngPart] = locationDetails.split(',');
            const lat = latPart.split(':')[1]?.trim();
            const lng = lngPart.split(':')[1]?.trim();

            if (lat && lng) {
                concertData = { ...concertData, latitude: lat, longitude: lng };
            }
        }

        fetch("http://localhost:8080/concerts/add", {
            method: 'POST',
            headers,
            body: JSON.stringify(concertData),
            credentials: 'include',
        })
        .then(response => {
            console.log("Response status:", response.status);
            if (!response.ok) {
                return response.text().then((text) => {
                    throw new Error(`Failed to add concert: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            navigate('/my-concerts');
        })
        .catch(error => {
            console.error("Error details:", error);
            alert('Error: ' + error.message);
        });
    };

    if (!hasAccess) {
        return (
            <div className="no-access-container">
                <div className="no-access-message">
                    <h2>⚠️ Access Denied</h2>
                    <p>You do not have permission to access this page.</p>
                </div>
            </div>
        );
    }

    return (
    <body className="bodyANC">
    <section className="h-wrapper">
                <div
                  className="flexCenter paddings innerWidth h-container"
                  style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    position: "relative",
                    height: "120px",
                  }}
                >
                <RouterLink to="/home" className="buttonLink">
                  <img
                    src="fakelogo.png"
                    alt="logo"
                    width={100}
                    style={{
                      position: "absolute",
                      left: "20px",
                      top: "10px"
                    }}
                  />
                  </RouterLink>
                  <div
                    style={{
                      fontSize: "6rem",
                      fontWeight: "bold",
                      color: "white",
                    }}
                  >
                    Add Concert
                  </div>
                </div>
              </section>
        <div className="concert-add-containerANC">
            <form id="concert-add-form" className="formANC" onSubmit={handleFormSubmit}>

                <div className="naslovANC">Add Your Concert</div>

                <label className="labelANC" htmlFor="concert-date">Select Concert Date:</label>
                <input
                    className="inputANC"
                    type="date"
                    id="concert-date"
                    value={concertDate}
                    onChange={(e) => setConcertDate(e.target.value)}
                />

                <label className = "labelANC" htmlFor="concert-time">Select Concert Time:</label>
                <input
                    className="inputANC"
                    type="time"
                    id="concert-time"
                    value={concertTime}
                    onChange={(e) => setConcertTime(e.target.value)}
                />

                <label className="labelANC" htmlFor="performer-name">Enter Performer Name:</label>
                <input
                    className="inputANC"
                    type="text"
                    id="performer-name"
                    value={performerName}
                    onChange={(e) => setPerformerName(e.target.value)}
                />

                <label className = "labelANC" htmlFor="city">City:</label>
                <input
                    className="inputANC"
                    type="text"
                    id="city"
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                />

                <label className = "labelANC" htmlFor="concert-location">Select Location:</label>
                <input
                    className="inputANC"
                    type="text"
                    id="concert-location"
                    readOnly
                    value={locationDetails}
                />
                <div className="button-groupANC">
                    <button  className = "buttonANC" type="button" onClick={openLocationSelector}>Select Location</button>
                    <button  className = "buttonANC" type="button" id="button-clear-location" onClick={clearLocation}>Clear Location</button>
                </div>

                <label className="labelANC" htmlFor="venue">Venue Name:</label>
                <input
                    className="inputANC"
                    type="text"
                    id="venue"
                    value={venueDetails}
                    onChange={(e) => setVenueDetails(e.target.value)}
                />

                <label className="labelANC" htmlFor="event-name">Event Name:</label>
                <input
                    className="inputANC"
                    type="text"
                    id="event-name"
                    value={eventName}
                    onChange={(e) => setEventName(e.target.value)}
                />

                <label className = "labelANC" htmlFor="image-url">Image URL (Optional):</label>
                <input
                    className="inputANC"
                    type="url"
                    id="image-url"
                    value={imageUrl}
                    onChange={(e) => setImageUrl(e.target.value)}
                />

                <button className="buttonANC" type="submit" id="button-addConcert">Add Concert</button>
            </form>
        </div>
    </body>
    );
};

export default AddNewConcert;