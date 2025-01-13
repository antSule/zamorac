import React, { useEffect, useState } from "react";
import axios from "axios";
import "./concerts.css";

const Concerts = () => {
  const [concerts, setConcerts] = useState([]);
  const [loading, setLoading] = useState(true);

 useEffect(() => {
     const token = localStorage.getItem("token");
     const headers = token
       ? {
           'Content-Type': 'application/json',
           'Authorization': `Bearer ${token}`,
         }
       : undefined;

     axios
       .get("http://localhost:8080/concerts/all", { withCredentials:true,headers })
       .then((response) => {
         if (Array.isArray(response.data)) {
           setConcerts(response.data);
         } else {
           console.error("Expected an array of concerts but got:", response.data);
         }
       })
       .catch((error) => console.error("Error fetching concerts: ", error))
       .finally(() => setLoading(false));
   }, []);

  return (
    <div id="concerts-container">
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
  );
};

export default Concerts;
