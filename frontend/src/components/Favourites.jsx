import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from 'axios';
import { Link as RouterLink } from "react-router-dom";

const Favourites = () => {
  const [followingArtists, setFollowingArtists] = useState([]);
  const [favoriteArtists, setFavoriteArtists] = useState([])
  const [genresArray, setGenresArray] = useState([]);
  const [hasAccess, setHasAccess] = useState(false);
  const navigate = useNavigate();
  const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
  const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

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
              if (userRoles.includes("USER") || userRoles.includes("ADMIN") || userRoles.includes("ARTIST") || userRoles.includes("SPOTIFY")) {
                  setHasAccess(true);
                  fetchFavorites(headers);
                  if(userRoles.includes("SPOTIFY")){
                    fetchSpotifyFollowing(headers);
                  }
              } else {
                  setHasAccess(false);
              }
          })
          .catch((err) => {
              console.error(err);
          })
  }, []);


  const fetchFavorites = async (headers) => {
    try {
      const response = await axios.get(`${BACKEND_URL}/favorites`, { withCredentials: true, headers });
      setFavoriteArtists(response.data);
      console.log(response.data); 
    } catch (error) {
      console.error("Error fetching favorite artists: ", error);
    }
  };

  const fetchSpotifyFollowing = async (headers) => {
    try {
      const response = await fetch(`${BACKEND_URL}/spotify/me/following`, {
        method: "GET",
        headers: headers,
        credentials: "include",
      });
      if (!response.ok) {
        throw new Error("Failed to fetch Spotify following artists");
      }
      const data = await response.json();
      const artists = data.artists.items;
      setFollowingArtists(artists);

      const genresSet = new Set();
      artists.forEach((artist) => {
        artist.genres.forEach((genre) => genresSet.add(genre));
      });
      setGenresArray(Array.from(genresSet));
    } catch (error) {
      console.error("Error fetching Spotify following artists: ", error);
    }
  };

const handleSeeConcerts = async (artistName, performerId) => {
  const token = localStorage.getItem("token");
  const headers = token
    ? {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }
    : {};
    try {
        console.log("artistName " + artistName);
        const ticketMasterConcertsPromise = axios.get(
          `${BACKEND_URL}/concerts/concerts?artist=${encodeURIComponent(artistName)}`,
          { headers, withCredentials: true }
        );

        const inAppConcertsPromise = performerId
          ? axios.get(`${BACKEND_URL}/concerts/all`, {
              headers,
              withCredentials: true,
            })
          : Promise.resolve({ data: [] });

        const [ticketMasterConcertsResponse, inAppConcertsResponse] = await Promise.all([
          ticketMasterConcertsPromise,
          inAppConcertsPromise
        ]);

        const ticketMasterConcerts = ticketMasterConcertsResponse.data;
        const inAppConcerts = inAppConcertsResponse.data;

        let filteredInAppConcerts = performerId
          ? inAppConcerts.filter(concert => concert.performerId === performerId)
          : [];

        let allConcerts = [...filteredInAppConcerts, ...ticketMasterConcerts];

        if (allConcerts.length === 0) {
          alert("No concerts found for this artist or user.");
        } else {
          localStorage.setItem("concerts", JSON.stringify(allConcerts));
          navigate("/ConcertDetails", { state: { concerts: allConcerts } });
        }
      } catch (error) {
        console.error("Error fetching concerts:", error);
        alert(error.message);
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
    <div>
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
            Favourites
          </div>
        </div>
      </section>

      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "flex-start",
          padding: "40px",
          gap: "40px",
        }}
      >
        {/* Kartica: Favourite Artists */}
        <div
          style={{
            width: "80%",
            maxWidth: "600px",
            backgroundColor: "white",
            borderRadius: "20px",
            boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
            padding: "20px",
            textAlign: "center",
          }}
        >
          <h2
            style={{
              fontSize: "4.5rem",
              marginBottom: "20px",
              color: "#02310B",
            }}
          >
            Favourite Artists
          </h2>
          <div>
            {followingArtists.map((artist, index) => (
              <div
                key={artist.id}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  margin: "15px 0",
                  fontSize: "2rem",
                  color: "#054210",
                }}
              >
                <span style={{ marginRight: "8px" }}>
                  {index + 1}. {artist.name}
                </span>
                {artist.images && artist.images.length > 0 && (
                  <img
                    src={artist.images[1]?.url || artist.images[0].url}
                    alt={`${artist.name} profile`}
                    style={{
                      width: "60px",
                      height: "60px",
                      objectFit: "cover",
                      marginLeft: "8px",
                      borderRadius: "50%",
                    }}
                  />
                )}
                <button
                  onClick={() => handleSeeConcerts(artist.name)}
                  style={{
                    marginLeft: "15px",
                    padding: "8px 15px",
                    backgroundColor: "#2d6a4f",
                    color: "white",
                    border: "none",
                    borderRadius: "5px",
                    cursor: "pointer",
                    fontSize: "1.5rem",
                    transition: "background-color 0.3s",
                  }}
                  onMouseEnter={(e) =>
                    (e.target.style.backgroundColor = "#218838")
                  }
                  onMouseLeave={(e) =>
                    (e.target.style.backgroundColor = "#2d6a4f")
                  }
                >
                  See concerts
                </button>
              </div>
            ))}
          </div>
        </div>

        {/* In-App Favorite Artists Section */}
        {favoriteArtists.length > 0 && (
          <div
            style={{
              width: "80%",
              maxWidth: "600px",
              backgroundColor: "white",
              borderRadius: "20px",
              boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
              padding: "20px",
              textAlign: "center",
            }}
          >
            <h2
              style={{
                fontSize: "4.5rem",
                marginBottom: "20px",
                color: "#02310B",
              }}
            >
              In-App Favorite Artists
            </h2>
            <div>
              {favoriteArtists.map((artist, index) => (
                <div
                  key={artist.id}
                  style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    margin: "15px 0",
                    fontSize: "2rem",
                    color: "#054210",
                  }}
                >
                  <span style={{ marginRight: "8px" }}>
                    {index + 1}. {artist.username}
                  </span>
                  {artist.images && artist.images.length > 0 && (
                    <img
                      src={artist.images[1]?.url || artist.images[0].url}
                      alt={`${artist.name} profile`}
                      style={{
                        width: "60px",
                        height: "60px",
                        objectFit: "cover",
                        marginLeft: "8px",
                        borderRadius: "50%",
                      }}
                    />
                  )}
                  <button
                    onClick={() => handleSeeConcerts(artist.username, artist.id)}
                    style={{
                      marginLeft: "15px",
                      padding: "8px 15px",
                      backgroundColor: "#2d6a4f",
                      color: "white",
                      border: "none",
                      borderRadius: "5px",
                      cursor: "pointer",
                      fontSize: "1.5rem",
                      transition: "background-color 0.3s",
                    }}
                    onMouseEnter={(e) =>
                      (e.target.style.backgroundColor = "#218838")
                    }
                    onMouseLeave={(e) =>
                      (e.target.style.backgroundColor = "#2d6a4f")
                    }
                  >
                    See concerts
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Kartica: Favourite Genres */}
        <div
          style={{
            width: "80%",
            maxWidth: "600px",
            backgroundColor: "white",
            borderRadius: "20px",
            boxShadow: "0 4px 10px rgba(0, 0, 0, 0.1)",
            padding: "15px",
            textAlign: "center",
          }}
        >
          <h3
            style={{
              fontSize: "4.5rem",
              marginBottom: "20px",
              color: "#02310B",
            }}
          >
            Favourite Genres
          </h3>
          <div>
            {genresArray.map((genre, index) => (
              <span
                key={index}
                style={{
                  display: "inline-block",
                  margin: "13px",
                  padding: "10px 20px",
                  backgroundColor: "#eee",
                  borderRadius: "5px",
                  fontSize: "1.8rem",
                  color: "#054210",
                }}
              >
                {genre}
              </span>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Favourites;