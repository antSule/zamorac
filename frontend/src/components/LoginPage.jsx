import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import {useState} from "react";

const LoginPage = () => {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleGoogleLogin = () => {
        window.location.href='http://localhost:8080/oauth2/authorization/google';
    }

    const handleSubmit = async(event) => {
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
                <Button component={RouterLink} to="/register" fullWidth sx={{ mt: 2, textTransform: "none" }}>
                  Don't have an account? Sign Up here!
                </Button>
                <Button onClick={handleGoogleLogin} variant="contained" fullWidth sx={{mt: 1}}>
                    Log In With Google
                </Button>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                <TextField
                    placeholder="Email"
                    fullWidth
                    required
                    autoFocus
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    sx={{ mb: 2 }}
                />
                <TextField
                    placeholder="Password"
                    fullWidth
                    required
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <Button type="submit" variant="contained" fullWidth sx={{ mt: 1 }}>
                    Log In
                </Button>
            </Box>
          </Paper>
        </Container>
    )
};

export default LoginPage;
