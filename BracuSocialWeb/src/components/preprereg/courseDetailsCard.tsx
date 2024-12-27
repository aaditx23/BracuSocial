import React from "react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Course } from "@/types/Course";

interface CourseDetailsCardProps {
  course: Course;
}

const CourseDetailsCard: React.FC<CourseDetailsCardProps> = ({ course }) => {
  return (
    <Card>
      <CardHeader>
        <CardTitle>
          {course.course} - {course.section}
        </CardTitle>
      </CardHeader>
      <CardContent>
        <ul className="space-y-1">
          <li>
            <strong>Faculty:</strong> {course.faculty}
          </li>
          <li>
            <strong>Classroom:</strong> {course.classRoom}
          </li>
          <li>
            <strong>Class Day:</strong> {course.classDay}
          </li>
          <li>
            <strong>Lab Room:</strong> {course.labRoom}
          </li>
          <li>
            <strong>Lab Day:</strong> {course.labDay}
          </li>
        </ul>
      </CardContent>
    </Card>
  );
};

export default CourseDetailsCard;
