const axios = require('axios');

module.exports.getStudentInfo = async (authorizationToken) => {
  try {
    // Ensure authorizationToken is provided
    if (!authorizationToken) {
      throw new Error('Authorization token is required');
    }

    // Prepare headers for the API request with the provided authorization token
    const headers = {
      'Authorization': `Bearer ${authorizationToken}`,
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    };

    // Make the GET request to the API with the token
    console.log('Making API request with authorization token...');
    const response = await axios.get('https://connect.bracu.ac.bd/api/mds/v1/portfolios', {
      headers: headers
    });

    // Return the response data from the API
    return response.data;
  } catch (error) {
    console.error('Error during API request:', error);
    throw new Error('Failed to fetch student info');
  }
};

module.exports.getSchedule = async (id, authorizationToken) => {
    try {
      // Prepare headers for the API request
      const headers = {
        'Authorization': `Bearer ${authorizationToken}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      };
  
      // Construct the URL using the student ID
      const url = `https://connect.bracu.ac.bd/api/adv/v1/student-courses/${id}`;
  
      // Make the GET request to the API
      const response = await axios.get(url, { headers });
  
      // Return the response data
      return response.data;
    } catch (error) {
      console.error('Error during API request:', error);
      throw new Error('Failed to fetch schedule data');
    }
  };