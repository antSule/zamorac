import React, { useEffect, useState } from "react";
import axios from "axios";
import "./concerts.css";
import { Link as RouterLink } from "react-router-dom";

const Concerts = () => {
  const [concerts, setConcerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [hasAccess, setHasAccess] = useState(false);
  const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
  const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

  // Local storage state for favorites
  const [favorites, setFavorites] = useState(() => {
    const storedFavorites = localStorage.getItem("favorites");
    return storedFavorites ? JSON.parse(storedFavorites) : [];
  });

  useEffect(() => {
    const token = localStorage.getItem("token");
    const headers = token
      ? {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      : undefined;

    axios
      .get(`${BACKEND_URL}/user-info`, { withCredentials: true, headers })
      .then((response) => {
        const userRoles = response.data.roles || [];
        if (userRoles.includes('USER') || userRoles.includes('ADMIN') || userRoles.includes('ARTIST')) {
          setHasAccess(true);
          fetchConcerts(headers);
        } else {
          setHasAccess(false);
        }
      })
      .catch((error) => {
        console.error("Error fetching user roles: ", error);
        setHasAccess(false);
      });
  }, []);

  const fetchConcerts = async (headers) => {
    try {
      const response = await axios.get(`{BACKEND_URL}/concerts/all`, { withCredentials: true, headers });
      if (Array.isArray(response.data)) {
        setConcerts(response.data);
      } else {
        console.error("Expected an array of concerts but got:", response.data);
      }
    } catch (error) {
      console.error("Error fetching concerts: ", error);
    } finally {
      setLoading(false);
    }
  };

  const addToFavorites = async (performerId) => {
    const token = localStorage.getItem("token");
    const headers = token
      ? {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      : undefined;

    try {
      await axios.post(
        `${BACKEND_URL}/add-favorite?artistId=${performerId}`,
        null,
        { withCredentials: true, headers }
      );

      const updatedFavorites = [...favorites, performerId];
      setFavorites(updatedFavorites);
      localStorage.setItem("favorites", JSON.stringify(updatedFavorites));
      alert("Added to favorites successfully!");
    } catch (error) {
      console.error("Failed to add to favorites:", error);
      alert("Failed to add to favorites.");
    }
  };


  const removeFromFavorites = async (performerId) => {
    const token = localStorage.getItem("token");
    const headers = token
      ? {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      : undefined;

    try {
      await axios.post(
        `${BACKEND_URL}/remove-favorite?artistId=${performerId}`,
        null,
        { withCredentials: true, headers }
      );

      const updatedFavorites = favorites.filter((id) => id !== performerId);
      setFavorites(updatedFavorites);
      localStorage.setItem("favorites", JSON.stringify(updatedFavorites));
      alert("Removed from favorites successfully!");
    } catch (error) {
      console.error("Failed to remove from favorites:", error);
      alert("Failed to remove from favorites.");
    }
  };


  const isFavorite = (performerId) => favorites.includes(performerId);

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
    <div id="concerts-container">
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
            Concerts
          </div>
        </div>
      </section>
      <div className="grid">
        {loading ? (
          <p>Loading concerts...</p>
        ) : concerts.length > 0 ? (
          concerts.map((concert, index) => (
            <div className="concert" key={index}>
              <div className="concert-image">
                <img
                  src={concert.imageUrl || "/fakelogo.png"}
                  alt={concert.event || "Concert"}
                />
              </div>
              <div className="concert-details">
                <h3>{concert.event}</h3>
                <p>
                  <strong>Performer:</strong> {concert.performer}
                </p>
                <p>
                  <strong>City:</strong> {concert.city}
                </p>
                <p>
                  <strong>Date:</strong> {concert.date}
                </p>
                <p>
                  <strong>Time:</strong> {concert.time}
                </p>
                <p>
                  <strong>Venue:</strong> {concert.venue}
                </p>
              </div>
              <div className="buttongroupMU">
                {isFavorite(concert.performerId) ? (
                  <button
                    className="removefromfav"
                    onClick={() => removeFromFavorites(concert.performerId)}
                  >
                    Remove from Favorites
                  </button>
                ) : (
                  <button
                    className="addtofav"
                    onClick={() => addToFavorites(concert.performerId)}
                  >
                    Add to Favorites
                  </button>
                )}
              </div>
            </div>
          ))
        ) : (
          <p>No concerts found.</p>
        )}
      </div>
    </div>
  );
};

export default Concerts;