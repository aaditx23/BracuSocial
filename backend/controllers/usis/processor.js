exports.processSchedule = function(response) {
    const demoDb = {};

    response.rows.forEach(entry => {
        // Extract relevant fields from the 'cell' array
        const courseCode = entry.cell[1];
        const section = entry.cell[3];
        const key = `${courseCode}-${section}`;
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

    return demoDb;
};

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
