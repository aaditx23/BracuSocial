import React, { useState, useEffect } from 'react'
import axios from 'axios'
import { Input } from "@/components/ui/input"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { ScrollArea } from "@/components/ui/scroll-area"
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

  useEffect(() => {
    const fetchCourses = async () => {
      setLoading(true)
      try {
        const response = await axios.get<Course[]>('http://localhost:3000/api/pdf/schedules')
        setCourses(response.data)
        setFilteredCoursesState(response.data) // Initialize filteredCourses with all data
        setFilteredCourses(response.data) // Update parent state (if needed)
      } catch (err) {
        console.error('Failed to fetch courses:', err)
      }
      setLoading(false)
    }

    fetchCourses()
  }, [setFilteredCourses, setLoading])

  useEffect(() => {
    filterCourses() // Re-filter courses when filter fields change
  }, [course, section, faculty, room, day])

  const filterCourses = () => {
    setLoading(true)
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
        <ScrollArea className="h-[30vh] w-full border rounded-md">
          <Table>
            <TableHeader className="sticky top-0 bg-background z-10">
              <TableRow>
                <TableHead>Course</TableHead>
                <TableHead>Section</TableHead>
                <TableHead>Faculty</TableHead>
                <TableHead>Class Room</TableHead>
                <TableHead>Class Day</TableHead>
                <TableHead>Class Time</TableHead>
                <TableHead>Lab Room</TableHead>
                <TableHead>Lab Day</TableHead>
                <TableHead>Lab Time</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredCourses.map((courseItem) => ( // Bind to filteredCourses
                <TableRow
                  key={courseItem._id}
                  onClick={() => handleCourseSelect(courseItem)}
                  className={`cursor-pointer transition-colors ${
                    addedCourses.some((addedCourse) => addedCourse.course === courseItem.course)
                      ? 'bg-green-500/30 text-black hover:bg-green-500/50'
                      : 'hover:bg-muted'
                  }`}
                  
                >
                  <TableCell>{courseItem.course}</TableCell>
                  <TableCell>{courseItem.section}</TableCell>
                  <TableCell>{courseItem.faculty}</TableCell>
                  <TableCell>{courseItem.classRoom}</TableCell>
                  <TableCell>{courseItem.classDay}</TableCell>
                  <TableCell>{courseItem.classTime}</TableCell>
                  <TableCell>{courseItem.labRoom}</TableCell>
                  <TableCell>{courseItem.labDay}</TableCell>
                  <TableCell>{courseItem.labTime}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </ScrollArea>
      )}
    </div>
  )
}

export default CourseList
