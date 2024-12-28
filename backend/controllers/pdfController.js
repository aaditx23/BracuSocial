const mongoose = require('mongoose');
const pdfUtil = require("./pdf/processor");
const Semester = require('../model/semester');
const PdfCourse = require('../model/pdfCourse');


exports.populate = async (req, res) => {
    try {
        const semesterDoc = await Semester.findOne();
        
        if (!semesterDoc) {
            return res.status(404).json({ message: "No semester data found in MongoDB." });
        }

        const currentSemester = semesterDoc.currentSemester;
        const [currentSemesterName, currentYear] = currentSemester.split(" ");
        
        const schedule = await pdfUtil.getCurrentSchedule(currentSemesterName, currentYear);
        
        if (schedule.error) {
            return res.status(404).json({ message: schedule.error });
        }

        const session = await mongoose.startSession();
        session.startTransaction();

        await Semester.findOneAndUpdate(
            {},
            {
                semester: currentSemesterName,
                year: currentYear,
                usis: false,
                lastUpdate: new Date()
            },
            { upsert: true, session }
        );

        const bulkOps = schedule.map(item => ({
            updateOne: {
                filter: { course: item.course, section: item.section },
                update: item,
                upsert: true
            }
        }));

        await PdfCourse.bulkWrite(bulkOps, { session });

        await session.commitTransaction();
        session.endSession();

        res.status(200).json({ message: "Schedule populated successfully!" });

    } catch (error) {
        console.error("Error populating schedule:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.getScheduleMetadata = async (req, res) => {
    try {
        const metadata = await Semester.findOne();
        
        if (!metadata) {
            return res.status(404).json({ message: "Metadata not found in MongoDB." });
        }

        res.status(200).json(metadata);
        
    } catch (error) {
        console.error("Error fetching metadata:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.schedules = async (req, res) => {
    try {
        const schedules = await PdfCourse.find();
        
        if (schedules.length === 0) {
            return res.status(404).json({ message: "No schedules found in MongoDB." });
        }

        res.status(200).json(schedules);

    } catch (error) {
        console.error("Error fetching schedules:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};
exports.getAllClassrooms = async (req, res) => {
    try {
        // Fetch distinct classroom names from the databas
        const classrooms = await PdfCourse.distinct('classRoom');

        if (classrooms.length === 0) {
            return res.status(404).json({ message: "No classrooms found in MongoDB." });
        }

        res.status(200).json(classrooms);

    } catch (error) {
        console.error("Error fetching classrooms:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.getAll = async (req, res) => {
    try {
        const schedules = await PdfCourse.find();
        
        if (schedules.length === 0) {
            return res.status(404).json({ message: "No schedules found in MongoDB." });
        }

        res.status(200).json(schedules);

    } catch (error) {
        console.error("Error fetching schedules:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.searchCourses = async (req, res) => {
    try {
        const { course, section, faculty, classTime, classRoom, classDay, labTime, labRoom, labDay } = req.body;

        const filters = {};
        if (course) filters.course = course;
        if (section) filters.section = section;
        if (faculty) filters.faculty = faculty;
        if (classTime) filters.classTime = classTime;
        if (classRoom) filters.$or = [{ classRoom }, { labRoom: classRoom }];
        if (classDay) filters.$or = [{ classDay }, { labDay: classDay }];
        if (labTime) filters.labTime = labTime;
        if (labRoom) filters.labRoom = labRoom;
        if (labDay) filters.labDay = labDay;

        const matchedCourses = await PdfCourse.find(filters);

        if (matchedCourses.length === 0) {
            return res.status(404).json({ message: "No matching courses found." });
        }

        res.status(200).json(matchedCourses);

    } catch (error) {
        console.error("Error searching courses:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

const timeSlots = [
    "08:00 AM - 09:20 AM",
    "09:30 AM - 10:50 AM",
    "11:00 AM - 12:20 PM",
    "12:30 PM - 01:50 PM",
    "02:00 PM - 03:20 PM",
    "03:30 PM - 04:50 PM",
    "05:00 PM - 06:20 PM",
    "06:30 PM - 08:00 PM",
  ];
  
  function convertTo24HourFormat(time) {

  const [hours, minutes] = time.split(':');
  const [minute, period] = minutes.split(' ');
  let hour = parseInt(hours, 10);
  
  if (period === "AM" && hour === 12) {
    hour = 0;  // Handle 12 AM as 00:00
  } else if (period === "PM" && hour !== 12) {
    hour += 12;  // Convert PM times (except 12 PM) to 24-hour format
  }
  
  return `${hour.toString().padStart(2, '0')}:${minute}`;
}

function addMinutesToTime(time, minutesToAdd) {
  let [hour, minute] = time.split(':').map(Number);
  minute += minutesToAdd;
  
  if (minute >= 60) {
    hour += Math.floor(minute / 60);
    minute = minute % 60;
  }

  if (hour >= 24) {
    hour = hour % 24; // Ensure the time stays within the 24-hour range
  }
  
  return `${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
}

function getTimeSlot(currentTime) {
  const time = convertTo24HourFormat(currentTime); // Convert current time to 24-hour format
  for (let slot of timeSlots) {
    const [start, end] = slot.split('-');
    
    // Convert both start and end times to 24-hour format
    const start24 = convertTo24HourFormat(start);
    
    // Add 10 minutes to the end time and convert to 24-hour format
    const end24 = addMinutesToTime(convertTo24HourFormat(end), 10);
    
    // Compare the times in 24-hour format
    if (time >= start24 && time <= end24) {
      return slot;
    }
  }
  return "Closed";
}
  function getCurrentTimeSlot() {
    const currentDate = new Date();
  
    // Get the current hour and minute
    let hours = currentDate.getHours();
    let minutes = currentDate.getMinutes();
  
    // Convert hours to 12-hour format and determine AM/PM
    let period = "AM";
  
    if (hours >= 12) {
      period = "PM";
      if (hours > 12) hours -= 12; // Convert 24-hour to 12-hour
    }
  
    // Format minutes to always show two digits
    minutes = minutes < 10 ? "0" + minutes : minutes;
  
    return getTimeSlot(`${hours}:${minutes} ${period}`);
  }
  async function findOccupiedClasses(time, day) {
    const occupiedClasses = await PdfCourse.find({
      classTime: time,
      classDay: { $regex: `.*${day.slice(0, 2)}.*`, $options: 'i' },
    }).select('classRoom');
  
    return occupiedClasses.map(course => course.classRoom);
  }

  async function getAllClassrooms() {
    const classrooms = await PdfCourse.distinct('classRoom');
    return classrooms;
  }

  exports.getEmptyClassrooms = async (req, res) => {
    const { day, time } = req.body; // Assume day is in the first two letters (e.g., "Mo", "Tu", "We") and time in "HH:mm AM/PM"
    
    try {
      // Step 1: Find the current time slot
      const timeSlot = getTimeSlot(time);
  
      if (timeSlot === "Closed") {
        return res.status(400).json({ message: "No classes available at this time" });
      }
  
      // Step 2: Find the occupied classrooms
      const occupiedClasses = await findOccupiedClasses(timeSlot, day);
  
      // Step 3: Get all classrooms
      const allClassrooms = await getAllClassrooms();
  
      // Step 4: Find empty classrooms
      const emptyClassrooms = allClassrooms.filter(classroom => 
        !occupiedClasses.includes(classroom) && classroom !== "Online" && !classroom.includes("FT")
      );
  
      if (emptyClassrooms.length === 0) {
        return res.status(404).json({ message: "No empty classrooms available" });
      }
  
      res.status(200).json(emptyClassrooms);
  
    } catch (error) {
      console.error("Error fetching empty classrooms:", error);
      res.status(500).json({ message: "Internal server error" });
    }
  };
  