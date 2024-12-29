import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import AddedCourses from "../components/preprereg/addedCourses";
import CourseList from "../components/courseList";
import { Button } from "@/components/ui/button";

const AddCourses: React.FC = () => {
  const [_, setFilteredCourses] = useState<any[]>([]);
  const [addedCourses, setAddedCourses] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPreviousCourses = async () => {
      const id = localStorage.getItem("id");
      if (!id) {
        alert("User not logged in.");
        navigate("/login");
        return;
      }

      try {
        const response = await axios.get(`http://localhost:3000/api/profile/${id}`);
        const courses = response.data?.enrolledCourses || "";
        console.log(courses)
        
        const courseArray = courses
          .split(",")
          .map((course: string) => course.trim())
          .filter((course: string) => course !== "");

        const fetchedCourses = [];
        for (const course of courseArray) {
          const [courseName, section] = course.split(" ");
          try {
            const courseResponse = await axios.post("http://localhost:3000/api/pdf/", {
              course: courseName,
              section,
            });
            if (courseResponse.data) {
              fetchedCourses.push(courseResponse.data);
            }
          } catch (err) {
            console.error(`Error fetching course: ${courseName} ${section}`, err);
          }
        }

        setAddedCourses(fetchedCourses);
      } catch (err) {
        console.error("Error fetching enrolled courses", err);
        alert("Failed to fetch previous courses.");
      } finally {
        setLoading(false);
      }
    };

    fetchPreviousCourses();
  }, [navigate]);

  const handleCourseClick = (course: any) => {
    const isAlreadyAdded = addedCourses.some(
      (addedCourse) => addedCourse._id === course._id
    );

    if (isAlreadyAdded) {
      setAddedCourses(
        addedCourses.filter((addedCourse) => addedCourse._id !== course._id)
      );
    } else {
      setAddedCourses([...addedCourses, course]);
    }
  };

  const handleSaveCourses = async () => {
    const email = localStorage.getItem("email");
    if (!email) return;

    const courseStrings = addedCourses
      .map((course) => `${course.course} ${course.section}`)
      .join(",");

    setSaving(true);
    try {
      await axios.post("http://localhost:3000/api/profile/addCourse", {
        email,
        newCourse: courseStrings,
      });
      navigate("/profile");
      alert("Courses saved successfully!");
      
    } catch (err) {
      console.error("Error saving courses", err);
      alert("Failed to save courses.");
    } finally {
      setSaving(false);
    }
  };

  const handleClearCourses = () => {
    setAddedCourses([]);
  };

  if (loading) {
    return <p>Loading courses...</p>;
  }

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

      <div className="flex justify-center space-x-4 mt-6">
        <Button onClick={handleSaveCourses} disabled={saving}>
          {saving ? "Saving..." : "Save Courses"}
        </Button>
        <Button variant="outline" onClick={handleClearCourses}>
          Clear All
        </Button>
      </div>
    </div>
  );
};

export default AddCourses;
