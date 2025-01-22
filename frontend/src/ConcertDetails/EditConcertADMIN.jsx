import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { Link as RouterLink } from "react-router-dom";

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
                        src="/fakelogo.png"
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
                        Edit Concert Admin
                      </div>
                    </div>
                  </section>
      <form onSubmit={handleFormSubmit}>
        <input
          type="text"
          name="event"
          value={formData.event || ""}
          onChange={handleInputChange}
          placeholder="Event Name"
          required
        />
        <input
          type="text"
          name="performer"
          value={formData.performer || ""}
          onChange={handleInputChange}
          placeholder="Performer Name"
          required
        />
        <input
          type="text"
          name="city"
          value={formData.city || ""}
          onChange={handleInputChange}
          placeholder="City"
          required
        />
        <input
          type="date"
          name="date"
          value={formData.date || ""}
          onChange={handleInputChange}
          required
        />
        <input
          type="time"
          name="time"
          value={formData.time || ""}
          onChange={handleInputChange}
          required
        />
        <input
          type="text"
          name="venue"
          value={formData.venue || ""}
          onChange={handleInputChange}
          placeholder="Venue"
          required
        />
        <input
          type="text"
          name="latitude"
          value={formData.latitude || ""}
          onChange={handleInputChange}
          placeholder="Latitude"
        />
        <input
          type="text"
          name="longitude"
          value={formData.longitude || ""}
          onChange={handleInputChange}
          placeholder="Longitude"
        />
        <input
          type="text"
          name="url"
          value={formData.url || ""}
          onChange={handleInputChange}
          placeholder="URL"
        />
        <input
          type="text"
          name="imageUrl"
          value={formData.imageUrl || ""}
          onChange={handleInputChange}
          placeholder="Image URL"
        />
        <button type="submit">Update Concert</button>
      </form>
    </div>
  );
};

export default EditConcertADMIN;
