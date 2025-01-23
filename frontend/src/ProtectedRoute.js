import React from 'react';
import { Navigate } from 'react-router-dom';
import axios from 'axios';
import { useState, useEffect } from 'react';

const ProtectedRoute = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);
    const BACKEND_URL = process.env.REACT_APP_BACKEND_URL;
    const FRONTEND_URL = process.env.REACT_APP_FRONTEND_URL;

    useEffect(() => {
        axios
            .get(`${BACKEND_URL}/user-info`, { withCredentials: true })
            .then((response) => {
                if (response.data) {
                    setIsAuthenticated(true);
                } else {
                    setIsAuthenticated(false);
                }
            })
            .catch((error) => {
                console.error('Authentication check failed:', error);
                setIsAuthenticated(false);
            })
            .finally(() => {
                setIsLoading(false);
            });
    }, []);

    if (isLoading) {
        return <p>Loading...</p>;
    }

    return isAuthenticated ? children : <Navigate to="/" />;
};

export default ProtectedRoute;
