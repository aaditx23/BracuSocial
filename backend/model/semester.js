class Semester {
    constructor(currentSemester, nextSemester) {
      this.currentSemester = currentSemester; 
      this.nextSemester = nextSemester;       
    }
  

    toFirestore() {
      return {
        currentSemester: this.currentSemester,
        nextSemester: this.nextSemester
      };
    }
  
    static fromFirestore(doc) {
      const data = doc.data();
      return new Semester(
        data.currentSemester,
        data.nextSemester
      );
    }
  }
  
  module.exports = Semester;
  