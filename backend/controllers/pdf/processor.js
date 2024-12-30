const { getProcessedSchedule } = require("./scraper");
const Semester = require("../../model/semester");

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
    const semesterDoc = await Semester.findOne({});

    if (!semesterDoc) {
      return { error: "No semester data found in MongoDB." };
    }

    const nextSemester = semesterDoc.nextSemester;
    const [nextSemesterName, nextYear] = nextSemester.split(" ");

    const schedule = await getProcessedSchedule(nextSemesterName, nextYear);

    if (!schedule || schedule.length === 0) {
      return { error: "No schedule found for the next semester." };
    }

    return schedule;
  } catch (error) {
    console.error("Error fetching semester data from MongoDB:", error);
    return { error: "Internal server error" };
  }
};
