
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Link as RouterLink } from "react-router-dom";

const ArtistConcerts = () => {
  const { artistId } = useParams(); // Dobivamo artistId iz URL parametra
  const [concerts, setConcerts] = useState([]);

  useEffect(() => {
    // Fetch koncerata za odabranog izvođača
    fetch(`http://localhost:8080/concerts/artist/${artistId}`, {
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch concerts");
        }
        return response.json();
      })
      .then((data) => {
        setConcerts(data.concerts);
      })
      .catch((error) => console.error("Error fetching concerts:", error));
  }, [artistId]);

  return (
    <div style={{ padding: "20px" }}>
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
                Concerts of Artists
              </div>
            </div>
          </section>
      <div>
        {concerts.length > 0 ? (
          concerts.map((concert, index) => (
            <div key={concert.id} style={{ marginBottom: "15px" }}>
              <h3>{concert.name}</h3>
              <p>{concert.date}</p>
              <p>{concert.venue}</p>
            </div>
          ))
        ) : (
          <p>No concerts found for this artist.</p>
        )}
      </div>
    </div>
  );
};

export default ArtistConcerts;
