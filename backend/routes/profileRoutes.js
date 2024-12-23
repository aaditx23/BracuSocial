const express = require('express');
const router = express.Router();
const pc = require('../controllers/profileController');

router.get('/profiles/:studentId', pc.getProfileByStudentId);
router.post('/profiles/register', pc.register);
router.post('/profiles/login', pc.login);

module.exports = router;
