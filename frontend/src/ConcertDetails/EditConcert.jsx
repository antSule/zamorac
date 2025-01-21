import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import './editConcert.css'

const EditConcertADMIN = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    date: "",
    time: "",
    performer: "",
    performerId: "",
    venue: "",
    latitude: "",
    longitude: "",
    url: "",
    city: "",
    event: "",
    imageUrl: "",
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchConcert = async () => {
      try {
        const token = localStorage.getItem("token");
        const headers = token
          ? { Authorization: `Bearer ${token}` }
          : undefined;

        const response = await axios.get(
          `http://localhost:8080/concerts/concert-info?id=${id}`,
          { withCredentials: true, headers }
        );

        console.log("Fetched concert data:", response.data);

        setFormData({
          date: response.data.date || "",
          time: response.data.time || "",
          performer: response.data.performer || "",
          performerId: response.data.performerId ||"",
          venue: response.data.venue || "",
          latitude: response.data.latitude || "",
          longitude: response.data.longitude || "",
          url: response.data.url || "",
          city: response.data.city || "",
          event: response.data.event || "",
          imageUrl: response.data.imageUrl || "",
        });
      } catch (err) {
        console.error("Error fetching concert data:", err.response?.data || err.message);
        setError("Error fetching concert data.");
      } finally {
        setLoading(false);
      }
    };

    fetchConcert();
  }, [id]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
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
        `http://localhost:8080/concerts/edit-concert?id=${id}`,
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
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
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
  
        <label className="labelECA" htmlFor="latitude">Latitude</label>
        <input
          className="inputECA"
          type="text"
          name="latitude"
          id="latitude"
          value={formData.latitude || ""}
          onChange={handleInputChange}
        />
  
        <label className="labelECA" htmlFor="longitude">Longitude</label>
        <input
          className="inputECA"
          type="text"
          name="longitude"
          id="longitude"
          value={formData.longitude || ""}
          onChange={handleInputChange}
        />
  
        <label className="labelECA" htmlFor="url">URL</label>
        <input
          className="inputECA"
          type="text"
          name="url"
          id="url"
          value={formData.url || ""}
          onChange={handleInputChange}
        />
  
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
  );
};

export default EditConcertADMIN;
