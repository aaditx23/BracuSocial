import React, { useState } from "react";
import CourseList from "../components/courseList";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { Course } from "@/types/Course";

const AllCourses: React.FC = () => {
  const [filteredCourses, setFilteredCourses] = useState<Course[]>([]);
  const [loading, setLoading] = useState(true);

  return (
    <div className="container mx-auto px-4 py-8 flex flex-col items-center">
      <h1 className="text-3xl font-bold mb-6">All Courses</h1>

      <CourseList
        setFilteredCourses={setFilteredCourses}
        setLoading={setLoading}
        showList={false}
        handleCourseSelect={(course) => {
          console.log("Course selected:", course);
        }}
        addedCourses={[]}
      />

      {loading ? (
        <div className="flex justify-center mt-8">
          <p className="text-center">Loading schedule...</p>
        </div>
      ) : filteredCourses.length > 0 ? (
        <div className="w-full mt-5">
          {/* Table with sticky header */}
          <div className="relative w-full border rounded-md">
            <Table className="w-full">
              <TableHeader className="sticky top-0 bg-white z-10 shadow">
                <TableRow>
                  <TableHead className="w-[10%] text-center">Course</TableHead>
                  <TableHead className="w-[10%] text-center">Section</TableHead>
                  <TableHead className="w-[10%] text-center">Faculty</TableHead>
                  <TableHead className="w-[15%] text-center">
                    Class Time
                  </TableHead>
                  <TableHead className="w-[10%] text-center">
                    Class Room
                  </TableHead>
                  <TableHead className="w-[10%] text-center">
                    Class Day
                  </TableHead>
                  <TableHead className="w-[15%] text-center">
                    Lab Time
                  </TableHead>
                  <TableHead className="w-[10%] text-center">
                    Lab Room
                  </TableHead>
                  <TableHead className="w-[10%] text-center">Lab Day</TableHead>
                </TableRow>
              </TableHeader>
            </Table>

            {/* Scrollable Table Body */}
            <div className="h-[50vh] overflow-y-auto">
              <Table className="w-full">
                <TableBody>
                  {filteredCourses.map((course, index) => (
                    <TableRow key={index}>
                      <TableCell className="w-[10%] text-center">
                        {course.course}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.section}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.faculty}
                      </TableCell>
                      <TableCell className="w-[15%] text-center">
                        {course.classTime}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.classRoom}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.classDay}
                      </TableCell>
                      <TableCell className="w-[15%] text-center">
                        {course.labTime || "-"}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.labRoom || "-"}
                      </TableCell>
                      <TableCell className="w-[10%] text-center">
                        {course.labDay || "-"}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </div>
        </div>
      ) : (
        <Alert variant="default" className="mt-6">
          <AlertDescription>No courses found.</AlertDescription>
        </Alert>
      )}
    </div>
  );
};

export default AllCourses;
