const { connectLogin } = require('../puppeteer/login')
const axios = require('axios');
const { getStudentInfo, getSchedule } = require('../util/collect')
const { processSchedule } = require('../util/process')

module.exports.login = async (req, res) => {
    const { username, password } = req.body;


    if (!username || !password) {
      return res.status(400).json({ error: 'Username and password are required.' });
    }

    try {
        // Use Puppeteer to log in and get the cookies and mercureAuthorization token
        const authorizationToken = await connectLogin(username, password);
        const studentInfo = await getStudentInfo(authorizationToken);
        const data = studentInfo[0]
        const studentSchedule = await getSchedule(data["id"], authorizationToken)
        const processedSchedule = processSchedule(studentSchedule)


        // Return the student info in the response
        res.json({ message: 'Data retrieved successfully', info: data, schedule: processedSchedule });
    } catch (error) {
      res.status(500).json({ error: error.message });
    }
};