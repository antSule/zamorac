import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import PersonIcon from '@mui/icons-material/Person';
import { Checkbox } from "@mui/material";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import {useState} from "react";

const ROLES = ["USER", "ARTIST", "SPOTIFY", "ADMIN"];

const RegistrationPage = () => {
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("")
    const [selectedRoles, setSelectedRoles] = useState([]);
    const navigate = useNavigate();

    const handleRoleChange = (role) => {
            setSelectedRoles((prevRoles) =>
                prevRoles.includes(role)
                    ? prevRoles.filter((r) => r !== role)
                    : [...prevRoles, role]
            );
        };

    const handleSubmit = async(event) => {
        event.preventDefault();

        const userPayload = {
            username: username,
            email: email,
            password: password,
            role: selectedRoles,
        };

        try {
            const response = await fetch("http://localhost:8080/auth/signup", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userPayload),
            });

            if (response.ok) {
                navigate("/verify", { state: { email } });
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
                    <Typography variant="subtitle1" sx={{ mb: 1 }}>
                        Select roles:
                    </Typography>
                    {ROLES.map((role) => (
                        <FormControlLabel
                            key={role}
                            control={
                                <Checkbox
                                    checked={selectedRoles.includes(role)}
                                    onChange={() => handleRoleChange(role)}
                                />
                            }
                            label={role}
                        />
                    ))}
                    <Button type="submit" variant="contained" fullWidth sx={{mt: 1}}>
                        Register
                    </Button>
                    <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '5vh'}}>
                        <Button style={{fontSize: '13px', padding: '0px 10px'}}>
                            <RouterLink to="/">Already have an account? Log in.</RouterLink>
                        </Button>
                    </div>
                </Box>
            </Paper>
        </Container>
    )
};

export default RegistrationPage;
