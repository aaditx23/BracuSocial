import React, { useState } from "react";
import AddedCourses from "../components/preprereg/addedCourses";
import CourseDetailsCard from "../components/preprereg/courseDetailsCard";
import CourseList from "../components/courseList";

const PrePreRegPage: React.FC = () => {
  const [filteredCourses, setFilteredCourses] = useState<any[]>([]);
  const [addedCourses, setAddedCourses] = useState<any[]>([]);

  // Double-click handler: Add or remove course
  const handleCourseClick = (course: any) => {
    const isAlreadyAddedById = addedCourses.some(
      (addedCourse) => addedCourse._id === course._id
    );
  
    const isAlreadyAddedByCourse = addedCourses.some(
      (addedCourse) => addedCourse.course === course.course
    );
  
    if (isAlreadyAddedById) {
      // Remove course if already added by ID
      setAddedCourses(
        addedCourses.filter((addedCourse) => addedCourse._id !== course._id)
      );
    } else if (!isAlreadyAddedByCourse) {
      // Add course if not in the list by course
      setAddedCourses([...addedCourses, course]);
    }
  };
  

  return (
    <div className="flex w-full h-[500px]">

      {/* Course List (Search and Browse) */}
      <div className="flex-1 p-4">
        <CourseList
          setFilteredCourses={setFilteredCourses}
          setLoading={() => {}}
          showList={true}
          addedCourses={addedCourses}
          handleCourseSelect={handleCourseClick} // Pass single-click handler
        />
      </div>

      {/* Added Courses Column */}
      <div className="flex-1 p-4">
        <AddedCourses
          addedCourses={addedCourses}
          handleCourseClick={handleCourseClick}
        />
      </div>
    </div>
  );
};

export default PrePreRegPage;
