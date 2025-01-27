import React, { useEffect, useState } from "react";
import axios from "axios";
import "./concertsNoArtists.css";
import { Link as RouterLink } from "react-router-dom";

const ConcertsNoArtist = () => {
  const [concerts, setConcerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [hasAccess, setHasAccess] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const headers = token
      ? {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        }
      : undefined;

    axios
      .get("http://localhost:8080/user-info", { withCredentials: true, headers })
      .then((response) => {
        const userRoles = response.data.roles || [];
        if (userRoles.includes("USER") || userRoles.includes("ADMIN") || userRoles.includes("ARTIST")) {
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
      const response = await axios.get("http://localhost:8080/concerts/all", { withCredentials: true, headers });
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
            Concerts
          </div>
        </div>
      </section>
      <div className="concerts-message">
        <h2>Hmm... seems like this artist doesn't have any upcoming concerts yet.</h2>
        <p>Take a look at some other concerts coming soon:</p>
      </div>
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
            </div>
          ))
        ) : (
          <p>No concerts found.</p>
        )}
      </div>
    </div>
  );
};

export default ConcertsNoArtist;
