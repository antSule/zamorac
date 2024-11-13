import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";

const Favourites = () => {

    const [followingArtists, setFollowingArtists] = useState([]);

    useEffect(() => {
        fetch("/following", {
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((data) => setFollowingArtists(data.artists.items))
            .catch((error) => console.error("Error fetching data:", error));
    }, []);

    const uniqueGenres = new Set();
    followingArtists.forEach(artist => {
        artist.genres.forEach(genre => uniqueGenres.add(genre));
    });
    const genresArray = Array.from(uniqueGenres);


    return(
        <>
            <section className=" h-wrapper">
                <div className="flexCenter paddings innerWidth h-container">
                    <img src="fakelogo.png" alt="logo" width={100}/>
                    <div className="centerText">
                        Favourites
                    </div>

                    <div className="flexCenter h-menu">
                    </div>
                </div>
            </section>

            <div style={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                minHeight: '100vh',
            }}>
                <h2>Favorite Artists</h2>
                <div>
                    {followingArtists.map((artist, index) => (
                        <div
                            key={index}
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                margin: '10px 0'
                            }}
                        >
                            <span style={{marginRight: '8px'}}>{index + 1}. {artist.name}</span>
                            {artist.images.length > 0 && (
                                <img
                                    src={artist.images[1].url}
                                    alt={`${artist.name} profile`}
                                    style={{
                                        width: '35px',
                                        height: '35px',
                                        objectFit: 'cover',
                                        marginLeft: '8px'
                                    }}
                                />
                            )}
                        </div>
                    ))}
                </div>
            </div>

            <div style={{marginTop: '20px', textAlign: 'center'}}>
                <h3>Favorite Genres</h3>
                <div>
                    {genresArray.map((genre, index) => (
                        <span
                            key={index}
                            style={{
                                display: 'inline-block',
                                margin: '5px',
                                padding: '5px 10px',
                                backgroundColor: '#eee',
                                borderRadius: '5px'
                            }}
                        >
                            {genre}
                        </span>
                    ))}
                </div>
            </div>
        </>
    )
};

export default Favourites;