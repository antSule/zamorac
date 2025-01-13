import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './concertsManager.css';

const ManageConcerts = () => {
  const [concerts, setConcerts] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [concertToDelete, setConcertToDelete] = useState(null);
  const [hasAccess, setHasAccess] = useState(false);

  const fetchConcerts = async (headers) => {
    try {
      const response = await axios.get('http://localhost:8080/concerts/all', { withCredentials:true,headers });
      setConcerts(response.data);
    } catch (err) {
      setError('Error fetching concerts.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const deleteConcert = async (concertId, headers) => {
    try {
      await axios.get(`http://localhost:8080/admin/remove?concertId=${concertId}`, { withCredentials:true,headers });
      fetchConcerts();
      alert('Concert successfully deleted!');
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
        .get('http://localhost:8080/user-info', { withCredentials: true, headers })
        .then((response) => {
          const userRoles = response.data.roles || [];
          if (userRoles.includes('ADMIN')) {
            setHasAccess(true);
            fetchConcerts(headers);
          } else {
            setHasAccess(false);
          }
        })
        .catch((err) => {
          setError('Error verifying user roles.');
          console.error(err);
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
    <div id="concerts-container">
      <h1>Manage Concerts</h1>
      {loading ? (
        <p>Loading concerts...</p>
      ) : error ? (
        <p style={{ color: 'red' }}>{error}</p>
      ) : concerts.length > 0 ? (
        concerts.map((concert, index) => (
          <div className="concert" key={index}>
            <div className="concert-image">
              <img
                src={concert.imageUrl || '/fakelogo.png'}
                alt={concert.name || 'Concert'}
              />
            </div>
            <div className="concert-details">
              <h3>{concert.event}</h3>
              <p><strong>Performer:</strong> {concert.performer}</p>
              <p><strong>City:</strong> {concert.city}</p>
              <p><strong>Date:</strong> {concert.date}</p>
              <p><strong>Time:</strong> {concert.time}</p>
              <p><strong>Venue:</strong> {concert.venue}</p>
              <button onClick={() => handleDeleteClick(concert.id)}>Delete</button>
            </div>
          </div>
        ))
      ) : (
        <p>No concerts found.</p>
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
  );
};

export default ManageConcerts;