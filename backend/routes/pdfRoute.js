const express = require('express');
const router = express.Router();
const pdfC = require('../controllers/pdfController');

router.get('/pdf/populate', pdfC.populate);
router.get('/pdf/metadata', pdfC.getScheduleMetadata);
router.get('/pdf/schedules', pdfC.schedules);
router.get('/pdf/getAll', pdfC.getAll);
router.post("/pdf/getCourse", pdfC.getCourseBySection)
router.post('/pdf/getClasses', pdfC.getEmptyClassrooms);

router.post("/pdf/search", pdfC.searchCourses)

module.exports = router;
