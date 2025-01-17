import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { useState } from "react";
import "./loginpage.css";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    const handleSpotifyLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/spotify';
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await fetch("http://localhost:8080/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email,
                    password: password,
                }),
            });

            if (!response.ok) {
                throw new Error("Invalid email or password");
            }

            const data = await response.json();
            console.log("Response Data:", data);
            const token = data.token;

            localStorage.setItem("token", token);
            navigate("/home");
        } catch (error) {
            console.error("Login error:", error.message);
            alert("Login failed: " + error.message);
        }
    };

    return (
        <body className="bodyLog">
        <div className="login">
            <form onSubmit={handleSubmit} className="formLog">
                <img src="/fakelogo.png" alt="logo" width={100} />

                <Typography
                    component="h1"
                    className="naslovLog"
                    variant="h5"
                    sx={{
                        textAlign: 'center',
                        marginBottom: '20px',
                        fontWeight: 700,
                        fontSize: 'xx-large',
                        fontFamily: 'Poppins, sans-serif',
                    }}
                >
                    Login
                </Typography>

                <Button
                    component={RouterLink}
                    to="/register"
                    fullWidth
                    sx={{
                        mt: 2,
                        textTransform: 'none',
                        backgroundColor: 'rgba(9, 51, 26, 0.937)',
                        color: '#ffffff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '20px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        transition: 'background-color 0.3s ease',
                        '&:hover': {
                            backgroundColor: '#ffff',
                            color: 'rgb(61, 193, 121)',
                            border: '1px solid rgb(61, 193, 121)',
                        },
                    }}
                >
                    Don't have an account? Sign Up here!
                </Button>

                <Button
                    onClick={handleGoogleLogin}
                    variant="contained"
                    fullWidth
                    sx={{
                        mt: 1,
                        backgroundColor: 'rgba(9, 51, 26, 0.937)',
                        color: '#ffffff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '20px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        transition: 'background-color 0.3s ease',
                        '&:hover': {
                            backgroundColor: '#ffff',
                            color: 'rgb(61, 193, 121)',
                            border: '1px solid rgb(61, 193, 121)',
                        },
                    }}
                >
                    Log In With Google
                </Button>

                <Button
                    onClick={handleSpotifyLogin}
                    variant="contained"
                    fullWidth
                    sx={{
                        mt: 1,
                        backgroundColor: 'rgba(9, 51, 26, 0.937)',
                        color: '#ffffff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '20px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        transition: 'background-color 0.3s ease',
                        '&:hover': {
                            backgroundColor: '#ffff',
                            color: 'rgb(61, 193, 121)',
                            border: '1px solid rgb(61, 193, 121)',
                        },
                    }}
                >
                    LOG IN WITH SPOTIFY
                </Button>

                <TextField
                    placeholder="Email"
                    fullWidth
                    required
                    autoFocus
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    sx={{
                        mb: 2,
                        mt: 2,
                        fontFamily: '"Poppins", sans-serif',
                        '& .MuiOutlinedInput-root': {
                            borderRadius: '20px',
                            overflow: 'hidden',
                            '& fieldset': {
                                borderColor: 'rgba(9, 51, 26, 0.937)',
                                borderRadius: '20px',
                            },
                            '&:hover fieldset': {
                                borderColor: 'rgba(9, 51, 26, 0.937)',
                            },
                            '&.Mui-focused fieldset': {
                                borderColor: 'rgb(61, 193, 121)',
                            },
                        },
                        '& .MuiInputBase-input': {
                            fontSize: '14px',
                            fontFamily: '"Poppins", sans-serif',
                        },
                    }}
                />

                <TextField
                    placeholder="Password"
                    fullWidth
                    required
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    sx={{
                        mb: 2,
                        fontFamily: '"Poppins", sans-serif',
                        '& .MuiOutlinedInput-root': {
                            borderRadius: '20px',
                            overflow: 'hidden',
                            '& fieldset': {
                                borderColor: 'rgba(9, 51, 26, 0.937)',
                                borderRadius: '20px',
                            },
                            '&:hover fieldset': {
                                borderColor: 'rgba(9, 51, 26, 0.937)',
                            },
                            '&.Mui-focused fieldset': {
                                borderColor: 'rgb(61, 193, 121)',
                            },
                        },
                        '& .MuiInputBase-input': {
                            fontSize: '14px',
                            fontFamily: '"Poppins", sans-serif',
                        },
                    }}
                />

                <Button
                    type="submit"
                    variant="contained"
                    fullWidth
                    sx={{
                        backgroundColor: 'rgba(9, 51, 26, 0.937)',
                        color: '#ffffff',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '20px',
                        cursor: 'pointer',
                        fontSize: '14px',
                        transition: 'background-color 0.3s ease',
                        '&:hover': {
                            backgroundColor: '#ffff',
                            color: 'rgb(61, 193, 121)',
                            border: '1px solid rgb(61, 193, 121)',
                        },
                    }}
                >
                    Log In
                </Button>
            </form>
        </div>
        </body>
    );
};

export default LoginPage;