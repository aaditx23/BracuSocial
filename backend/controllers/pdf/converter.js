const axios = require('axios');
const https = require('https');
const pdf = require('pdf-parse');


function combineSchedule(data) {
  // Object to hold combined data by course and section
  const combinedSchedule = {};

  data.forEach(entry => {
    const { course, faculty, section, day, startTime, endTime, room } = entry;
    const isLab = room.endsWith('L');
    const isClass = room.endsWith('C');

    // Create a key based on course and section
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

    if (isClass) {
      // Add class time information
      if (combinedSchedule[key].classTime === '') {
        combinedSchedule[key].classTime = `${startTime} - ${endTime}`;
        combinedSchedule[key].classRoom = room;
        combinedSchedule[key].classDay = day.slice(0, 2);
      } else {
        // Append days if there are multiple class times
        combinedSchedule[key].classDay += ` ${day.slice(0, 2)}`;
      }
    } else if (isLab) {
      // Add lab time information
      if (combinedSchedule[key].labTime === '') {
        combinedSchedule[key].labTime = `${startTime} - ${endTime}`;
        combinedSchedule[key].labRoom = room;
        combinedSchedule[key].labDay = day.slice(0, 2);
      } else {
        // Append lab days if there are multiple lab times
        if(combinedSchedule[key].labDay !== day){
          combinedSchedule[key].labDay += ` ${day.slice(0, 2)}`;
        }

        // Update lab time span if there are multiple entries
        const labTimes = combinedSchedule[key].labTime.split(' - ');
        const firstStartTime = startTime;
        const secondEndTime = labTimes[1];
        combinedSchedule[key].labTime = `${firstStartTime} - ${secondEndTime}`;
      }
    }
  });

  // Convert combinedSchedule object to an array
  const result = Object.values(combinedSchedule);

  return result;
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