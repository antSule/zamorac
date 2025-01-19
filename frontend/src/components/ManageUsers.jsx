import React, { useState, useEffect } from 'react';
import axios from 'axios';
import "./usersManager.css";

const ManageUsers = () => {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const [selectedUserId, setSelectedUserId] = useState('');
    const [newRoles, setNewRoles] = useState([]);
    const [currentRoles, setCurrentRoles] = useState([]);
    const [showDeleteModal, setShowDeleteModal] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);
    const [hasAdminRole, setHasAdminRole] = useState(false);
    const [searchParams, setSearchParams] = useState({
        username: '',
        roles: [],
        provider: null
    });
    const [filteredUsers, setFilteredUsers] = useState([]);

    const roles = ["USER", "ARTIST", "SPOTIFY", "ADMIN"];
    const providers = ["GOOGLE", "SPOTIFY", "JWT"];

    const searchUsers = async (headers, params) => {
        try {
            const response = await axios.post('/admin/search', params, {withCredentials:true, headers});
            setFilteredUsers(response.data);
        } catch(err){
            setError(err);
            console.error(err);
        }
    };

    const fetchUsers = async (headers) => {
        try {
            const response = await axios.get('/admin/all', { withCredentials:true,headers });
            setUsers(response.data);
            setFilteredUsers(response.data);
        } catch (err) {
            setError('Error fetching users.');
            console.error(err);
        }
    };

    const fetchCurrentRoles = async (userId, headers) => {
        try {
            const response = await axios.get(`/admin/userroles?userId=${userId}`, {withCredentials:true, headers});
            setCurrentRoles(response.data);
            setNewRoles(response.data);
        } catch(err){
            setError('Error fetching user roles.');
            console.log(err);
        }
    };

    const deleteUser = async (userId, headers) => {
        try {
            await axios.get(`/admin/delete?userId=${userId}`, { withCredentials:true,headers });
            setSuccessMessage('User deleted successfully.');
            fetchUsers(headers);
            setShowDeleteModal(false);
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

    const handleProviderChange = (e) => {
        setSearchParams((prevParams) => ({ ...prevParams, provider: e.target.value }));
    };

    
    const handleSearchChange = (e) => {
        setSearchParams((prevParams) => ({ ...prevParams, username: e.target.value }));
    };

    const changeUserRole = async (headers) => {
        if (!selectedUserId || newRoles.length === 0) return;

        try {
            const rolesParam = newRoles.join(',');
            const url = `http://localhost:8080/admin/roles?userId=${selectedUserId}&roles=${encodeURIComponent(rolesParam)}`;
            console.log(url);

            await axios.get(url, { withCredentials:true,headers });

            setSuccessMessage('User roles updated successfully.');
            fetchUsers(headers);
        } catch (err) {
            setError('Error changing user role.');
            console.error(err);
        }
    };

    const handleDeleteClick = (userId) => {
        setUserToDelete(userId);
        setShowDeleteModal(true);
    };

    const handleConfirmDelete = () => {
        const token = localStorage.getItem("token");
        const headers = token
            ? {
                 'Content-Type': 'application/json',
                 'Authorization': `Bearer ${token}`,
               }
             : undefined;
            deleteUser(userToDelete, headers);
            setShowDeleteModal(false);
    };

    const handleCancelDelete = () => {
        setShowDeleteModal(false);
    };

    const handleSearchSubmit = () => {
        const token = localStorage.getItem("token");
        const headers = token
            ? {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            }
            : undefined;
        searchUsers(headers, searchParams);

        setSearchParams({
            username: '',
            roles: [],
            provider: null
        });
    };

    useEffect(() => {
       const token = localStorage.getItem("token");
       const headers = token
         ? {
             'Content-Type': 'application/json',
             'Authorization': `Bearer ${token}`,
           }
         : undefined;

        axios.get('http://localhost:8080/user-info', {withCredentials: true, headers})
        .then((response) => {
            const userRoles = response.data.roles || [];
            if(userRoles.includes('ADMIN')){
                setHasAdminRole(true);
                fetchUsers(headers);
            } else {
                setHasAdminRole(false);
            }
        })
        .catch((err) => {
            setError('Error fetching user roles.');
            console.log(err);
        })
    }, []);

    useEffect(() => {
        if (selectedUserId) {
            const token = localStorage.getItem("token");
            const headers = token
                ? {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                }
                : undefined;
            fetchCurrentRoles(selectedUserId, headers);
        }
    }, [selectedUserId]);

    if(!hasAdminRole){
        return (
            <div className="no-access-container">
                <div className="no-access-message">
                    <h2>⚠️ Access Denied</h2>
                    <p>You do not have permission to access this page.</p>
                </div>
            </div>
        );
    }

    return (
        <body className='bodyMU'>
        <div>
            <h1>Manage Users</h1>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {successMessage && <p style={{ color: 'green' }}>{successMessage}</p>}
            <h2>Search Users</h2>
            <div>
                <input
                    type="text"
                    placeholder="Search by Username"
                    value={searchParams.username}
                    onChange={handleSearchChange}
                />
                <div>
                    <p>Select Role:</p>
                    {roles.map((role) => (
                        <label key={role}>
                            <input
                                type="radio"
                                name="role"
                                value={role}
                                checked={searchParams.roles.includes(role)}
                                onChange={(e) => setSearchParams({ ...searchParams, roles: [e.target.value] })}
                            />
                            {role}
                        </label>
                    ))}
                </div>
                <div>
                    <p>Select Provider:</p>
                    {providers.map((provider) => (
                        <label key={provider}>
                            <input
                                type="radio"
                                name="provider"
                                value={provider}
                                checked={searchParams.provider === provider}
                                onChange={handleProviderChange}
                            />
                            {provider}
                        </label>
                    ))}
                </div>
                <button onClick={handleSearchSubmit}>Search</button>
            </div>

            <h2>Users List</h2>
            <ul>
                {filteredUsers.length > 0 ?(
                    filteredUsers.map((user) => (
                    <li key={user.id}>
                        <span>{user.username} ({user.email})</span>
                        <button onClick={() => handleDeleteClick(user.id)}>Delete</button>
                        <button onClick={() => setSelectedUserId(user.id)}>Change Role</button>
                    </li>
                ))
            ):(<p>No users found.</p>)}
            </ul>

            {selectedUserId && (
                <div>
                    <h3>Change Role for User ID: {selectedUserId}</h3>
                    <p>Current Roles:</p>
                    <ul>
                        {currentRoles.map((role) => (
                            <li key={role}>{role}</li>
                        ))}
                    </ul>
                    <p>Select roles for the user:</p>
                    {roles.map((role) => (
                        <div key={role}>
                            <label>
                                <input
                                    type="checkbox"
                                    value={role}
                                    onChange={handleRoleChange}
                                    checked={newRoles.includes(role)}
                                />
                                {role}
                            </label>
                        </div>
                    ))}
                    <button onClick={changeUserRole}>Update Role</button>
                </div>
            )}

            {showDeleteModal && (
                <div className="delete-modal">
                    <div className="modal-content">
                        <h4> Are you sure you want to delete this user? </h4>
                        <button onClick={handleConfirmDelete}>Yes</button>
                        <button onClick={handleCancelDelete}>No</button>
                    </div>
                </div>
            )}
        </div>
    </body>    
    );
};

export default ManageUsers;