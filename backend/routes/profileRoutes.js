const express = require('express');
const router = express.Router();
const pc = require('../controllers/profileController');

router.get('/profile/:studentId', pc.getProfileByStudentId);
router.get("/profiles/getAll", pc.getAllProfiles)
router.post('/auth/register', pc.register);
router.post('/auth/login', pc.login);
router.post('/profiles/addCourse', pc.addCourseToProfile)

module.exports = router;
