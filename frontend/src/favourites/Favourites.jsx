import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import React, {useState} from "react";

const Favourites = () => {
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
        </>
    )
};

export default Favourites;