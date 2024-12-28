const axios = require('axios');
const https = require('https');
const pdf = require('pdf-parse');


function combineSchedule(data) {
  const combinedSchedule = {};

  data.forEach(entry => {
    const { course, faculty, section, day, startTime, endTime, room } = entry;
    const isLab = room.endsWith('L');
    const isClass = room.endsWith('C') || room.endsWith('L');  // Treat L rooms as flexible

    const key = `${course} ${section}`;

    if (!combinedSchedule[key]) {
      combinedSchedule[key] = {
        course,
        section,
        faculty,
        classTime: '',
        classRoom: '',
        classDay: '',
        labTime: '',
        labRoom: '',
        labDay: ''
      };
    }

    const shortDay = day.slice(0, 2);
    const timeSlot = `${startTime} - ${endTime}`;

    if (isClass) {
      if (room.endsWith('L')) {
        // Handle lab-like classes
        if (!combinedSchedule[key].labDay.includes(shortDay)) {
          // Add new day and time
          combinedSchedule[key].labDay += (combinedSchedule[key].labDay ? ' ' : '') + shortDay;
          combinedSchedule[key].labTime += (combinedSchedule[key].labTime ? '; ' : '') + timeSlot;
        } else {
          // Extend existing lab time for the day
          const existingTimes = combinedSchedule[key].labTime.split('; ');
          const days = combinedSchedule[key].labDay.split(' ');

          const dayIndex = days.indexOf(shortDay);
          const [existingStart, existingEnd] = existingTimes[dayIndex].split(' - ');

          combinedSchedule[key].labTime = existingTimes.map((slot, index) => {
            if (index === dayIndex) {
              return `${existingStart} - ${endTime}`;
            }
            return slot;
          }).join('; ');
        }
        combinedSchedule[key].labRoom = room;
      } else {
        // Regular class
        if (combinedSchedule[key].classTime === '') {
          combinedSchedule[key].classTime = timeSlot;
          combinedSchedule[key].classRoom = room;
          combinedSchedule[key].classDay = shortDay;
        } else {
          const days = combinedSchedule[key].classDay.split(' ');
          if (!days.includes(shortDay)) {
            combinedSchedule[key].classDay += ` ${shortDay}`;
          }
        }
      }
    }
  });

  return Object.values(combinedSchedule);
}




exports.convertPdfToJson = async (pdfBuffer) => {

  try {
    if (!pdfBuffer) {
      throw new Error('PDF buffer is required');
    }

    // Parse PDF data from buffer
    const data = await pdf(pdfBuffer);
    const pdfText = data.text;
    // Extract schedule data
    const lines = pdfText.split('\n');
    let schedule = [];
    let currentIndex = 0;

    const roomFormat = /^\d{2}[A-Z]-\d{2}[A-Z]$/;
    while (currentIndex < lines.length) {
      
      let line = lines[currentIndex].trim();
      line = line.replace(/^\d+/, '').trim();  // Remove serial numbers if not a room

      let course = line.substring(0, 6).trim();
      let faculty = '';
      let section = '';
      let day = '';
      let startTime = '';
      let endTime = '';
      let room = '';

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
        schedule.push({ course, faculty, section, day, startTime, endTime, room });
      }
      if(course === "CSE391"){
        console.log(course, faculty, section, day, startTime, endTime, room)
      }
    }

    if (schedule.length > 0) {
      schedule = combineSchedule(schedule);
    }

    return schedule;

  } catch (error) {
    console.error('Error converting PDF:', error);
    throw new Error('Error converting PDF');
  }
};