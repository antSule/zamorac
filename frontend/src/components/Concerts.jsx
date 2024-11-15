import React, { useEffect, useState } from "react";
import axios from "axios";

const Concerts = () => {
    const [concert, setConcert] = useState([]);
    const [loading, setLoading] = useState(true); // Add loading state

    useEffect(() => {
        axios.get('https://ticketmestarbackend-yqpn.onrender.com/api/concerts/all', { withCredentials: true })
            .then(response => {
                // Check if response.data is an array
                if (Array.isArray(response.json().data)) {
                    setConcert(response.data);
                } else {
                    console.error('Expected an array of concerts but got:', response.data);
                }
            })
            .catch(error => console.error('Error fetching concerts: ', error))
            .finally(() => setLoading(false));  // Set loading to false once the request is complete
    }, []);

    return (
        <>
            <section className="h-wrapper">
                <div className="flexCenter paddings innerWidth h-container">
                    <img src="fakelogo.png" alt="logo" width={100} />
                    <div className="centerText">
                        Concerts
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
                <h2>Concerts Near You</h2>

                {loading ? (
                    <p>Loading concerts...</p>
                ) : concert.length > 0 ? (
                    <ul style={{ listStyleType: 'none', padding: 0, width: '80%' }}>
                        {concert.map((concert, index) => (
                            <li key={index} style={{
                                marginBottom: '20px',
                                border: '1px solid #ccc',
                                borderRadius: '8px',
                                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
                                padding: '16px',
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center',
                                backgroundColor: '#f9f9f9'
                            }}>
                                <div style={{ fontWeight: 'bold', fontSize: '1.2em' }}>{concert.performer}</div>
                                <div style={{ textAlign: 'right' }}>
                                    <div><strong>Date:</strong> {concert.date}</div>
                                    <div><strong>Time:</strong> {concert.time}</div>
                                </div>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No concerts found</p>
                )}
            </div>
        </>
    );
};

export default Concerts;
