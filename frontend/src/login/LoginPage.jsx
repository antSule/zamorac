import { Avatar, Box, Button, Container, FormControlLabel, Grid, Link, Paper, TextField, Typography } from "@mui/material";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import { CheckBox } from "@mui/icons-material";
import { Link as RouterLink } from "react-router-dom";

const LoginPage = () => {
    const handleSubmit = () => console.log("Log in!")
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
                    sx={{ mt: 1 }}
                >
                    <TextField
                        placeholder="Email"
                        fullWidth
                        required
                        autoFocus
                        sx={{mb: 2}}
                    />
                    <TextField
                        placeholder="Password"
                        fullWidth
                        required
                        type="password"
                    />
                    <Button type="submit" variant="contained" fullWidth sx={{mt: 1}}>
                        Log In
                    </Button>
                    <Button type="submit" variant="contained" fullWidth sx={{mt: 1}}>
                        Log In With Spotify
                    </Button>
                </Box>
            </Paper>
        </Container>
    )
};

export default LoginPage;