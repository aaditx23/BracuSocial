const admin = require('firebase-admin');
const db = admin.firestore();
const pdfUtil = require("./pdf/processor")
const Metadata = require('../model/course/metadata');

exports.populate = async (req, res) => {
    try {
        const semesterDoc = await db.collection('semester').doc('semester').get();
        
        if (!semesterDoc.exists) {
            return res.status(404).json({ message: "No semester data found in Firestore." });
        }

        const semesterData = semesterDoc.data();
        const currentSemester = semesterData.currentSemester;
        const [currentSemesterName, currentYear] = currentSemester.split(" ");
        
        const schedule = await pdfUtil.getCurrentSchedule(currentSemesterName, currentYear);
        
        if (schedule.error) {
            return res.status(404).json({ message: schedule.error });
        }

        const batch = db.batch();

        // Create Metadata entry
        const metadata = new Metadata(
            currentSemesterName,
            currentYear,
            false,  
            new Date() 
        );
        
        const metadataRef = db.collection('pdfSchedule').doc('metadata');
        batch.set(metadataRef, metadata.toFirestore());

        schedule.forEach(item => {
            const docName = `${item.course} ${item.section}`;
            const docRef = db.collection('pdfSchedule').doc(docName);
            batch.set(docRef, item);
        });

        await batch.commit();

        res.status(200).json({ message: "Schedule populated successfully!" });

    } catch (error) {
        console.error("Error populating schedule:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.getScheduleMetadata = async (req, res) => {
    try {
        const metadataDoc = await db.collection('pdfSchedule').doc('metadata').get();
        
        if (!metadataDoc.exists) {
            return res.status(404).json({ message: "Metadata not found in Firestore." });
        }

        const metadata = Metadata.fromFirestore(metadataDoc);

        res.status(200).json(metadata);
        
    } catch (error) {
        console.error("Error fetching metadata:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.schedules = async (req, res) => {
    try {
        const snapshot = await db.collection('pdfSchedule').get();
        
        if (snapshot.empty) {
            return res.status(404).json({ message: "No schedules found in Firestore." });
        }

        const schedules = [];
        
        snapshot.forEach(doc => {
            if (doc.id !== 'metadata') {
                schedules.push({ id: doc.id, ...doc.data() });
            }
        });

        res.status(200).json(schedules);

    } catch (error) {
        console.error("Error fetching schedules:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};

exports.getAll = async (req, res) => {
    try {
        const snapshot = await db.collection('pdfSchedule').get();
        
        if (snapshot.empty) {
            return res.status(404).json({ message: "No schedules found in Firestore." });
        }

        const schedules = [];
        
        snapshot.forEach(doc => {
            schedules.push({ id: doc.id, ...doc.data() });
        });

        res.status(200).json(schedules);

    } catch (error) {
        console.error("Error fetching schedules:", error);
        res.status(500).json({ message: "Internal server error" });
    }
};
