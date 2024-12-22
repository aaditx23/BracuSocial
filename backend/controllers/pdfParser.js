const axios = require('axios');
const pdf = require('pdf-parse');


function combineSchedule(data) {
  // Object to hold combined data by course and section
  const combinedSchedule = {};

  data.forEach(entry => {
    const { course, faculty, section, day, startTime, endTime, room } = entry;
    const isLab = room.endsWith('L');
    const isClass = room.endsWith('C');

    // Create a key based on course and section
    const key = `${course}-${section}`;

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
        combinedSchedule[key].classDay = day;
      } else {
        // Append days if there are multiple class times
        combinedSchedule[key].classDay += `, ${day}`;
      }
    } else if (isLab) {
      // Add lab time information
      if (combinedSchedule[key].labTime === '') {
        combinedSchedule[key].labTime = `${startTime} - ${endTime}`;
        combinedSchedule[key].labRoom = room;
        combinedSchedule[key].labDay = day;
      } else {
        // Append lab days if there are multiple lab times
        if(combinedSchedule[key].labDay !== day){
          combinedSchedule[key].labDay += `, ${day}`;
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


exports.convertPdfToJson = async (req, res) => {
  try {
    const pdfUrl = 'https://www.bracu.ac.bd/sites/default/files/academics/Class-schedule/Class-Schedule-Fall%202024-PRINT-Version.pdf';
    
    // Fetch the PDF as a binary response
    const response = await axios.get(pdfUrl, { responseType: 'arraybuffer' });
    
    // Parse the PDF data
    const data = await pdf(response.data);
    const pdfText = data.text;


    // Process the text to extract schedule data
    const lines = pdfText.split('\n');
    var schedule = [];
    let currentIndex = 0;

    while (currentIndex < lines.length) {
      let line = lines[currentIndex].trim();

      // Step 1: Check if the line matches the room number format
      const roomFormat = /^\d{2}[A-Z]-\d{2}[A-Z]$/;
      if (roomFormat.test(line)) {
        // If the current line matches the room format, don't remove the serial number
        // Proceed to extract fields directly from the current line
      } else {
        // If it does not match, use regex to remove the serial number at the start
        line = line.replace(/^\d+/, '').trim();
      }


      // Step 2: Extract the main schedule details from the current line
      let course = line.substring(0, 6).trim();
      let faculty = '';
      let section = '';
      let day = '';
      let startTime = '';
      let endTime = '';
      let room = '';

      let i = 6;
      // Extract faculty initials
      while (i < line.length && !/\d/.test(line[i])) {
        faculty += line[i];
        i++;
      }

      // Extract section number (2 digits)
      section = line.substring(i, i + 2);
      i += 2;

      // Extract day
      while (i < line.length && !/\d/.test(line[i])) {
        day += line[i];
        i++;
      }

      // Extract start and end times
      startTime = line.substring(i, i + 8).trim();
      i += 8;
      endTime = line.substring(i, i + 8).trim();
      i += 8;

      // Step 3: Check if the next line contains the room number
      if (currentIndex + 1 < lines.length) {
        let nextLine = lines[currentIndex + 1].trim();
        if (roomFormat.test(nextLine)) {
          room = nextLine; // Room number is on the next line
          currentIndex += 2; // Move to the line after the next line
        } else {
          currentIndex++; // Move to the next line if it doesn't match the room format
        }
      } else {
        currentIndex++; // If there's no next line, just move to the next line
      }

      // Add to schedule if room data was found
      if (room) {
        schedule.push({ course, faculty, section, day, startTime, endTime, room });
      }
    }

    if (schedule.length >0){
      schedule = combineSchedule(schedule)
    }

    // Return the processed JSON data
    res.status(200).json(schedule);
  } catch (error) {
    console.error('Error converting PDF:', error);
    res.status(500).json({ error: 'Error converting PDF' });
  }
};
