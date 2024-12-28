import React, { useState } from "react";
import AddedCourses from "../components/preprereg/addedCourses";
import CourseList from "../components/courseList";
import RoutineTable from "@/components/preprereg/table";

const PrePreRegPage: React.FC = () => {
  const [_, setFilteredCourses] = useState<any[]>([]);
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
    <div className="flex flex-col w-full h-[500px]">

      {/* Row 1: Course List and Added Courses */}
      <div className="flex w-full space-x-4 p-4">
        {/* Course List (Search and Browse) */}
        <div className="flex-1">
          <CourseList
            setFilteredCourses={setFilteredCourses}
            setLoading={() => {}}
            showList={true}
            addedCourses={addedCourses}
            handleCourseSelect={handleCourseClick} // Pass single-click handler
          />
        </div>

        {/* Added Courses Column */}
        <div className="flex-2">
          <AddedCourses
            addedCourses={addedCourses}
            handleCourseClick={handleCourseClick}
          />
        </div>
      </div>

      {/* Row 2: Course Schedule Table */}
      <div className="flex-1 p-4">
        <RoutineTable 
        addedCourses={addedCourses}
        getClash={() => {} } />
      </div>
    </div>
  );
};

export default PrePreRegPage;
