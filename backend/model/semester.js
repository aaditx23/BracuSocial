const mongoose = require('mongoose');

const semesterSchema = new mongoose.Schema({
  currentSemester: { type: String, required: true },
  nextSemester: { type: String, required: true }
}, { timestamps: true });

// Model
const Semester = mongoose.model('Semester', semesterSchema);

module.exports = Semester;
