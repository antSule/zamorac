import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import "./myConcerts.css"
import { Link as RouterLink } from "react-router-dom";

const MyConcerts = () => {
  const [concerts, setConcerts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [concertToDelete, setConcertToDelete] = useState(null);
  const [hasAccess, setHasAccess] = useState(false);
  const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
  const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

  const navigate = useNavigate();
  const fetchMyConcerts = async (headers) => {
      try {
        const response = await axios.get(`${BACKEND_URL}/concerts/me`, { withCredentials:true,headers });
        const data = response.data;
        console.log(data);
        if (Array.isArray(data)) {
          setConcerts(data);
        } else if (data && Array.isArray(data.concerts)) {
          setConcerts(data.concerts);
        } else {
          console.error("Unexpected data format:", data);
          setConcerts([]);
        }
      } catch (err) {
        setError("Error fetching your concerts.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

  const deleteConcert = async (concertId, headers) => {
    try {
      await axios.post(`${BACKEND_URL}/concerts/delete?concertId=${concertId}`, {}, { withCredentials: true, headers });
      fetchMyConcerts(headers);
    } catch (err) {
      setError('Error deleting concert.');
      console.error(err);
    }
  };


  const handleDeleteClick = (concertId) => {
    setConcertToDelete(concertId);
    setShowDeleteModal(true);
  };

  const handleConfirmDelete = () => {
      const token = localStorage.getItem("token");
      const headers = token
          ? {
               'Content-Type': 'application/json',
               'Authorization': `Bearer ${token}`,
             }
           : undefined;
    deleteConcert(concertToDelete, headers);
    setShowDeleteModal(false);
  };

  const handleCancelDelete = () => {
    setShowDeleteModal(false);
  };

  useEffect(() => {
      const token = localStorage.getItem('token');
      const headers = token
        ? {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
          }
        : undefined;

      axios
        .get(`${BACKEND_URL}/user-info`, { withCredentials: true, headers })
        .then((response) => {
          const userRoles = response.data.roles || [];
          if (userRoles.includes('ADMIN') || userRoles.includes('ARTIST')) {
            setHasAccess(true);
            fetchMyConcerts(headers);
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
    <div className='MyConcerts'>
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
                    My Concerts
                  </div>
                </div>
              </section>
    <div id="concerts-containerMC">
      {loading ? (
        <p>Loading concerts...</p>
      ) : error ? (
        <p style={{ color: 'red' }}>{error}</p>
      ) : concerts.length > 0 ? (
        concerts.map((concert, index) => (
          <div className="concertMC" key={index}>
            <div className="concert-imageMC">
              <img
                src={concert.imageUrl || '/fakelogo.png'}
                alt={concert.name || 'Concert'}
              />
            </div>
            <div className="concert-detailsMC">
              <h3>{concert.event}</h3>
              <p><strong>Performer:</strong> {concert.performer}</p>
              <p><strong>City:</strong> {concert.city}</p>
              <p><strong>Date:</strong> {concert.date}</p>
              <p><strong>Time:</strong> {concert.time}</p>
              <p><strong>Venue:</strong> {concert.venue}</p>
              <button className = "buttonEdit" onClick={() => navigate(`/edit-concert/${concert.id}`)}>Edit</button>
              <button className='deleteMC' onClick={() => handleDeleteClick(concert.id)}>Delete</button>
            </div>
          </div>
        ))
      ) : (
        <p className='NCF'>No concerts found.</p>
      )}

      {showDeleteModal && (
        <div className="delete-modal">
          <div className="modal-content">
            <h4>Are you sure you want to delete this concert?</h4>
            <button onClick={handleConfirmDelete}>Yes</button>
            <button onClick={handleCancelDelete}>No</button>
          </div>
        </div>
      )}
    </div>
    </div>
  );
};

export default MyConcerts;