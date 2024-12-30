import React, { useState } from "react";
import AddedCourses from "../components/preprereg/addedCourses";
import CourseList from "../components/courseList";
import RoutineTable from "@/components/preprereg/table";

const PrePreRegPage: React.FC = () => {
  const [_, setFilteredCourses] = useState<any[]>([]);
  const [addedCourses, setAddedCourses] = useState<any[]>([]);

  const handleCourseClick = (course: any) => {
    const isAlreadyAddedById = addedCourses.some(
      (addedCourse) => addedCourse._id === course._id
    );

    const isAlreadyAddedByCourse = addedCourses.some(
      (addedCourse) => addedCourse.course === course.course
    );

    if (isAlreadyAddedById) {
      setAddedCourses(
        addedCourses.filter((addedCourse) => addedCourse._id !== course._id)
      );
    } else if (!isAlreadyAddedByCourse) {
      setAddedCourses([...addedCourses, course]);
    }
  };

  return (
    <div className="flex flex-col w-full h-[500px]">
      <div className="flex w-full space-x-4 p-4">
        <div className="flex-1">
          <CourseList
            setFilteredCourses={setFilteredCourses}
            setLoading={() => {}}
            showList={true}
            addedCourses={addedCourses}
            handleCourseSelect={handleCourseClick}
          />
        </div>

        <div className="flex-2">
          <AddedCourses
            addedCourses={addedCourses}
            handleCourseClick={handleCourseClick}
          />
        </div>
      </div>

      <div className="flex-1 p-4">
        <RoutineTable addedCourses={addedCourses} getClash={() => {}} />
      </div>
    </div>
  );
};

export default PrePreRegPage;
