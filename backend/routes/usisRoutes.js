const express = require('express');
const router = express.Router();
const usisC = require('../controllers/usisController');

router.post('/usis/schedule', usisC.getCurrentSchedule);
router.post('/usis/populateNext', usisC.populateNextSchedule);


module.exports = router;
