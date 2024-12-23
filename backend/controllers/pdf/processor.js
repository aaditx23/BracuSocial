const admin = require('firebase-admin');
const { getProcessedSchedule } = require('./scraper');

const db = admin.firestore();

exports.getCurrentSchedule = async (semesterName, year) => {
    try {
        const schedule = await getProcessedSchedule(semesterName, year);
        
        if (!schedule || schedule.length === 0) {
            return { error: "No schedule found for the given semester." };
        }

        return schedule;

    } catch (error) {
        console.error("Error fetching schedule:", error);
        return { error: "Internal server error" };
    }
};
  
  exports.getNextSchedule = async () => {
    try {
        const semesterDoc = await db.collection('semester').doc('semester').get();
        
        if (!semesterDoc.exists) {
            return { error: "No semester data found in Firestore." };
        }
        
        const semesterData = semesterDoc.data();
        const nextSemester = semesterData.nextSemester;
        const [nextSemesterName, nextYear] = nextSemester.split(" ");
        
        const schedule = await getProcessedSchedule(nextSemesterName, nextYear);
        
        if (!schedule || schedule.length === 0) {
            return { error: "No schedule found for the next semester." };
        }
  
        return schedule;
  
    } catch (error) {
        console.error("Error fetching semester data from Firestore:", error);
        return { error: "Internal server error" };
    }
  };
  