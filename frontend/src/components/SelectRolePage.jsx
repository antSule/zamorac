import React, { useState } from "react";
import './selectrolepage.css';

const SelectRolePage = () => {
    const [selectedRoles, setSelectedRoles] = useState([]);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(false);

    const roles = ["USER", "ARTIST", "SPOTIFY", "ADMIN"];

    const handleRoleChange = (event) => {
        const { value, checked } = event.target;
        setSelectedRoles(checked
            ? [...selectedRoles, value]
            : selectedRoles.filter((role) => role !== value));
    };

    const handleSubmit = async () => {
        if (loading) return;

        if (selectedRoles.length === 0) {
            setError("You must select at least one role.");
            return;
        }

        setLoading(true);
        setError("");

        try {
            const query = new URLSearchParams({ roles: selectedRoles }).toString();
            const response = await fetch(`http://localhost:8080/auth/setrole?${query}`, {
                method: "GET",
                credentials: "include",
            });

            if (response.ok) {
                alert("Roles have been successfully assigned!");
                window.location.href = "/home";
            } else {
                const errorMessage = await response.text();
                setError(`An error occurred: ${errorMessage}`);
            }
        } catch (error) {
            setError("Failed to connect to the server.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="selectrole">
  <form>
    <img src="/fakelogo.png" alt="logo" width={100} />
    <h2 className="naslov">Select Role</h2>
    <p className="text">Please select your roles:</p>
    <div className="checkbox-container">
      {roles.map((role) => (
        <label key={role} className="checkbox">
          <input
            type="checkbox"
            value={role}
            onChange={handleRoleChange}
          />
          {role}
        </label>
      ))}
    </div>
    {error && <p style={{ color: "red" }}>{error}</p>}
    <button onClick={handleSubmit} disabled={loading} style={{ marginTop: "10px" }}>
      {loading ? "Setting roles..." : "Set Roles"}
    </button>
  </form>
</div>
    );
};

export default SelectRolePage;