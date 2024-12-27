const mongoose = require('mongoose');
const session = require("./usis/session");
const processor = require("./usis/processor");
const USISCourse = require('../model/usisCourse'); // Import the USISCourse model
const Semester = require('../model/semester');

exports.getCurrentSchedule = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Email and password are required" });
  }

  try {
    // Fetch currentSemester from MongoDB
    const semesterDoc = await Semester.findOne({});
    if (!semesterDoc) {
      return res.status(404).json({ message: "Semester data not found" });
    }

    const currentSemester = semesterDoc.currentSemester;
    const [semester, year] = currentSemester.split(" ");
    
    const sessionCode = session.generateSessionCode(semester.toLowerCase(), parseInt(year));

    const finalJson = await processor.getProcessedSchedule(email, password, sessionCode);
    
    return res.status(200).json(finalJson); 
  } catch (error) {
    console.error("Error in getCurrentSchedule:", error);
    return res.status(500).json({ message: "Internal server error" });
  }
};



exports.populateNextSchedule = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Email and password are required" });
  }

  try {
    // Fetch nextSemester from MongoDB
    const semesterDoc = await Semester.findOne({});
    if (!semesterDoc) {
      return res.status(404).json({ message: "Semester data not found" });
    }

    const nextSemester = semesterDoc.nextSemester;
    const [semester, year] = nextSemester.split(" ");
    
    // Generate session code
    const sessionCode = session.generateSessionCode(semester.toLowerCase(), parseInt(year));

    // Get the processed schedule
    const finalJson = await processor.getProcessedSchedule(email, password, sessionCode);

    // Check if finalJson is not empty
    if (finalJson && finalJson.length > 0) {
      // Start MongoDB session for bulk operations
      const mongoSession = await mongoose.startSession();
      mongoSession.startTransaction();

      const bulkOps = finalJson.map(item => ({
        updateOne: {
          filter: { course: item.course, section: item.section },
          update: item,
          upsert: true
        }
      }));

      // Perform bulk write operation to populate the USISCourse collection
      await USISCourse.bulkWrite(bulkOps, { session: mongoSession });

      // Commit transaction
      await mongoSession.commitTransaction();
      mongoSession.endSession();
      
      console.log("Data written to MongoDB successfully.");
    } else {
      console.log("No data to write to MongoDB.");
    }

    return res.status(200).json(finalJson);  // Send back the processed schedule data
    
  } catch (error) {
    console.error("Error in getNextSchedule:", error);
    return res.status(500).json({ message: "Internal server error" });
  }
};

