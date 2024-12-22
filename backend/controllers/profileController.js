const admin = require('firebase-admin');
const db = admin.firestore();

// Controller to get a profile by studentId
exports.getProfileByStudentId = async (req, res) => {
  try {
    const { studentId } = req.params;

    // Query the 'profiles' collection for the document with the given studentId as a field
    const snapshot = await db.collection('profiles').where('studentId', '==', studentId).get();

    if (snapshot.empty) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    // Assuming only one profile will be found, return the first one
    const profile = snapshot.docs[0].data();
    res.status(200).json({ id: snapshot.docs[0].id, ...profile });
  } catch (error) {
    console.error('Error fetching profile:', error);
    res.status(500).json({ error: 'Error fetching profile' });
  }
};
