const axios = require('axios');
const { wrapper } = require('axios-cookiejar-support');
const { CookieJar } = require('tough-cookie');
const qs = require('qs');  // To parse data for the login form

const loginUrl = "https://usis.bracu.ac.bd/academia/j_spring_security_check";
const scheduleUrlClass = "https://usis.bracu.ac.bd/academia/academicSection/listAcademicSectionWithSchedule?academiaSession=627125&_search=false&nd=1734965745519&rows=5000&page=1&sidx=course_code&sord=asc"
// Create the cookie jar
const jar = new CookieJar();

// Create the axios instance wrapped with cookie jar support
const client = wrapper(axios.create({ jar }));

// Function to login and fetch schedule
exports.usisLoginAndFetchSchedule = async (email, password) => {
  try {
    // Prepare the login data
    const loginData = qs.stringify({
      j_username: email,
      j_password: password,
    });

    // Step 1: Login via POST request
    const loginResponse = await client.post(loginUrl, loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    // Check if login was successful by inspecting the response content
    if (!loginResponse.data.includes("Invalid")) {
      // Login successful, now fetch the schedule

      // Step 2: Fetch schedule using GET request, sending cookies in the header
      const scheduleResponse = await client.get(scheduleUrlClass);

      if (scheduleResponse.status === 200) {
        return {
          success: true,
          schedule: scheduleResponse.data,  // Return the class schedule JSON data
        };
      } else {
        return {
          success: false,
          message: "Failed to fetch schedule.",
        };
      }
    } else {
      return {
        success: false,
        message: "Login failed. Please check your credentials.",
      };
    }
  } catch (error) {
    console.error("Error during login or fetching schedule:", error);
    return {
      success: false,
      message: "Error during login or fetching schedule. Please try again.",
    };
  }
};

// Function to call login and fetch schedule with email and password
exports.loginAndFetchSchedule = async (email, password) => {
  if (!email || !password) {
    return { success: false, message: "Email and password are required" };
  }

  try {
    const result = await exports.usisLoginAndFetchSchedule(email, password);

    if (result.success) {
      return { success: true, schedule: result.schedule };  // Return the class schedule as JSON
    } else {
      return { success: false, message: result.message };  // Return the error message
    }
  } catch (error) {
    console.error("Error in loginAndFetchSchedule:", error);
    return { success: false, message: "Internal server error. Please try again." };
  }
};
