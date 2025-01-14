
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const ArtistConcerts = () => {
  const { artistId } = useParams(); // Dobivamo artistId iz URL parametra
  const [concerts, setConcerts] = useState([]);

  useEffect(() => {
    // Fetch koncerata za odabranog izvođača
    fetch(`https://ticketmestarbackend-yqpn.onrender.com/concerts/artist/${artistId}`, {
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
      <h2>Concerts of Artist {artistId}</h2>
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
