const express = require('express');
const router = express.Router();
const connectC = require('../controller/connectController');

router.post('/connect/login', connectC.login);


module.exports = router;
