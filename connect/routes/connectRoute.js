const express = require('express');
const router = express.Router();
const connectC = require('../controller/connectController');


router.post('/connect/login', connectC.login);
router.get('/connect/get', (req, res)=> {
    res.json({ message: 'Data retrieved successfully' });
})


module.exports = router;
