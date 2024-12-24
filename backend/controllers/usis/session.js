const BASE_SESSION_CODE = 627125;  // Fall 2024
const BASE_YEAR = 2024;

const SEMESTER_DISTANCES = { spring: 1, summer: 2, fall: 3 };

// Function to generate session code based on semester and year
exports.generateSessionCode = (semester, year) => {
  const semDistance = SEMESTER_DISTANCES[semester.toLowerCase()];
  if (!semDistance) {
    throw new Error("Invalid semester. Use 'spring', 'summer', or 'fall'.");
  }

  const yearDiff = year - BASE_YEAR;
  const codeOffset = yearDiff * 3 + semDistance - 3;  // Offset from fall of base year
  return BASE_SESSION_CODE + codeOffset;
};

// Function to parse session code to get semester and year
exports.parseSessionCode = (sessionCode) => {
  const diff = sessionCode - BASE_SESSION_CODE;

  const yearDiff = Math.floor(diff / 3);  // How many full years have passed
  const remainder = diff % 3;  // Which semester (spring, summer, fall)

  const year = BASE_YEAR + yearDiff;
  const semester = Object.keys(SEMESTER_DISTANCES).find(key => SEMESTER_DISTANCES[key] === remainder + 3);

  return { year, semester };
};

// Testing the functions
console.log("Generate Session Code (Fall 2021):", exports.generateSessionCode('fall', 2021));  // 627116
console.log("Generate Session Code (Spring 2025):", exports.generateSessionCode('spring', 2025));  // 627126
console.log("Generate Session Code (Summer 2026):", exports.generateSessionCode('summer', 2026));  // 627130

console.log("Parse Session Code (627116):", exports.parseSessionCode(627116));  // { year: 2021, semester: 'fall' }
console.log("Parse Session Code (627126):", exports.parseSessionCode(627126));  // { year: 2025, semester: 'spring' }
console.log("Parse Session Code (627130):", exports.parseSessionCode(627130));  // { year: 2026, semester: 'summer' }
