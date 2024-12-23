const express = require('express');
const router = express.Router();
const usisC = require('../controllers/usisController');

router.post('/usis/schedule', usisC.getSchedule);


module.exports = router;
