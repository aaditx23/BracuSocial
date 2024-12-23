class Course {
    constructor(course, section, faculty, classTime, classRoom, classDay, labTime = "", labRoom = "", labDay = "") {
      this.course = course;
      this.section = section;
      this.faculty = faculty;
      this.classTime = classTime;
      this.classRoom = classRoom;
      this.classDay = classDay;
      this.labTime = labTime;
      this.labRoom = labRoom;
      this.labDay = labDay;
    }
  
    toFirestore() {
      return {
        course: this.course,
        section: this.section,
        faculty: this.faculty,
        classTime: this.classTime,
        classRoom: this.classRoom,
        classDay: this.classDay,
        labTime: this.labTime,
        labRoom: this.labRoom,
        labDay: this.labDay,
      };
    }
  
    static fromFirestore(doc) {
      const data = doc.data();
      return new Course(
        data.course,
        data.section,
        data.faculty,
        data.classTime,
        data.classRoom,
        data.classDay,
        data.labTime || '',
        data.labRoom || '',
        data.labDay || ''
      );
    }
  }
  
  module.exports = Course;
  