const puppeteer = require('puppeteer');
const axios = require('axios');

module.exports.connectLogin = async (username, password) => {
  const browser = await puppeteer.launch({ headless: true });
  const page = await browser.newPage();

  let authorizationToken = '';

  // Track all requests and their headers
  page.on('request', (request) => {
    // Check if the Authorization header is present in the request
    const authHeader = request.headers()['authorization'];
    if (authHeader) {
    //   console.log('Authorization Header:', authHeader);
      authorizationToken = authHeader.split(' ')[1];
    }
  });

  try {
    // Navigate to the connect URL (this will redirect to SSO)
    console.log('Navigating to the connect URL...');
    await page.goto('https://connect.bracu.ac.bd/');
    await page.waitForNavigation({ waitUntil: 'networkidle0' });

    // Wait for the username input field to be loaded
    await page.waitForSelector('input[name="username"]');
    
    // Fill in the credentials
    await page.type('input[name="username"]', username);
    await page.type('input[name="password"]', password);

    // Submit the login form
    await page.keyboard.press('Enter');
    await page.waitForNavigation({ waitUntil: 'networkidle0' });

    // Log the final URL (after all redirects)
    console.log(`Final URL after all redirects: ${page.url()}`);

    // Extract cookies from the final page
    const cookies = await browser.cookies();

    // Close the browser
    await browser.close();

    // If authorization token is found, proceed
    if (authorizationToken !== "") {
        console.log(`Auth Token: ${authorizationToken}`)
      return authorizationToken;
    } else {
      throw new Error('Authorization token not found');
    }
  } catch (error) {
    console.error('Error during login automation:', error);
    await browser.close();
    throw new Error('Login failed or token not found');
  }
};

