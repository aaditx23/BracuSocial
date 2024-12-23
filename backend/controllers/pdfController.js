const admin = require('firebase-admin');
const { getProcessedSchedule } = require('./pdf/scraper');

const db = admin.firestore();

exports.fetchAndProcessSchedule = async (req, res) => {
  try {
      const semesterDoc = await db.collection('semester').doc('semester').get();
      
      if (!semesterDoc.exists) {
          return res.status(404).json({ message: "No semester data found in Firestore." });
      }
      
      const semesterData = semesterDoc.data();
      const currentSemester = semesterData.currentSemester;
      const [currentSemesterName, currentYear] = currentSemester.split(" ");
      
      const schedule = await getProcessedSchedule(currentSemesterName, currentYear);
      
      if (!schedule || schedule.length === 0) {
          return res.status(404).json({ message: "No schedule found for the current semester." });
      }

      return res.status(200).json(schedule);

  } catch (error) {
      console.error("Error fetching semester data from Firestore:", error);
      res.status(500).json({ message: "Internal server error" });
  }
};

