import React, { useState, useEffect } from 'react';
import { Form, Row, Col } from 'react-bootstrap';
import axios from 'axios';

const SearchCourse = ({ setFilteredCourses, setLoading }) => {
  const [courses, setCourses] = useState([]);
  const [query, setQuery] = useState({
    course: '',
    section: '',
    faculty: '',
    room: '',
    day: ''
  });

  useEffect(() => {
    const fetchCourses = async () => {
      setLoading(true);  // Start loading
      try {
        const response = await axios.get('http://localhost:3000/api/pdf/schedules');
        setCourses(response.data);
        setFilteredCourses(response.data);
      } catch (err) {
        console.error('Failed to fetch courses:', err);
      }
      setLoading(false);  // Stop loading
    };

    fetchCourses();
  }, [setFilteredCourses, setLoading]);

  const filterCourses = () => {
    setLoading(true);  // Show loading while filtering
    const filtered = courses.filter((course) => {
      return (
        (query.course === '' || course.course.toLowerCase().includes(query.course.toLowerCase())) &&
        (query.section === '' || course.section.includes(query.section)) &&
        (query.faculty === '' || course.faculty.toLowerCase().includes(query.faculty.toLowerCase())) &&
        (query.room === '' || 
          course.classRoom.toLowerCase().includes(query.room.toLowerCase()) ||
          course.labRoom.toLowerCase().includes(query.room.toLowerCase())) &&
        (query.day === '' || 
          course.classDay.toLowerCase().includes(query.day.toLowerCase()) ||
          course.labDay.toLowerCase().includes(query.day.toLowerCase()))
      );
    });

    setFilteredCourses(filtered);
    setLoading(false);  // Hide loading after filtering
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setQuery({ ...query, [name]: value });
    filterCourses();
  };

  return (
    <Form className="w-100 mb-4">
      <Row className="g-3">
        <Col md={3}>
          <Form.Control 
            type="text" 
            placeholder="Course" 
            name="course"
            value={query.course} 
            onChange={handleChange} 
          />
        </Col>
        <Col md={2}>
          <Form.Control 
            type="text" 
            placeholder="Section" 
            name="section"
            value={query.section} 
            onChange={handleChange} 
          />
        </Col>
        <Col md={3}>
          <Form.Control 
            type="text" 
            placeholder="Faculty" 
            name="faculty"
            value={query.faculty} 
            onChange={handleChange} 
          />
        </Col>
        <Col md={2}>
          <Form.Control 
            type="text" 
            placeholder="Room" 
            name="room"
            value={query.room} 
            onChange={handleChange} 
          />
        </Col>
        <Col md={2}>
          <Form.Control 
            type="text" 
            placeholder="Day" 
            name="day"
            value={query.day} 
            onChange={handleChange} 
          />
        </Col>
      </Row>
    </Form>
  );
};

export default SearchCourse;
