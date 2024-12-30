const axios = require("axios");
const https = require("https");
const pdf = require("pdf-parse");

function handleClass(entry, combinedSchedule, key, shortDay) {
  const { startTime, endTime, room } = entry;

  if (combinedSchedule[key].classTime === "") {
    combinedSchedule[key].classTime = `${startTime} - ${endTime}`;
    combinedSchedule[key].classRoom = room;
    combinedSchedule[key].classDay = shortDay;
  } else {
    // Add unique days only
    const days = combinedSchedule[key].classDay.split(" ");
    if (!days.includes(shortDay)) {
      combinedSchedule[key].classDay += ` ${shortDay}`;
    }
  }
}

function convertTo24HourFormat(time) {
  const [hours, minutes] = time.split(/[: ]/);
  const isPM = time.includes("PM");
  let hour24 = parseInt(hours, 10);
  if (isPM && hour24 !== 12) hour24 += 12;
  if (!isPM && hour24 === 12) hour24 = 0;
  return hour24 * 60 + parseInt(minutes, 10); // Convert to total minutes
}

function convertTo12HourFormat(totalMinutes) {
  const hours24 = Math.floor(totalMinutes / 60);
  const minutes = totalMinutes % 60;
  const isPM = hours24 >= 12;
  const hours12 = hours24 % 12 || 12;

  // Ensure both hour and minute are two digits
  const formattedHours = hours12.toString().padStart(2, "0");
  const formattedMinutes = minutes.toString().padStart(2, "0");
  const period = isPM ? "PM" : "AM";

  return `${formattedHours}:${formattedMinutes} ${period}`;
}

function handleLab(entry, combinedSchedule, key, shortDay) {
  const { startTime, endTime, room } = entry;

  if (combinedSchedule[key].labTime === "") {
    combinedSchedule[key].labTime = `${startTime} - ${endTime}`;
    combinedSchedule[key].labRoom = room;
    combinedSchedule[key].labDay = shortDay;
  } else {
    // Add unique lab days
    const labDays = combinedSchedule[key].labDay.split(" ");
    if (!labDays.includes(shortDay)) {
      combinedSchedule[key].labDay += ` ${shortDay}`;
    }

    // Update lab time span if there are multiple entries
    const labTimes = combinedSchedule[key].labTime.split(" - ");

    // Collect all time entries and convert to 24-hour format
    const timesInMinutes = [
      convertTo24HourFormat(labTimes[0]),
      convertTo24HourFormat(labTimes[1]),
      convertTo24HourFormat(startTime),
      convertTo24HourFormat(endTime),
    ];

    // Find smallest and largest times
    const smallestTime = Math.min(...timesInMinutes);
    const largestTime = Math.max(...timesInMinutes);

    // Update lab time span
    combinedSchedule[key].labTime = `${convertTo12HourFormat(
      smallestTime
    )} - ${convertTo12HourFormat(largestTime)}`;
  }
}

function combineSchedule(data) {
  // Object to hold combined data by course and section
  const combinedSchedule = {};

  data.forEach((entry) => {
    if (entry.course !== "CSE391" && entry.course !== "CSE489") {
      const { course, section, day, room } = entry;
      const isLab = room.endsWith("L");
      const isClass = room.endsWith("C");

      // Create a key based on course and section
      const key = `${course} ${section}`;

      if (!combinedSchedule[key]) {
        combinedSchedule[key] = {
          course,
          section,
          faculty: entry.faculty,
          classTime: "",
          classRoom: "",
          classDay: "",
          labTime: "",
          labRoom: "",
          labDay: "",
        };
      }

      const shortDay = day.slice(0, 2);

      if (isClass) {
        handleClass(entry, combinedSchedule, key, shortDay);
      } else if (isLab) {
        handleLab(entry, combinedSchedule, key, shortDay);
      }

      if (entry.course === "CSE110" && entry.section === "03") {
        console.log(entry.room, isClass, isLab);
      }
    }
  });

  // Convert combinedSchedule object to an array
  return Object.values(combinedSchedule);
}

exports.convertPdfToJson = async (pdfBuffer) => {
  try {
    if (!pdfBuffer) {
      throw new Error("PDF buffer is required");
    }

    // Parse PDF data from buffer
    const data = await pdf(pdfBuffer);
    const pdfText = data.text;
    // Extract schedule data
    const lines = pdfText.split("\n");
    let schedule = [];
    let currentIndex = 0;

    const roomFormat = /^\d{2}[A-Z]-\d{2}[A-Z]$/;
    while (currentIndex < lines.length) {
      let line = lines[currentIndex].trim();
      line = line.replace(/^\d+/, "").trim(); // Remove serial numbers if not a room

      let course = line.substring(0, 6).trim();
      let faculty = "";
      let section = "";
      let day = "";
      let startTime = "";
      let endTime = "";
      let room = "";

      let i = 6;
      while (i < line.length && !/\d/.test(line[i])) faculty += line[i++];
      section = line.substring(i, i + 2);
      i += 2;
      while (i < line.length && !/\d/.test(line[i])) day += line[i++];
      startTime = line.substring(i, i + 8).trim();
      i += 8;
      endTime = line.substring(i, i + 8).trim();
      i += 8;

      if (currentIndex + 1 < lines.length) {
        let nextLine = lines[currentIndex + 1].trim();
        if (roomFormat.test(nextLine)) {
          room = nextLine;
          currentIndex += 2;
        } else {
          currentIndex++;
        }
      } else {
        currentIndex++;
      }

      if (room) {
        schedule.push({
          course,
          faculty,
          section,
          day,
          startTime,
          endTime,
          room,
        });
      }
      if (course === "CSE391") {
        console.log(course, faculty, section, day, startTime, endTime, room);
      }
    }

    if (schedule.length > 0) {
      schedule = combineSchedule(schedule);
    }

    return schedule;
  } catch (error) {
    console.error("Error converting PDF:", error);
    throw new Error("Error converting PDF");
  }
};
