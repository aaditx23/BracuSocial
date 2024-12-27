const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
require("dotenv").config();

const app = express();

app.use(express.json());
app.use(cors());

// MongoDB Connection
const MONGO_URI = process.env.MONGODB_URI;

mongoose.connect(MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
.then(() => console.log('MongoDB connected successfully'))
.catch((err) => {
  console.error('MongoDB connection error:', err);
  process.exit(1); 
});

// Routes
const profileRoutes = require("./routes/profileRoutes");
const pdfRoutes = require("./routes/pdfRoute");
const usisRoutes = require("./routes/usisRoutes");

app.use("/api", profileRoutes);
app.use("/api", pdfRoutes);
app.use("/api", usisRoutes);

// Start Server
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server running at http://localhost:${PORT}`);
});
