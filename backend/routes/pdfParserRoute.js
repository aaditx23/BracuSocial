const express = require('express');
const router = express.Router();
const { convertPdfToJson } = require('../controllers/pdfParser');

router.get('/convert-pdf', convertPdfToJson);

module.exports = router;
