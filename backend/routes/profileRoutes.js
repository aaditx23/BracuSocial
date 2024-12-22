const express = require('express');
const router = express.Router();
const profileController = require('../controllers/profileController');

// Route to get a profile by studentId
router.get('/profiles/:studentId', profileController.getProfileByStudentId);

module.exports = router;
