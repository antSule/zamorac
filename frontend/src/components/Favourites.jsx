import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const Favourites = () => {
  const [followingArtists, setFollowingArtists] = useState([]);
  const [genresArray, setGenresArray] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch("http://localhost:8080/spotify/me/following", {
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to fetch following artists");
        }
        return response.json();
      })
      .then((data) => {
        const artists = data.artists.items;
        setFollowingArtists(artists);

        const genresSet = new Set();
        artists.forEach((artist) => {
          artist.genres.forEach((genre) => genresSet.add(genre));
        });
        setGenresArray(Array.from(genresSet));
      })
      .catch((error) => console.error("Error fetching data:", error));
  }, []);

  const handleSeeConcerts = (artistId) => {
    navigate(`/concerts/artist/${artistId}`);
  };

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
          <img
            src="fakelogo.png"
            alt="logo"
            width={100}
            style={{
              position: "absolute",
              left: "20px",
            }}
          />
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
                  onClick={() => handleSeeConcerts(artist.id)}
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