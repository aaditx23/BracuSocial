const bcrypt = require('bcrypt');
const Profile = require('../model/profile');
const Course = require('../model/pdfCourse');
const mongoose = require('mongoose');

exports.getAllProfiles = async (req, res) => {
  try {
    const profiles = await Profile.find({});
    res.status(200).json(profiles);
  } catch (error) {
    console.error('Error fetching all profiles:', error);
    res.status(500).json({ error: 'Error fetching all profiles' });
  }
};


exports.getProfileByStudentId = async (req, res) => {
  try {
    const { studentId } = req.params;

    const profile = await Profile.findOne({ studentId });
    if (!profile) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    res.status(200).json(profile);
  } catch (error) {
    console.error('Error fetching profile:', error);
    res.status(500).json({ error: 'Error fetching profile' });
  }
};

exports.register = async (req, res) => {
  try {
    const { studentId, email, name, password } = req.body;

    if (!studentId || !email || !name || !password) {
      return res.status(400).json({ message: 'All fields are required' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newProfile = new Profile({
      email,
      studentId,
      password: hashedPassword,
      name
    });

    await newProfile.save();

    res.status(201).json({ message: 'User registered successfully' });
  } catch (error) {
    console.error('Error registering user:', error);
    res.status(500).json({ error: 'Error registering user' });
  }
};

exports.login = async (req, res) => {
  try {
    const { identifier, password } = req.body;
    const isEmail = identifier.includes('@');
    const queryField = isEmail ? 'email' : 'studentId';

    const user = await Profile.findOne({ [queryField]: identifier });
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    const isPasswordValid = await bcrypt.compare(password, user.password);

    if (!isPasswordValid) {
      return res.status(401).json({ message: 'Invalid password' });
    }

    res.status(200).json({
      message: 'Login successful',
      user
    });
  } catch (error) {
    console.error('Error logging in user:', error);
    res.status(500).json({ error: 'Error logging in user' });
  }
};

exports.sendFriendRequest = async (req, res) => {
  try {
    const { from, to } = req.body;

    const fromProfile = await Profile.findOne({ studentId: from });
    const toProfile = await Profile.findOne({ studentId: to });

    if (!fromProfile || !toProfile) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    if (!toProfile.friendRequests.includes(from)) {
      const updatedRequests = toProfile.friendRequests
        ? `${toProfile.friendRequests},${from}`
        : from;

      toProfile.friendRequests = updatedRequests;
      await toProfile.save();

      res.status(200).json({ message: 'Friend request sent successfully' });
    } else {
      res.status(400).json({ message: 'Friend request already sent' });
    }
  } catch (error) {
    console.error('Error sending friend request:', error);
    res.status(500).json({ error: 'Error sending friend request' });
  }
};

exports.acceptFriendRequest = async (req, res) => {
  try {
    const { studentId, friendId } = req.body;

    const profile = await Profile.findOne({ studentId });
    if (!profile) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    if (!profile.addedFriends.includes(friendId)) {
      const updatedFriends = profile.addedFriends
        ? `${profile.addedFriends},${friendId}`
        : friendId;

      const updatedRequests = profile.friendRequests
        .split(',')
        .filter(id => id !== friendId)
        .join(',');

      profile.addedFriends = updatedFriends;
      profile.friendRequests = updatedRequests;
      await profile.save();

      res.status(200).json({ message: 'Friend request accepted' });
    } else {
      res.status(400).json({ message: 'Already friends' });
    }
  } catch (error) {
    console.error('Error accepting friend request:', error);
    res.status(500).json({ error: 'Error accepting friend request' });
  }
};

exports.cancelFriendRequest = async (req, res) => {
  try {
    const { from, to } = req.body;

    const profile = await Profile.findOne({ studentId: to });
    if (!profile) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    const updatedRequests = profile.friendRequests
      .split(',')
      .filter(id => id !== from)
      .join(',');

    profile.friendRequests = updatedRequests;
    await profile.save();

    res.status(200).json({ message: 'Friend request canceled' });
  } catch (error) {
    console.error('Error canceling friend request:', error);
    res.status(500).json({ error: 'Error canceling friend request' });
  }
};

exports.addCourseToProfile = async (req, res) => {
  try {
    const { email, newCourse } = req.body;

    const profile = await Profile.findOne({ email });
    if (!profile) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    const updatedCourses = newCourse;

    profile.enrolledCourses = updatedCourses;
    await profile.save();

    res.status(200).json({ message: 'Course added to profile' });
  } catch (error) {
    console.error('Error adding course:', error);
    res.status(500).json({ error: 'Error adding course' });
  }
};

exports.searchCourses = async (req, res) => {
  try {
    const { course, section, faculty, classTime, classRoom, classDay, labTime, labRoom, labDay } = req.body;

    const filters = {};
    if (course) filters.course = course;
    if (section) filters.section = section;
    if (faculty) filters.faculty = faculty;
    if (classTime) filters.classTime = classTime;
    if (classRoom) filters.classRoom = classRoom;
    if (classDay) filters.classDay = classDay;
    if (labTime) filters.labTime = labTime;
    if (labRoom) filters.labRoom = labRoom;
    if (labDay) filters.labDay = labDay;

    const matchedCourses = await Course.find(filters);

    if (matchedCourses.length === 0) {
      return res.status(404).json({ message: "No matching courses found." });
    }

    res.status(200).json(matchedCourses);
  } catch (error) {
    console.error("Error searching courses:", error);
    res.status(500).json({ message: "Internal server error" });
  }
};
