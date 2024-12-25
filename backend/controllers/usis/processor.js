const usis = require("../usis/scraper")

async function processSchedule(response) {
    const demoDb = {};

    response.rows.forEach(entry => {
        // Extract relevant fields from the 'cell' array
        const courseCode = entry.cell[1];
        const section = entry.cell[3];
        const key = `${courseCode} ${section}`;
        const faculty = entry.cell[8];
        const exam = entry.cell[10];
        const classTimes = entry.cell[11]; // Can be multiple times
        const labTimes = entry.cell[12]; // Can be multiple times

        // Initialize the object structure if it's not already created
        if (!demoDb[key]) {
            demoDb[key] = {
                course_code: courseCode,
                section: section,
                faculty: faculty,
                exam: exam || "",
                class: {},
                lab: {}
            };
        }

        // If exam exists, this is class data
        if (exam) {
            const classData = getDayTime(entry.cell)
            demoDb[key].class = classData.class
        } else {
            // If exam doesn't exist, this is lab data
            const labData = getDayTime(entry.cell)
            demoDb[key].lab = labData.class
        }
    });

    const transformedDb = {};
    for (const [key, value] of Object.entries(demoDb)) {
        transformedDb[key] = await transformSchedule(value);
    }

    return transformedDb;
};

async function transformSchedule(schedule) {
    // Create a copy of the input object without class and lab fields
    const copyObject = {
        course_code: schedule.course_code,
        section: schedule.section,
        faculty: schedule.faculty,
        exam: schedule.exam
    };

    // Extract and process class field
    if (schedule.class && Object.keys(schedule.class).length > 0) {
        const classKeys = Object.keys(schedule.class);
        copyObject.classDays = classKeys.join(' ');  // Join days with space
        copyObject.classTime = schedule.class[classKeys[0]];  // Take any available time
    } else {
        copyObject.classDays = "";
        copyObject.classTime = "";
    }
    copyObject.classRoom = "";
    if (schedule.lab && Object.keys(schedule.lab).length > 0) {
        const labKeys = Object.keys(schedule.lab);
        copyObject.labDays = labKeys.join(' ');  // Join days with space
        copyObject.labTime = schedule.lab[labKeys[0]];  // Take any available time
    } else {
        copyObject.labDays = "";
        copyObject.labTime = "";
    }

    copyObject.labRoom = "unavailable";

    return copyObject;
}


function getDayTime(cell) {
    const dayMap = {
        11: 'Su',
        12: 'Mo',
        13: 'Tu',
        14: 'We',
        15: 'Th',
        16: 'Fr',
        17: 'Sa'
    };

    const classTimesJson = { class: {} };

    for (let i = 11; i <= 17; i++) {
        var classTime = cell[i];
        if (classTime) {
            classTime = processTime(classTime)
            const day = dayMap[i]; 
            classTimesJson.class[day] = classTime; 
        }
    }

    return classTimesJson;
};

function processTime(timeString) {
    if (timeString.length === 17) {
        return timeString; 
    } else if (timeString.length ===34) {
        return  timeString.slice(17, 25) + '-' +  timeString.slice(9, 17);  
    }
    return null; 
}

async function extractClassrooms(response) {
    const classroomDb = {};

    response.rows.forEach(entry => {
        const cell = entry.cell;
        const courseCode = cell[2];
        const section = cell[4];
        const classroom = cell[8];
        const key = `${courseCode} ${section}`;

        if (!classroomDb[key]) {
            classroomDb[key] = new Set();
        }

        classroomDb[key].add(classroom);
    });

    const result = {};
    for (const [key, value] of Object.entries(classroomDb)) {
        result[key] = Array.from(value);
    }

    return result;
};

async function processScheduleWithRoom(classSchedule, roomJson) {
    for (const key in classSchedule) {
        if (roomJson[key]) {
            classSchedule[key].classRoom = roomJson[key][0] || '';
        } else {
            classSchedule[key].classRoom = '';
        }
    }
    return classSchedule;
};

exports.getProcessedSchedule = async (email, password, sessionCode) => {
    try {
        const scheduleWithLab = await usis.classAndLabSchedule(email, password, sessionCode);
        const processedScheduleWithLab = await processSchedule(scheduleWithLab);
        
        const scheduleRoom = await usis.classOnlySchedule(email, password, sessionCode);
        const classrooms = await extractClassrooms(scheduleRoom);

        const finalJson = await processScheduleWithRoom(processedScheduleWithLab, classrooms);

        return finalJson;
    } catch (error) {
        console.error("Error in getProcessedSchedule:", error);
        throw new Error("Failed to process schedule");
    }
};

