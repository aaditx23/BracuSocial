const express = require('express');
const router = express.Router();
const pc = require('../controllers/profileController');


router.post('/auth/register', pc.register);
router.post('/auth/login', pc.login);
////////////////////////////////////////
router.get('/profile/:studentId', pc.getProfileByStudentId);
router.get("/profiles/getAll", pc.getAllProfiles)
router.post('/profile/uploadImage', pc.uploadProfileImage)
router.post('/profile/addCourse', pc.addCourseToProfile)
router.post('/profile/sendFriendRequest', pc.sendFriendRequest)
router.post('/profile/acceptRequest', pc.acceptFriendRequest)

module.exports = router;
