const admin = require('firebase-admin');
const bcrypt = require('bcrypt');
const db = admin.firestore();
const Profile = require('../model/profile');

exports.getProfileByStudentId = async (req, res) => {
  try {
    const { studentId } = req.params;

    const snapshot = await db.collection('profiles')
      .where('studentId', '==', studentId)
      .get();

    if (snapshot.empty) {
      return res.status(404).json({ message: 'Profile not found' });
    }

    const profileDoc = snapshot.docs[0];
    const profile = Profile.fromFirestore(profileDoc);

    res.status(200).json(profile);
  } catch (error) {
    console.error('Error fetching profile:', error);
    res.status(500).json({ error: 'Error fetching profile' });
  }
};

exports.register = async (req, res) => {
  try {
    const { studentId, email, name, password } = req.body;

    if (!studentId || !email || !name || !password) {
      return res.status(400).json({ message: 'All fields are required' });
    }

    const hashedPassword = await bcrypt.hash(password, 10);

    const newProfile = new Profile(
      email,
      studentId,
      hashedPassword,
      name
    );

    await db.collection('profiles').doc(email).set(newProfile.toFirestore());

    res.status(201).json({ message: 'User registered successfully' });
  } catch (error) {
    console.error('Error registering user:', error);
    res.status(500).json({ error: 'Error registering user' });
  }
};

exports.login = async (req, res) => {
  try {
    const { identifier, password } = req.body;
    const isEmail = identifier.includes('@');
    const queryField = isEmail ? 'email' : 'studentId';

    const snapshot = await db
      .collection('profiles')
      .where(queryField, '==', identifier)
      .get();

    if (snapshot.empty) {
      return res.status(404).json({ message: 'User not found' });
    }

    const userDoc = snapshot.docs[0];
    const user = Profile.fromFirestore(userDoc);

    const isPasswordValid = await bcrypt.compare(password, user.password);

    if (!isPasswordValid) {
      return res.status(401).json({ message: 'Invalid password' });
    }

    res.status(200).json({
      message: 'Login successful',
      user: user
    });
  } catch (error) {
    console.error('Error logging in user:', error);
    res.status(500).json({ error: 'Error logging in user' });
  }
};
