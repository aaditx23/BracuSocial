const axios = require('axios');
const { wrapper } = require('axios-cookiejar-support');
const { CookieJar } = require('tough-cookie');
const qs = require('qs');  // To parse data for the login form


const loginUrl = "https://usis.bracu.ac.bd/academia/j_spring_security_check";
const scheduleUrlClassAndLab = "https://usis.bracu.ac.bd/academia/academicSection/listAcademicSectionWithSchedule?academiaSession=SESSION&_search=false&nd=1734965745519&rows=5000&page=1&sidx=course_code&sord=asc";
const scheduleUrlClass = "https://usis.bracu.ac.bd/academia/studentCourse/showClassScheduleInTabularFormatInGrid?query=&academiaSession=SESSION&_search=false&nd=1726345445840&rows=5000&page=1&sidx=course_code&sord=asc";

// Create the cookie jar and axios client with cookie jar support
const jar = new CookieJar();
const client = wrapper(axios.create({ jar }));

// Function to login and initialize client with cookies
async function loginToUsis(email, password) {
  const loginData = qs.stringify({
    j_username: email,
    j_password: password,
  });

  try {
    const loginResponse = await client.post(loginUrl, loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });

    // Check if login was successful
    if (loginResponse.data.includes("Invalid")) {
      throw new Error("Login failed. Invalid credentials.");
    }

    return true;  // Login successful
  } catch (error) {
    console.error("Login error:", error);
    throw new Error("Login failed. Please try again.");
  }
}

// Function to fetch class schedule, using client after login
exports.classAndLabSchedule = async (email, password, sessionCode) => {
  try {
    // Login to save cookies and initialize client
    await loginToUsis(email, password);

    const url = scheduleUrlClassAndLab.replace('SESSION', sessionCode);  // Replace session dynamically
    const response = await client.get(url);
    return response.data;
  } catch (error) {
    console.error("Failed to fetch class schedule:", error);
    throw new Error("Failed to fetch class schedule.");
  }
};

// Function to fetch room schedule, using client after login
exports.classOnlySchedule = async (email, password, sessionCode) => {
  try {
    // Login to save cookies and initialize client
    await loginToUsis(email, password);

    const url = scheduleUrlClass.replace('SESSION', sessionCode);  // Replace session dynamically
    const response = await client.get(url);
    return response.data;
  } catch (error) {
    console.error("Failed to fetch room schedule:", error);
    throw new Error("Failed to fetch room schedule.");
  }
};
