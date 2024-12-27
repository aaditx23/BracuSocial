import React, { useState } from 'react';
import { Table, Spinner, Alert, Container } from 'react-bootstrap';
import SearchCourse from '../components/searchCourse';

const AllCourses = () => {
  const [filteredCourses, setFilteredCourses] = useState([]);
  const [loading, setLoading] = useState(true);  // Loading state

  return (
    <Container 
      className="d-flex flex-column justify-content-center align-items-center" 
      style={{ height: '100vh' }}
    >
      <h1 className="mb-4">All Courses</h1>

      {/* Filter/Search Component */}
      <SearchCourse 
        setFilteredCourses={setFilteredCourses} 
        setLoading={setLoading}   // Pass loading handler to Search component
      />

      {/* Render List or Loading Spinner */}
      {loading ? (
        <Spinner animation="border" role="status" className="mt-4">
          <span className="visually-hidden">Loading...</span>
        </Spinner>
      ) : filteredCourses.length > 0 ? (
        <Table striped bordered hover responsive className="mt-3">
          <thead>
            <tr>
              <th>Course</th>
              <th>Section</th>
              <th>Faculty</th>
              <th>Class Time</th>
              <th>Class Room</th>
              <th>Class Day</th>
              <th>Lab Time</th>
              <th>Lab Room</th>
              <th>Lab Day</th>
            </tr>
          </thead>
          <tbody>
            {filteredCourses.map((course, index) => (
              <tr key={index}>
                <td>{course.course}</td>
                <td>{course.section}</td>
                <td>{course.faculty}</td>
                <td>{course.classTime}</td>
                <td>{course.classRoom}</td>
                <td>{course.classDay}</td>
                <td>{course.labTime || '-'}</td>
                <td>{course.labRoom || '-'}</td>
                <td>{course.labDay || '-'}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      ) : (
        <Alert variant="info" className="mt-3">
          No courses found.
        </Alert>
      )}
    </Container>
  );
};

export default AllCourses;
