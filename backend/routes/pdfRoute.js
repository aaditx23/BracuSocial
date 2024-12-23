const express = require('express');
const router = express.Router();
const pdfC = require('../controllers/pdfController');

router.get('/pdf/populate', pdfC.populate);
router.get('/pdf/metadata', pdfC.getScheduleMetadata);
router.get('/pdf/schedules', pdfC.schedules);
router.get('/pdf/getAll', pdfC.getAll);

module.exports = router;
