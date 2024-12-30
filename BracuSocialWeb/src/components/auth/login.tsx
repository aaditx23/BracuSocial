import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export const Login: React.FC = () => {
  const [identifier, setIdentifier] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isSuccess, setIsSuccess] = useState(false);
  const navigate = useNavigate();  // Initialize navigate

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("");
    setIsSuccess(false);

    try {
      const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ identifier, password }),
      });

      const data = await response.json();

      if (response.ok) {
        setMessage("Login successful!");
        setIsSuccess(true);

        // Save email and studentId to LocalStorage
        if (data.user && data.user.email && data.user.studentId) {
          localStorage.setItem("email", data.user.email);
          localStorage.setItem("id", data.user.studentId);
          window.dispatchEvent(new Event("loginStatusChange"));
          
          // Redirect to home page
          navigate("/");
        }
      } else {
        setMessage(data.message || "Login failed.");
        setIsSuccess(false);
      }
    } catch (error) {
      console.error("Error during login:", error);
      setMessage("An error occurred. Please try again.");
      setIsSuccess(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleLogin}>
      <h2 className="text-xl font-bold">Login</h2>
      {message && (
        <p
          className={`text-sm text-center ${
            isSuccess ? "text-green-600" : "text-red-600"
          }`}
        >
          {message}
        </p>
      )}
      <div>
        <label htmlFor="identifier" className="block text-sm font-medium">
          Email or Student ID
        </label>
        <input
          type="text"
          id="identifier"
          className="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Enter email or student ID"
          value={identifier}
          onChange={(e) => setIdentifier(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="password" className="block text-sm font-medium">
          Password
        </label>
        <input
          type="password"
          id="password"
          className="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Enter your password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <button
        type="submit"
        className="w-full p-2 text-white bg-blue-600 rounded-md hover:bg-blue-700"
      >
        Login
      </button>
    </form>
  );
};
