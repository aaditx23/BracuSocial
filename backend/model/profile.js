class Profile {
    constructor(email, studentId, password, studentName, enrolledCourses = [], addedFriends = [], friendRequests = [], profilePicture = '') {
      this.email = email;
      this.studentId = studentId;
      this.password = password;
      this.studentName = studentName;
      this.enrolledCourses = enrolledCourses;
      this.addedFriends = addedFriends;
      this.friendRequests = friendRequests;
      this.profilePicture = profilePicture;
    }
  
    toFirestore() {
      return {
        email: this.email,
        studentId: this.studentId,
        password: this.password,
        studentName: this.studentName,
        enrolledCourses: this.enrolledCourses,
        addedFriends: this.addedFriends,
        friendRequests: this.friendRequests,
        profilePicture: this.profilePicture,
      };
    }
  
    static fromFirestore(doc) {
      const data = doc.data();
      return new Profile(
        data.email,
        data.studentId,
        data.password,
        data.studentName,
        data.enrolledCourses || [],
        data.addedFriends || [],
        data.friendRequests || [],
        data.profilePicture || ''
      );
    }
  }
  
  module.exports = Profile;
  