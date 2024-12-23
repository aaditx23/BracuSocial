const express = require('express')
const cors = require('cors')
require("dotenv").config()
// firebase

const firebaseConfig = require("./firebaseConfig")
const admin = require('firebase-admin')
admin.initializeApp({
    credential: admin.credential.cert(firebaseConfig)
  });
  

const app = express()

app.use(express.json())
app.use(cors())

const profileRoutes = require("./routes/profileRoutes")
const pdfRoutes = require("./routes/pdfRoute")

app.use("/api", profileRoutes)
app.use("/api", pdfRoutes)

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});


