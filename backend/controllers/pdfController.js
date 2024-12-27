const mongoose = require('mongoose');
const pdfUtil = require("./pdf/processor");
const Semester = require('../model/semester');
const PdfCourse = require('../model/pdfCourse');

exports.populate = async (req, res) => {
    try {
        const semesterDoc = await Semester.findOne();
        console.log(semesterDoc)
        
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
    console.log("fetching schedules")
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
