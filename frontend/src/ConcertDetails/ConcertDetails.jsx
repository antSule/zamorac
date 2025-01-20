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
  <div className="ConcertDetailsBody">
    <div id="concerts-containerCD">
      {concerts.length > 0 ? (
        concerts.map((concert, index) => (
          <div className="concertCD" key={index}>
            <div className="concert-imageCD">
              <img
                src={concert.imageUrl || "/fakelogo.png"}
                alt={concert.name || "Concert"}
              />
            </div>
            <div className="concert-detailsCD">
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
    </div>
  );
};

export default ConcertsResults;
