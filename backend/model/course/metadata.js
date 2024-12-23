class Metadata {
    constructor(semester, year, usis, lastUpdate) {
      this.semester = semester;  // fall, spring, or summer
      this.year = year;          // Year in string format (e.g., "2024", "2025")
      this.usis = usis;          // Boolean value
      this.lastUpdate = lastUpdate;  // Date object
    }
  
    toFirestore() {
      return {
        semester: this.semester,
        year: this.year,
        usis: this.usis,
        lastUpdate: this.lastUpdate,
      };
    }
  
    static fromFirestore(doc) {
      const data = doc.data();
      return new Metadata(
        data.semester,
        data.year,
        data.usis,
        data.lastUpdate.toDate() // Firestore stores Date objects in Timestamp format
      );
    }
  }
  
  module.exports = Metadata;
  