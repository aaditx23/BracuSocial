const mongoose = require('mongoose');

const pdfCourseSchema = new mongoose.Schema({
  course: { type: String, required: true },
  section: { type: String, required: true },
  faculty: { type: String, required: true },
  classTime: { type: String, required: true },
  classRoom: { type: String, required: true },
  classDay: { type: String, required: true },
  labTime: { type: String, default: "" },
  labRoom: { type: String, default: "" },
  labDay: { type: String, default: "" },
}, { timestamps: true });

// Model
const PDFCourse = mongoose.model('PDFCourse', pdfCourseSchema);

module.exports = PDFCourse;
