import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './concertAdd.css';

const AddNewConcert = () => {
  const navigate = useNavigate();
  const [concertDate, setConcertDate] = useState('');
  const [concertTime, setConcertTime] = useState('');
  const [performerName, setPerformerName] = useState('');
  const [city, setCity] = useState('');
  const [locationDetails, setLocationDetails] = useState('');
  const [eventName, setEventName] = useState('');
  const [venueDetails, setVenueDetails] = useState('');
  const [imageUrl, setImageUrl] = useState('');
  const [hasAccess, setHasAccess] = useState(false);
  const [error, setError] = useState(null);

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
        setError('Error checking user roles.');
        console.error(err);
        setHasAccess(false);
      });
  }, []);

  const openLocationSelector = () => {
    setLocationDetails('');
    window.location.href = 'http://localhost:63342/zamorac/frontend/src/GoogleMapsAdd/GoogleMaps.html';
  };

  const clearLocation = () => {
    setLocationDetails('');
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();

    const token = localStorage.getItem('token');
    const headers = token
      ? {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        }
      : undefined;

    const concertData = {
      date: concertDate,
      time: concertTime,
      performer: performerName,
      city,
      venue: venueDetails,
      event: eventName,
      imageUrl,
      ...(locationDetails && {
        latitude: locationDetails.split(',')[0].split(':')[1]?.trim(),
        longitude: locationDetails.split(',')[1].split(':')[1]?.trim(),
      }),
    };

    axios
      .post('http://localhost:8080/concerts/add', concertData, { headers })
      .then(() => {
        alert('Concert added successfully!');
        navigate('/home');
      })
      .catch((err) => {
        console.error('Error adding concert:', err);
        setError('Failed to add concert. Please try again.');
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
    <div className="concert-add-container">
      <h1>Add New Concert</h1>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form id="concert-add-form" className="formANC" onSubmit={handleFormSubmit}>
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

        <label htmlFor="city">City:</label>
        <input
          type="text"
          id="city"
          value={city}
          onChange={(e) => setCity(e.target.value)}
        />

        <label htmlFor="concert-location">Select Location:</label>
        <input
          type="text"
          id="concert-location"
          readOnly
          value={locationDetails}
        />
        <div className="button-group">
          <button type="button" onClick={openLocationSelector}>
            Select Location
          </button>
          <button type="button" id="button-clear-location" onClick={clearLocation}>
            Clear Location
          </button>
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

        <button type="submit" id="button-addConcert">
          Add Concert
        </button>
      </form>
    </div>
  );
};

export default AddNewConcert;