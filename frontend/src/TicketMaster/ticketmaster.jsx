import React, { useState, useEffect } from "react";
import './ticketmaster.css';
import { useNavigate } from 'react-router-dom';

const Ticketmaster = () => {
    const navigate = useNavigate();
    const [date, setDate] = useState("");
    const [artist, setArtist] = useState("");
    const [location, setLocation] = useState("");
    const [radius, setRadius] = useState("");

  useEffect(() => {
    const queryParams = new URLSearchParams(window.location.search);
    const lat = queryParams.get('lat');
    const lng = queryParams.get('lng');

    if (lat && lng) {
        const locationText = `Lat: ${lat}, Lng: ${lng}`;
        setLocation(locationText);
    }
    const savedDate = localStorage.getItem("concert-date");
    const savedArtist = localStorage.getItem("artist-name");
    const savedRadius = localStorage.getItem("radius");
    const savedLocation = localStorage.getItem("selectedLocation");

    if (savedDate) setDate(savedDate);
    if (savedArtist) setArtist(savedArtist);
    if (savedRadius) setRadius(savedRadius);
    if (savedLocation) setLocation(savedLocation);
  }, []);

    useEffect(() => {
    if (date) localStorage.setItem("concert-date", date);
    if (artist) localStorage.setItem("artist-name", artist);
    if (radius) localStorage.setItem("radius", radius);
    if (location) localStorage.setItem("selectedLocation", JSON.stringify({ lat: location.split(",")[0].split(":")[1].trim(), lng: location.split(",")[1].split(":")[1].trim() }));
    }, [date, artist, location, radius]);

  const handleLocationClick = () => {
        setLocation("");
        localStorage.removeItem("selectedLocation");
        window.location.href = 'http://localhost:63342/zamorac/frontend/src/GoogleMapsTicket/GoogleMaps.html?_ijt=ml0hnlo0ra6317f2o6s3o373bo&_ij_reload=RELOAD_ON_SAVE';
  };

  const handleClearLocation = () => {
    setLocation("");
    localStorage.removeItem("selectedLocation");
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    let query = "";
    if (radius && !location) {
      alert("Please select a location before entering a radius.");
      return;
    }

    if (date) query += `?date=${date}`;
    if (artist) query += `${query ? '&' : '?'}artist=${artist}`;
    if (location) {
      const [latPart, lngPart] = location.split(',');
      const lat = latPart.split(':')[1].trim();
      const lng = lngPart.split(':')[1].trim();
      query += `${query ? '&' : '?'}latitude=${lat}&longitude=${lng}`;
    }
    if (location && radius) query += `${query ? '&' : '?'}radius=${radius}`;

    fetch(`http://localhost:8080/concerts/concerts${query}`, { method: 'GET' })
      .then(response => {
        if (!response.ok) {
          throw new Error('No concerts match your search criteria. Please try different parameters.');
        }
        return response.json();
      })
      .then(data => {
        if (data.length === 0) {
          alert("No concerts match your search criteria. Please try different parameters.");
        } else {
          localStorage.setItem('concerts', JSON.stringify(data));
          window.open('http://localhost:3000/ConcertDetails', '_blank');
        }
      });
  };

  return (
    <div>
      <form id="parameters-form" onSubmit={handleSubmit}>
        <div className="naslov">Search for concerts</div>
        <label htmlFor="concert-date">Select the date of the concert:</label>
        <br />
        <input
          type="date"
          id="concert-date"
          name="concert-date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />
        <br />
        <label htmlFor="artist-name">Artist name:</label>
        <br />
        <input
          type="text"
          id="artist-name"
          name="artist-name"
          value={artist}
          onChange={(e) => setArtist(e.target.value)}
        />
        <br />
        <label htmlFor="location">Location:</label>
        <br />
        <input
          type="text"
          id="location"
          name="location"
          readOnly
          value={location}
        />
        <div className="button-group">
          <button type="button" id="button-location" onClick={handleLocationClick}>
            Select location
          </button>
          <br />
          <button type="button" id="button-clear-location" onClick={handleClearLocation}>
            Clear location
          </button>
          <br />
        </div>
        <label htmlFor="radius">Radius in km:</label>
        <br />
        <input
          type="text"
          id="radius"
          name="radius"
          value={radius}
          onChange={(e) => setRadius(e.target.value)}
        />
        <br />
        <button type="submit">Send parameters</button>
      </form>
    </div>
  );
};

export default Ticketmaster;
