import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import './editConcert.css';
import { Link as RouterLink } from "react-router-dom";

const EditConcertADMIN = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [locationDetails, setLocationDetails] = useState("");
  const [formData, setFormData] = useState({
    date: "",
    time: "",
    performer: "",
    performerId: "",
    venue: "",
    latitude: "",
    longitude: "",
    city: "",
    event: "",
    imageUrl: "",
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [hasAccess, setHasAccess] = useState(false);
  const [concertFetchError, setConcertFetchError] = useState(false);
  const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
  const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

  useEffect(() => {
    const checkUserRole = async () => {
      try {
        const token = localStorage.getItem("token");
        const headers = token ? { Authorization: `Bearer ${token}` } : undefined;

        const response = await axios.get(`${BACKEND_URL}/user-info`, { withCredentials: true, headers });

        const userRoles = response.data.roles || [];
        if (userRoles.includes('ADMIN')) {
          setHasAccess(true); 
        } else {
          setHasAccess(false);
          setLoading(false);
        }
      } catch (err) {
        setError('Error verifying user roles.');
        console.error(err);
      }
    };

    checkUserRole();
  }, []);

  useEffect(() => {
    if(hasAccess){
    const fetchConcert = async () => {
      try {
        const token = localStorage.getItem("token");
        const headers = token
          ? { Authorization: `Bearer ${token}` }
          : undefined;

        const response = await axios.get(
          `${BACKEND_URL}/concerts/concert-info?id=${id}`,
          { withCredentials: true, headers }
        );

        console.log("Fetched concert data:", response.data);
        if(response.data === ""){
          setConcertFetchError(true);
        }

        setFormData({
          date: response.data.date || "",
          time: response.data.time || "",
          performer: response.data.performer || "",
          performerId: response.data.performerId ||"",
          venue: response.data.venue || "",
          latitude: (() => {
              const queryParams = new URLSearchParams(window.location.search);
              const lat = queryParams.get("lat");
              if (lat) return lat;

              const locationFromLocalStorage = localStorage.getItem("concert-location");
              if (locationFromLocalStorage) {
                const parsedLocation = JSON.parse(locationFromLocalStorage);
                if (parsedLocation.lat) return parsedLocation.lat;
              }

              return response.data.latitude || "";
            })(),
            longitude: (() => {
              const queryParams = new URLSearchParams(window.location.search);
              const lng = queryParams.get("lng");
              if (lng) return lng;

              const locationFromLocalStorage = localStorage.getItem("concert-location");
              if (locationFromLocalStorage) {
                const parsedLocation = JSON.parse(locationFromLocalStorage);
                if (parsedLocation.lng) return parsedLocation.lng;
              }

              return response.data.longitude || "";
            })(),
          city: response.data.city || "",
          event: response.data.event || "",
          imageUrl: response.data.imageUrl || "",
        });

        if (response.data.latitude && response.data.longitude) {
            const locationText = `Lat: ${response.data.latitude}, Lng: ${response.data.longitude}`;
            setLocationDetails(locationText);
        }

        const queryParams = new URLSearchParams(window.location.search);
          const lat = queryParams.get('lat');
          const lng = queryParams.get('lng');

          if (lat && lng) {
              const locationText = `Lat: ${lat}, Lng: ${lng}`;
              setLocationDetails(locationText);
              response.data.latitude = lat;
              response.data.longitude = lng;
          }

        const locationFromLocalStorage = localStorage.getItem("concert-location");
        if (locationFromLocalStorage) {
            const parsedLocation = JSON.parse(locationFromLocalStorage);
            setFormData((prev) => ({
            ...prev,
            latitude: parsedLocation.lat || prev.latitude,
            longitude: parsedLocation.lng || prev.longitude,
            }));
        setLocationDetails(`Lat: ${parsedLocation.lat}, Lng: ${parsedLocation.lng}`);
        }
      } catch (err) {
        console.error("Error fetching concert data:", err.response?.data || err.message);
        setError("Error fetching concert data.");
      } finally {
        setLoading(false);
      }
    };

    fetchConcert();
  }
  }, [hasAccess, id]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const openLocationSelector = () => {
  setLocationDetails("");
  localStorage.removeItem("concert-location");

  window.location.href = `${FRONTEND_URL}/google-maps-edit-admin/${id}`;
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const headers = token
        ? {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          }
        : undefined;

      const payload = { ...formData };

      console.log("Submitting updated concert data:", payload);

      await axios.put(
        `${BACKEND_URL}/concerts/edit-concert?id=${id}`,
        payload,
        { withCredentials: true, headers }
      );

      navigate("/manage-concerts");
    } catch (err) {
      console.error("Error updating concert:", err.response?.data || err.message);
      setError("Error updating concert.");
    }
  };

  if (loading) return <p>Loading concert details...</p>;
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
  if(concertFetchError) return <p>Concert not found.</p>
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div className="edit-concertMain">
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
                  src="/fakelogo.png"
                  alt="logo"
                  width={100}
                  style={{
                    position: "absolute",
                    left: "20px",
                    top: "10px",
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
                Edit Concert
              </div>
            </div>
          </section>
        <div className="edit-concert">
          <form className='formECA' onSubmit={handleFormSubmit}>
            <h2 className="naslovENC">Edit Concert</h2>

            <label className="labelECA" htmlFor="event">Event Name</label>
            <input
              className="inputECA"
              type="text"
              name="event"
              id="event"
              value={formData.event || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="performer">Performer Name</label>
            <input
              className="inputECA"
              type="text"
              name="performer"
              id="performer"
              value={formData.performer || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="city">City</label>
            <input
              className="inputECA"
              type="text"
              name="city"
              id="city"
              value={formData.city || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="date">Date</label>
            <input
              className="inputECA"
              type="date"
              name="date"
              id="date"
              value={formData.date || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="time">Time</label>
            <input
              className="inputECA"
              type="time"
              name="time"
              id="time"
              value={formData.time || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="venue">Venue</label>
            <input
              className="inputECA"
              type="text"
              name="venue"
              id="venue"
              value={formData.venue || ""}
              onChange={handleInputChange}
              required
            />

            <label className="labelECA" htmlFor="concert-location">Select Location:</label>
            <input
                className="inputECA"
                type="text"
                id="concert-location"
                readOnly
                value={locationDetails}
            />
            <div className="button-groupECA">
                <button className = "buttonECA" type="button" onClick={openLocationSelector}>Change Location</button>
            </div>

            <label className="labelECA" htmlFor="imageUrl">Image URL</label>
            <input
              className="inputECA"
              type="text"
              name="imageUrl"
              id="imageUrl"
              value={formData.imageUrl || ""}
              onChange={handleInputChange}
            />

            <button className="updateconcert" type="submit">Update Concert</button>
          </form>
        </div>
    </div>
  );
};

export default EditConcertADMIN;