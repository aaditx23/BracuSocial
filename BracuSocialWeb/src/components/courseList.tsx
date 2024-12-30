import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { XIcon } from 'lucide-react'
import { Course } from "@/types/Course";


interface CourseListProps {
  setFilteredCourses: React.Dispatch<React.SetStateAction<Course[]>>
  setLoading: React.Dispatch<React.SetStateAction<boolean>>
  addedCourses?: Course[]
  showList: boolean
  handleCourseSelect: (course: Course) => void

}

const CourseList: React.FC<CourseListProps> = ({
  setFilteredCourses,
  setLoading,
  addedCourses = [],
  showList,
  handleCourseSelect
}) => {
  const [courses, setCourses] = useState<Course[]>([]) // The full list of courses
  const [filteredCourses, setFilteredCoursesState] = useState<Course[]>([]) // The filtered list
  const [course, setCourse] = useState('')
  const [section, setSection] = useState('')
  const [faculty, setFaculty] = useState('')
  const [room, setRoom] = useState('')
  const [day, setDay] = useState('')
  const [_, setIsLoading] = useState(true)

  useEffect(() => {
    const fetchCourses = async () => {
      setLoading(true)
      
      try {
        const response = await axios.get<Course[]>('https://bracusocial-web-backend.vercel.app/api/pdf/schedules')
        setCourses(response.data)
        setFilteredCoursesState(response.data) // Initialize filteredCourses with all data
        setFilteredCourses(response.data) // Update parent state (if needed)
      } catch (err) {
        console.error('Failed to fetch courses:', err)
      }
      setLoading(false)
      setIsLoading(false)
    }

    fetchCourses()
  }, [setFilteredCourses])

  useEffect(() => {
    filterCourses() // Re-filter courses when filter fields change
  }, [course, section, faculty, room, day])

  const filterCourses = () => {
    setLoading(true)
    setIsLoading(true)
    const filtered = courses.filter((courseItem) => {
      return (
        (course === '' || courseItem.course.toLowerCase().includes(course.toLowerCase())) &&
        (section === '' || courseItem.section.includes(section)) &&
        (faculty === '' || courseItem.faculty.toLowerCase().includes(faculty.toLowerCase())) &&
        (room === '' || 
          courseItem.classRoom.toLowerCase().includes(room.toLowerCase()) ||
          courseItem.labRoom.toLowerCase().includes(room.toLowerCase())) &&
        (day === '' || 
          courseItem.classDay.toLowerCase().includes(day.toLowerCase()) ||
          courseItem.labDay.toLowerCase().includes(day.toLowerCase()))
      )
    })

    setFilteredCoursesState(filtered) // Update filteredCourses state
    setFilteredCourses(filtered) // Update parent state (if necessary)
    setLoading(false)
    setIsLoading(false)
  }

  const resetField = (field: string) => {
    // Reset the input field based on the field name
    if (field === 'course') setCourse('')
    if (field === 'section') setSection('')
    if (field === 'faculty') setFaculty('')
    if (field === 'room') setRoom('')
    if (field === 'day') setDay('')
  }

  return (
    <div className="w-full space-y-4">
      <div className="grid grid-cols-8 gap-2">
        <div className="relative col-span-2">
          <Input
            type="text"
            placeholder="Course"
            value={course}
            onChange={(e) => setCourse(e.target.value)}
          />
          {course && (
            <button
              type="button"
              onClick={() => resetField('course')}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
            >
              <XIcon className="h-5 w-5" />
            </button>
          )}
        </div>

        <div className="relative">
          <Input
            type="text"
            placeholder="Section"
            value={section}
            onChange={(e) => setSection(e.target.value)}
          />
          {section && (
            <button
              type="button"
              onClick={() => resetField('section')}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
            >
              <XIcon className="h-5 w-5" />
            </button>
          )}
        </div>

        <div className="relative">
          <Input
            type="text"
            placeholder="Faculty"
            value={faculty}
            onChange={(e) => setFaculty(e.target.value)}
          />
          {faculty && (
            <button
              type="button"
              onClick={() => resetField('faculty')}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
            >
              <XIcon className="h-5 w-5" />
            </button>
          )}
        </div>

        <div className="relative col-span-2">
          <Input
            type="text"
            placeholder="Room"
            value={room}
            onChange={(e) => setRoom(e.target.value)}
          />
          {room && (
            <button
              type="button"
              onClick={() => resetField('room')}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
            >
              <XIcon className="h-5 w-5" />
            </button>
          )}
        </div>

        <div className="relative col-span-2">
          <Input
            type="text"
            placeholder="Day"
            value={day}
            onChange={(e) => setDay(e.target.value)}
          />
          {day && (
            <button
              type="button"
              onClick={() => resetField('day')}
              className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
            >
              <XIcon className="h-5 w-5" />
            </button>
          )}
        </div>
      </div>

      {showList && (
        
        <div className="h-[30vh] w-full border rounded-md overflow-y-auto">
        <div className="w-full mt-5">
  {/* Table with sticky header */}
  <div className="relative w-full border rounded-md">
    <Table className="w-full">
      <TableHeader className="sticky top-0 bg-white z-10 shadow">
        <TableRow>
          <TableHead className="w-[10%] text-center">Course</TableHead>
          <TableHead className="w-[10%] text-center">Section</TableHead>
          <TableHead className="w-[10%] text-center">Faculty</TableHead>
          <TableHead className="w-[15%] text-center">Class Time</TableHead>
          <TableHead className="w-[10%] text-center">Class Room</TableHead>
          <TableHead className="w-[10%] text-center">Class Day</TableHead>
          <TableHead className="w-[15%] text-center">Lab Time</TableHead>
          <TableHead className="w-[10%] text-center">Lab Room</TableHead>
          <TableHead className="w-[10%] text-center">Lab Day</TableHead>
        </TableRow>
      </TableHeader>
    </Table>

    {/* Scrollable Table Body */}
    <div className="h-[50vh] overflow-y-auto">
      <Table className="w-full">
        <TableBody>
          {filteredCourses.map((courseItem) => (
            <TableRow
              key={courseItem._id}
              onClick={() => handleCourseSelect(courseItem)}
              className={`cursor-pointer transition-colors ${
                addedCourses.some(
                  (addedCourse) => addedCourse.course === courseItem.course
                )
                  ? "bg-green-500/30 text-black hover:bg-green-500/50"
                  : "hover:bg-muted"
              }`}
            >
              <TableCell className="w-[10%] text-center">
                {courseItem.course}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.section}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.faculty}
              </TableCell>
              <TableCell className="w-[15%] text-center">
                {courseItem.classTime}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.classRoom}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.classDay}
              </TableCell>
              <TableCell className="w-[15%] text-center">
                {courseItem.labTime || "-"}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.labRoom || "-"}
              </TableCell>
              <TableCell className="w-[10%] text-center">
                {courseItem.labDay || "-"}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  </div>
</div>

      </div>
      
        
      )}
    </div>
  )
}

export default CourseList