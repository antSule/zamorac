import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import PersonIcon from '@mui/icons-material/Person';
import { Checkbox } from "@mui/material";
import {Link as RouterLink, useNavigate} from "react-router-dom";
import {useState} from "react";
import './registrationpage.css'

const ROLES = ["USER", "ARTIST"];

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
        alert("If the data is valid, we will send you a verification code to your email.");

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
        <body className="bodyReg">
        <div className="registration">
            <form onSubmit={handleSubmit} className="formReg" style={{ textAlign: "center" }}>
                <img src="/fakelogo.png" alt="logo" width={100} style={{ marginBottom: "20px" }} />

                <Typography
                    component="h1"
                    className="naslovReg"
                    variant="h5"
                    sx={{
                        textAlign: "center",
                        marginBottom: "20px",
                        fontWeight: 700,
                        fontSize: "xx-large",
                        fontFamily: "Poppins, sans-serif",
                    }}
                >
                    Registration
                </Typography>

                <TextField
    placeholder="Username"
    fullWidth
    required
    autoFocus
    value={username}
    onChange={(e) => setUsername(e.target.value)}
    sx={{
        mb: 2,
        fontFamily: '"Poppins", sans-serif',
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        '& .MuiOutlinedInput-root': {
            borderRadius: "20px",
            '& fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&:hover fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&.Mui-focused fieldset': {
                borderColor: "rgb(61, 193, 121)",
            },
        },
        '& .MuiInputBase-input': {
            fontSize: "14px",
            fontFamily: '"Poppins", sans-serif',
        },
    }}
/>
<TextField
    placeholder="Email"
    fullWidth
    required
    value={email}
    onChange={(e) => setEmail(e.target.value)}
    sx={{
        mb: 2,
        fontFamily: '"Poppins", sans-serif',
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        '& .MuiOutlinedInput-root': {
            borderRadius: "20px",
            '& fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&:hover fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&.Mui-focused fieldset': {
                borderColor: "rgb(61, 193, 121)",
            },
        },
        '& .MuiInputBase-input': {
            fontSize: "14px",
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
        overflow: "hidden",
        textOverflow: "ellipsis",
        width: "100%",
        '& .MuiOutlinedInput-root': {
            borderRadius: "20px",
            '& fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&:hover fieldset': {
                borderColor: "rgba(9, 51, 26, 0.937)",
            },
            '&.Mui-focused fieldset': {
                borderColor: "rgb(61, 193, 121)",
            },
        },
        '& .MuiInputBase-input': {
            fontSize: "14px",
            fontFamily: '"Poppins", sans-serif',
        },
    }}
/>

      <Typography
    variant="h6" 
    sx={{
        fontSize: "18px", 
        fontWeight:"700",
        color: "rgba(16, 125, 60, 0.937)", 
        mb: 0.5, 
        textAlign: "left",
    }}
>
    Select roles:
</Typography>

                {ROLES.map((role) => (
                    <FormControlLabel
                        key={role}
                        control={
                            <Checkbox
                            checked={selectedRoles.includes(role)}
                            onChange={() => handleRoleChange(role)}
                            sx={{
                                color: "rgba(9, 51, 26, 0.937)",
                                transform: "scale(1.5)",
                                '& .MuiSvgIcon-root': {
                                    fontSize: 28,
                                },
                                '&.Mui-checked': {
                                    color: "rgb(61, 193, 121)",
                                },
                            }}
                        />
                        
                        }
                        label={role}
                        sx={{
                            fontFamily: '"Poppins", sans-serif',
                        }}
                    />
                ))}
                <Button
                    type="submit"
                    variant="contained"
                    fullWidth
                    sx={{
                        mt: 2,
                        backgroundColor: "rgba(9, 51, 26, 0.937)",
                        color: "#ffffff",
                        border: "none",
                        padding: "10px 20px",
                        borderRadius: "20px",
                        cursor: "pointer",
                        fontSize: "14px",
                        transition: "background-color 0.3s ease",
                        '&:hover': {
                            backgroundColor: "#ffff",
                            color: "rgb(61, 193, 121)",
                            border: "1px solid rgb(61, 193, 121)",
                        },
                    }}
                >
                    Register
                </Button>
                <div style={{ display: "flex", justifyContent: "center", alignItems: "center", height: "5vh" }}>
                    <Button
                        component={RouterLink}
                        to="/"
                        style={{
                            fontSize: "13px",
                            padding: "0px 10px",
                            textDecoration: "underline",
                        }}
                    >
                        Already have an account? Log in.
                    </Button>
                </div>
            </form>
        </div>
        </body>
    );
};


export default RegistrationPage;
