const admin = require('firebase-admin');
const usis = require("./usis/scraper");
const session = require("./usis/session");
const processor = require("./usis/processor")

exports.getCurrentSchedule = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Email and password are required" });
  }

  try {
    // Fetch currentSemester from Firebase
    const semesterDoc = await admin.firestore().collection('semester').doc('semester').get();
    const currentSemester = semesterDoc.data().currentSemester;

    const [semester, year] = currentSemester.split(" ");
    
    const sessionCode = session.generateSessionCode(semester.toLowerCase(), parseInt(year));
    

    // Call the fetchClassSchedule function from usisScraper
    const scheduleResult = await usis.classAndLabSchedule(email, password, sessionCode);
    const processedSchedule = processor.processSchedule(scheduleResult)

    return res.status(200).json(processedSchedule);  // Return fetched schedule
  } catch (error) {
    console.error("Error in getCurrentSchedule:", error);
    return res.status(500).json({ message: "Internal server error" });
  }
};

exports.getNextSchedule = async (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    return res.status(400).json({ message: "Email and password are required" });
  }

  try {
    // Fetch nextSemester from Firebase
    const semesterDoc = await admin.firestore().collection('semester').doc('semester').get();
    const nextSemester = semesterDoc.data().nextSemester;

    const [semester, year] = nextSemester.split(" ");
    
    const sessionCode = session.generateSessionCode(semester.toLowerCase(), parseInt(year));

    // Call the fetchClassSchedule function from usisScraper
    const scheduleResult = await usis.classAndLabSchedule(email, password, sessionCode);
    const processedSchedule = processor.processSchedule(scheduleResult)

    return res.status(200).json(processedSchedule);  // Return fetched schedule
  } catch (error) {
    console.error("Error in getNextSchedule:", error);
    return res.status(500).json({ message: "Internal server error" });
  }
};
