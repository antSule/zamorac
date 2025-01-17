import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import './verifypage.css'; 

function VerifyPage() {
  const [code, setCode] = useState("");
  const [error, setError] = useState(null);
  const [message, setMessage] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email;

  const handleVerify = async (e) => {
    e.preventDefault();
    try {
        const response = await fetch("http://localhost:8080/auth/verify", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, verificationCode: code }),
        });

      if (response.ok) {
        alert("Successful registration!");
        navigate("/");
      } else {
        const data = await response.json();
        setError(data.message);
      }
    } catch (err) {
      setError("Something went wrong. Please try again.");
    }
  };

  const handleResend = async () => {
      setError(null);
      setMessage(null);
      try {
        const response = await fetch(`http://localhost:8080/auth/resend?email=${encodeURIComponent(email)}`, {
          method: "POST",
        });

        if (response.ok) {
          setMessage("Verification code has been resent to your email.");
        } else {
          const data = await response.json();
          setError(data.message);
        }
      } catch (err) {
        setError("Unable to resend verification code. Please try again.");
      }
  };

  return (
    <div className="center-container">
      <div className="form-wrapper">
      <img src="/fakelogo.png" alt="logo" width={100} style={{ marginBottom: "20px" }} />
        <h1 className="verifynaslov">Verify Email</h1>
        <form onSubmit={handleVerify}>
          <input
            className="inputV"
            type="text"
            placeholder="Enter verification code"
            value={code}
            onChange={(e) => setCode(e.target.value)}
            required
          />
          <button className="buttonV" type="submit">Verify</button>
        </form>
        <button className="buttonV" onClick={handleResend} style={{ marginTop: "10px" }}>
          Resend Verification Code
        </button>
        {error && <p style={{ color: "red" }}>{error}</p>}
        {message && <p style={{ color: "green" }}>{message}</p>}
      </div>
    </div>
  );
}

export default VerifyPage;
