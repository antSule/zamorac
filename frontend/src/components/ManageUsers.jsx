import React, { useState, useEffect } from 'react';
import axios from 'axios';

const ManageUsers = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [selectedUserId, setSelectedUserId] = useState('');
    const [newRoles, setNewRoles] = useState([]);
    const roles = ["USER", "ARTIST", "SPOTIFY", "ADMIN"];

    const fetchUsers = async () => {
        try {
            const response = await axios.get('/admin/all', {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            setUsers(response.data);
        } catch (err) {
            setError('Error fetching users.');
            console.error(err);
        }
    };

    const deleteUser = async (userId) => {
        try {
            await axios.get(`/admin/delete?userId=${userId}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            setSuccessMessage('User deleted successfully.');
            fetchUsers();
        } catch (err) {
            setError('Error deleting user.');
            console.error(err);
        }
    };

    const handleRoleChange = (e) => {
        const role = e.target.value;
        setNewRoles((prevRoles) =>
            prevRoles.includes(role)
                ? prevRoles.filter((r) => r !== role)
                : [...prevRoles, role]
        );
    };

    const changeUserRole = async () => {
        if (!selectedUserId || newRoles.length === 0) return;

        try {
            const rolesParam = newRoles.join(',');
            const url = `http://localhost:8080/admin/roles?userId=${selectedUserId}&roles=${encodeURIComponent(rolesParam)}`;
            console.log(url);

            await axios.get(url, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            setSuccessMessage('User roles updated successfully.');
            fetchUsers();
        } catch (err) {
            setError('Error changing user role.');
            console.error(err);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return (
        <div>
            <h1>Manage Users</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}

            <h2>Users List</h2>
            <ul>
                {users.map((user) => (
                    <li key={user.id}>
                        <span>{user.username} ({user.email})</span>
                        <button onClick={() => deleteUser(user.id)}>Delete</button>
                        <button onClick={() => setSelectedUserId(user.id)}>Change Role</button>
                    </li>
                ))}
            </ul>

            {selectedUserId && (
                <div>
                    <h3>Change Role for User ID: {selectedUserId}</h3>
                    <p>Select roles for the user:</p>
                    {roles.map((role) => (
                        <div key={role}>
                            <label>
                                <input
                                    type="checkbox"
                                    value={role}
                                    onChange={handleRoleChange}
                                />
                                {role}
                            </label>
                        </div>
                    ))}
                    <button onClick={changeUserRole}>Update Role</button>
                </div>
            )}
        </div>
    );
};

export default ManageUsers;