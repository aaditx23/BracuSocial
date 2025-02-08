function mergeLabSchedules(processedScheduleMap) {
    const updatedSchedule = {};

    for (const key in processedScheduleMap) {
        if (key.includes("L")) {
            // This is a lab section
            const [courseCode] = key.split("L"); // Extract course code (e.g., "CSE420" from "CSE420L-05")
            const nonLabKey = Object.keys(processedScheduleMap).find(k => k.startsWith(courseCode) && !k.includes("L"));

            if (nonLabKey && updatedSchedule[nonLabKey]) {
                // If the main course exists in the updatedSchedule, add labDays
                updatedSchedule[nonLabKey].labDays = processedScheduleMap[key].classDays;
            }
        } else {
            // This is a regular course, keep it intact
            updatedSchedule[key] = { ...processedScheduleMap[key] };
        }
    }

    return updatedSchedule;
};


module.exports.processSchedule = (sectionArray) => {
    if (!Array.isArray(sectionArray)) {
        throw new Error("Invalid input: Expected an array of sections.");
    }

    function processSectionData(section) {
        if (!section || typeof section !== "object") {
            throw new Error("Invalid section data");
        }

        // Extract main fields
        const courseCode = section.courseCode;
        const sectionName = section.sectionName;
        const roomNumber = section.roomNumber;
        const faculty = section.faculties;

        // Parse the nested JSON field
        const schedule = JSON.parse(section.sectionSchedule);

        // Convert 24-hour time format to 12-hour AM/PM format
        function convertToAmPm(timeStr) {
            if (!timeStr) return null;
            const [hours, minutes] = timeStr.split(":").map(Number);
            const amPm = hours >= 12 ? "PM" : "AM";
            const formattedHours = (hours % 12 || 12).toString().padStart(2, "0"); // Ensure two digits
            const formattedMinutes = minutes.toString().padStart(2, "0"); // Ensure two digits
            return `${formattedHours}:${formattedMinutes} ${amPm}`;
        }

        // Extract schedule details
        const classDays = schedule.classSchedules.map((cls) => ({
            day: cls.day,
            startTime: convertToAmPm(cls.startTime),
            endTime: convertToAmPm(cls.endTime),
        }));

        const midExam = {
            date: schedule.midExamDate,
            startTime: convertToAmPm(schedule.midExamStartTime),
            endTime: convertToAmPm(schedule.midExamEndTime),
        };

        const finalExam = {
            date: schedule.finalExamDate,
            startTime: convertToAmPm(schedule.finalExamStartTime),
            endTime: convertToAmPm(schedule.finalExamEndTime),
        };

        // Return structured result
        return {
            courseCode,
            section: sectionName,
            roomNumber,
            faculty,
            classDays,
            midExam,
            finalExam,
        };
    }

    // Create the map-like object
    const scheduleMap = {};
    sectionArray.forEach((section) => {
        const key = `${section.courseCode}-${section.sectionName}`;
        scheduleMap[key] = processSectionData(section);
    });

    return mergeLabSchedules(scheduleMap);
};

