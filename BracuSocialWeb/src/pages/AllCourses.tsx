import React, { useState } from 'react'
import CourseList from '../components/courseList'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { Loader2 } from 'lucide-react'
import { Course } from "@/types/Course";


const AllCourses: React.FC = () => {
  const [filteredCourses, setFilteredCourses] = useState<Course[]>([])
  const [loading, setLoading] = useState(true)

  return (
    <div className="container mx-auto px-4 py-8 flex flex-col items-center">
      <h1 className="text-3xl font-bold mb-6">All Courses</h1>

      <CourseList 
        setFilteredCourses={setFilteredCourses} 
        setLoading={setLoading}   
        showList={false}
        handleCourseSelect={(course) => {
          console.log('Course selected:', course);
        }}
        addedCourses={[]} 
      />

      {loading ? (
        <div className="flex justify-center mt-8">
          <Loader2 className="h-8 w-8 animate-spin" />
        </div>
      ) : filteredCourses.length > 0 ? (
        <div className="w-full mt-5">
          {/* Table with sticky header */}
          <div className="relative w-full border rounded-md">
            <Table className="w-full">
              <TableHeader className="sticky top-0 bg-white z-10 shadow">
                <TableRow>
                  <TableHead>Course</TableHead>
                  <TableHead>Section</TableHead>
                  <TableHead>Faculty</TableHead>
                  <TableHead>Class Time</TableHead>
                  <TableHead>Class Room</TableHead>
                  <TableHead>Class Day</TableHead>
                  <TableHead>Lab Time</TableHead>
                  <TableHead>Lab Room</TableHead>
                  <TableHead>Lab Day</TableHead>
                </TableRow>
              </TableHeader>
            </Table>

            {/* Scrollable Table Body */}
            <div className="h-[50vh] overflow-y-auto">
              <Table className="w-full">
                <TableBody>
                  {filteredCourses.map((course, index) => (
                    <TableRow key={index}>
                      <TableCell>{course.course}</TableCell>
                      <TableCell>{course.section}</TableCell>
                      <TableCell>{course.faculty}</TableCell>
                      <TableCell>{course.classTime}</TableCell>
                      <TableCell>{course.classRoom}</TableCell>
                      <TableCell>{course.classDay}</TableCell>
                      <TableCell>{course.labTime || '-'}</TableCell>
                      <TableCell>{course.labRoom || '-'}</TableCell>
                      <TableCell>{course.labDay || '-'}</TableCell>
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
  )
}

export default AllCourses
