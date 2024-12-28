import React from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Course } from "@/types/Course";


interface AddedCoursesProps {
  addedCourses: Course[];
  handleCourseClick: (course: Course) => void;
}

const AddedCourses: React.FC<AddedCoursesProps> = ({
  addedCourses,
  handleCourseClick: handleCourseClick,
}) => {
  return (
    <Card className="h-[35vh] bg-gray-100 flex flex-col">
      <CardHeader>
        <CardTitle className="font-bold">Added Courses</CardTitle>
      </CardHeader>
      <CardContent className="overflow-y-auto flex-grow">
        <ul className="space-y-2">
          {addedCourses.map((course) => (
            <li
              key={course._id}
              className="cursor-pointer bg-green-500 text-white px-4 py-2 rounded-md transition-colors hover:bg-green-600"
              onClick={() => handleCourseClick(course)}
            >
              {course.course} - {course.section}
            </li>
          ))}
        </ul>
      </CardContent>
    </Card>

  );
};

export default AddedCourses;
