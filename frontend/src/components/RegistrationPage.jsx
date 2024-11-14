import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import PersonIcon from '@mui/icons-material/Person';
import { CheckBox } from "@mui/icons-material";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import {useState} from "react";

const RegistrationPage = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("")
    const navigate = useNavigate();

    const handleSubmit = async(event) => {
        event.preventDefault();

        const userPayload = {
            username: username,
            email: email,
            password: password,
        };

        try {
            const response = await fetch("/api/registration", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(userPayload),
            });

            if (response.ok) {
                console.log('Registration successful');
                navigate("/login");
            } else{
                console.error('Registration failed');
            }
        } catch(error) {
            console.log('Error during registration', error);
        }
    };

    return (
        <Container maxWidth="xs">
            <Paper elevation={10} sx={{ marginTop: 8, padding: 2}}>
                <Avatar sx={{
                    mx: "auto",
                    bgcolor: "secondary.main",
                    textAlign: "center",
                    mb: 1
                }}>
                    <PersonIcon/>
                </Avatar>
                <Typography component="h1" variant="h5" sx={{textAlign:"center"}}>
                    Registration
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
                        placeholder="Email"
                        fullWidth
                        required
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
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
                        Register
                    </Button>
                    <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '5vh'}}>
                        <Button style={{fontSize: '13px', padding: '0px 10px'}}>
                            <RouterLink to="/login">Already have an account? Log in.</RouterLink>
                        </Button>
                    </div>
                </Box>
            </Paper>
        </Container>
    )
};

export default RegistrationPage;