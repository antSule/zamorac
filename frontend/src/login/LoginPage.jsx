import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import {useState} from "react";

const LoginPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleSpotifyLogin = () => {
        window.location.href='http://localhost:8080/oauth2/authorization/spotify';
    };

    const handleSubmit = async(event) => {
        event.preventDefault();

        try {
            const response = await fetch("/login", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams({
                    username: username,
                    password: password,
                }),
            });

            if (response.ok) {
                console.log("Login successful");
                sessionStorage.setItem('username', username);
                navigate("/");
            } else {
                console.error("Login failed");
                alert("Invalid username or password");
            }
        } catch(error) {
            console.error("Error during login", error);
            alert("An error occurred. Please try again.");
        }
    }
    return (
        <Container maxWidth="xs">
            <Paper elevation={10} sx={{ marginTop: 8, padding: 2}}>
                <Avatar sx={{
                    mx: "auto",
                    bgcolor: "secondary.main",
                    textAlign: "center",
                    mb: 1
                }}>
                    <LockOutlinedIcon/>
                </Avatar>
                <Typography component="h1" variant="h5" sx={{textAlign:"center"}}>
                    Login
                </Typography>
                <Box
                    component='form'
                    onSubmit={handleSubmit}
                    noValidate
                    sx={{mt: 1}}
                >
                    <TextField
                        placeholder="Username"
                        fullWidth
                        required
                        autoFocus
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        sx={{mb: 2}}
                    />
                    <TextField
                        placeholder="Password"
                        fullWidth
                        required
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <Button type="submit" variant="contained" fullWidth sx={{mt: 1}}>
                        Log In
                    </Button>
                    <Button onClick={handleSpotifyLogin} variant="contained" fullWidth sx={{mt: 1}}>
                        Log In With Spotify
                    </Button>
                    <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '5vh'}}>
                        <Button style={{fontSize: '13px', padding: '0px 10px'}}>
                            <RouterLink to="/registration">Don't have an account? Register here!</RouterLink>
                        </Button>
                    </div>
                </Box>
            </Paper>
        </Container>
    )
};

export default LoginPage;