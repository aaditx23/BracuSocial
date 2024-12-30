import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export const Register: React.FC = () => {
  const [studentId, setStudentId] = useState("");
  const [email, setEmail] = useState("");
  const [name, setName] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isSuccess, setIsSuccess] = useState(false); // Track success state
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage("");
    setIsSuccess(false); // Reset success on every attempt

    try {
      const response = await fetch("https://bracusocial-web-backend-b6x213chy-aaditx23s-projects.vercel.app/api/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ studentId, email, name, password }),
      });

      const data = await response.json();

      if (response.ok) {
        setMessage("Registration successful!");
        setIsSuccess(true);

        // Save email and studentId to localStorage
        localStorage.setItem("email", email);
        localStorage.setItem("id", studentId);

        // Dispatch custom event to update NavBar immediately
        window.dispatchEvent(new Event("loginStatusChange"));

        // Navigate to homepage
        navigate("/");
      } else {
        setMessage(data.message || "Registration failed.");
        setIsSuccess(false);
      }
    } catch (error) {
      console.error("Error during registration:", error);
      setMessage("An error occurred. Please try again.");
      setIsSuccess(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleRegister}>
      <h2 className="text-xl font-bold">Register</h2>
      {message && (
        <p className={`text-sm text-center ${isSuccess ? "text-green-600" : "text-red-600"}`}>
          {message}
        </p>
      )}
      <div>
        <label htmlFor="studentId" className="block text-sm font-medium">
          Student ID
        </label>
        <input
          type="text"
          id="studentId"
          className="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Enter your student ID"
          value={studentId}
          onChange={(e) => setStudentId(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="email" className="block text-sm font-medium">
          Email
        </label>
        <input
          type="email"
          id="email"
          className="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Enter your email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div>
        <label htmlFor="name" className="block text-sm font-medium">
          Name
        </label>
        <input
          type="text"
          id="name"
          className="w-full p-2 border border-gray-300 rounded-md"
          placeholder="Enter your name"
          value={name}
          onChange={(e) => setName(e.target.value)}
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
        className="w-full p-2 text-white bg-green-600 rounded-md hover:bg-green-700"
      >
        Register
      </button>
    </form>
  );
};
