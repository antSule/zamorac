import React, { useEffect, useState } from "react";
import axios from "axios";

const Favourites = () => {
  const [followingArtists, setFollowingArtists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [hasAccess, setHasAccess] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem("token");
    const headers = token
      ? {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        }
      : undefined;

    axios
      .get("http://localhost:8080/user-info", { withCredentials: true, headers })
      .then((response) => {
        const userRoles = response.data.roles || [];
        if (userRoles.includes('SPOTIFY')) {
          setHasAccess(true);
          fetchFollowingArtists(headers);
        } else {
          setHasAccess(false);
        }
      })
      .catch((error) => {
        console.error("Error fetching user roles: ", error);
        setHasAccess(false);
      });
  }, []);

  const fetchFollowingArtists = async (headers) => {
    try {
      const response = await axios.get("http://localhost:8080/following", { withCredentials: true, headers });
      setFollowingArtists(response.data.artists.items);
    } catch (error) {
      console.error("Error fetching following artists: ", error);
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
    <div id="favourites-container">
      {loading ? (
        <p>Loading favourites...</p>
      ) : followingArtists.length > 0 ? (
        followingArtists.map((artist, index) => (
          <div className="artist" key={index}>
            <div className="artist-image">
              <img
                src={artist.images[1]?.url || "/fakelogo.png"}
                alt={artist.name || "Artist"}
              />
            </div>
            <div className="artist-details">
              <h3>{artist.name}</h3>
              <p>
                <strong>Genres:</strong> {artist.genres.join(", ")}
              </p>
            </div>
          </div>
        ))
      ) : (
        <p>No favourite artists found.</p>
      )}
    </div>
  );
};

export default Favourites;