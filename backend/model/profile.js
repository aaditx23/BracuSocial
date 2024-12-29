const mongoose = require('mongoose');

const profileSchema = new mongoose.Schema({
  email: { type: String, required: true, unique: true },
  studentId: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  name: { type: String, required: true },
  enrolledCourses: { type: String, default: "" },  
  addedFriends: { type: String, default: "" }, 
  friendRequests: { type: String, default: "" },
  profilePicture: { type: String, default: '' }
}, { timestamps: true });

// Model
const Profile = mongoose.model('Profile', profileSchema);

module.exports = Profile;
