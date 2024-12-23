const axios = require('axios');
const cheerio = require('cheerio');
const {convertPdfToJson} = require('./converter')
const https = require('https');



function getNextSemesters(currentSemester, currentYear) {
    const semesters = ['spring', 'summer', 'fall'];
    let nextSemesters = [];
  
    let currentSemesterIndex = semesters.indexOf(currentSemester.toLowerCase());
    if (currentSemesterIndex === -1) {
      throw new Error("Invalid semester");
    }
  
    for (let i = 0; i < 3; i++) {
      currentSemesterIndex = (currentSemesterIndex + 1) % 3;
      if (currentSemesterIndex === 0) {
        currentYear = (parseInt(currentYear) + 1).toString();
      }
      nextSemesters.push({ semester: semesters[currentSemesterIndex], year: currentYear });
    }
  
    return nextSemesters;
}

function getNextSemesterUrl(currentSemester, currentYear) {
    const nextSemesters = getNextSemesters(currentSemester, currentYear);
    const nextSemester = nextSemesters[0]; 
    const baseUrl = 'https://www.bracu.ac.bd/class-schedule-';
  
    return `${baseUrl}${nextSemester.semester}-${nextSemester.year}`;
}





async function scrapeScheduleUrl(baseUrl) {
  try {

    const agent = new https.Agent({
      rejectUnauthorized: false
    });

    // Fetch the PDF as a binary response
    const response = await axios.get(baseUrl, {
      responseType: 'arraybuffer',
      httpsAgent: agent  // Attach custom HTTPS agent
    });
    const $ = cheerio.load(response.data);

    if ($('body').text().toLowerCase().includes("could not be found")) {
      return { message: "Schedule not available" };
    }

    const pdfLink = $('strong:contains("Class schedule") a[href*="www.bracu.ac.bd/sites/default/files/"]').attr('href');
    
    if (pdfLink) {
      return { pdfUrl: `https:${pdfLink}` }; 
    }

    return { message: "PDF Not available" };

  } catch (error) {
    console.error("Error scraping schedule URL ", error);
    return { message: "Error scraping schedule URL" };
  }
}

exports.getProcessedSchedule = async (currentSemester, currentYear) => {
  try {
    const agent = new https.Agent({
      rejectUnauthorized: false
    });

    const scraped = await scrapeScheduleUrl(`https://www.bracu.ac.bd/class-schedule-${currentSemester}-${currentYear}`);
    if(!scraped.pdfUrl){
      return { message: scraped.message }
    }
    const pdfUrl = scraped.pdfUrl
    const response = await axios.get(pdfUrl, {
      responseType: 'arraybuffer',
      httpsAgent: agent
    });

    const pdfBuffer = response.data;  

    const pdfResponse = await convertPdfToJson(pdfBuffer);
    return pdfResponse;

  } catch (error) {
    console.error("Error in getProcessedSchedule:", error);
    return { message: "Error processing schedule" };
  }
};

exports.getNextProcessedSchedule = async (currentSemester, currentYear) => {
    try {
      const nextSemesterUrl = getNextSemesterUrl(currentSemester, currentYear);
  
      const { pdfUrl, message } = await scrapeScheduleUrl(nextSemesterUrl);
  
      if (message) {
        return { message };
      }
  
      const pdfResponse = await convertPdfToJson(pdfUrl);
  
      return pdfResponse; 
  
    } catch (error) {
      console.error("Error in getProcessedSchedule:", error);
      return { message: "Error processing schedule" };
    }
};



  

