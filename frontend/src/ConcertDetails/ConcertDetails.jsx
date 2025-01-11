import React, { useEffect, useState } from "react";
import "./styleConcert.css";

const ConcertsResults = () => {
  const [concerts, setConcerts] = useState([]);

  useEffect(() => {
    
    const storedConcerts = JSON.parse(localStorage.getItem("concerts"));

    if (!storedConcerts || storedConcerts.length === 0) {
      alert("No concerts found.");
      return;
    }

    setConcerts(storedConcerts);
  }, []);

  return (
    <div id="concerts-container">
      {concerts.length > 0 ? (
        concerts.map((concert, index) => (
          <div className="concert" key={index}>
            <div className="concert-image">
              <img
                src={concert.imageUrl || "default-image.jpg"}
                alt={concert.name || "Concert"}
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
              <a href={concert.url} target="_blank" rel="noopener noreferrer">
                Buy tickets
              </a>
            </div>
          </div>
        ))
      ) : (
        <p>No concerts found.</p>
      )}
    </div>
  );
};

export default ConcertsResults;
