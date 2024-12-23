const express = require('express');
const router = express.Router();
const pdfC = require('../controllers/pdfController');

router.get('/schedule', pdfC.fetchAndProcessSchedule);

module.exports = router;
