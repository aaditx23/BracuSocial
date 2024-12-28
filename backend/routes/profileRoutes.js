const express = require('express');
const router = express.Router();
const pc = require('../controllers/profileController');

router.get('/profiles/:studentId', pc.getProfileByStudentId);
router.post('/auth/register', pc.register);
router.post('/auth/login', pc.login);

module.exports = router;
