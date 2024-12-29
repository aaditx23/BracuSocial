import React, { useEffect, useState } from "react";
import axios from "axios";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import AddCourses from "@/components/addCourses";
import { Button } from "@/components/ui/button";

const ProfilePage: React.FC = () => {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    const studentId = localStorage.getItem("id");
    const email = localStorage.getItem("email");

    if (!studentId || !email) {
      setError("User not logged in.");
      setLoading(false);
      return;
    }

    const fetchProfile = async () => {
      try {
        const response = await axios.get(
          `http://localhost:3000/api/profile/${studentId}`
        );
        setProfile(response.data);
      } catch (err) {
        setError("Error fetching profile.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, []);

  if (loading) {
    return <p>Loading profile...</p>;
  }

  if (error) {
    return <p className="text-red-600">{error}</p>;
  }

  return (
    <div className="mx-auto p-6">
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle>Profile</CardTitle>
        </CardHeader>
        {profile && (
          <CardContent>
            <div className="mb-4 space-y-2">
              <p className="text-lg font-medium">
                <strong>Name:</strong> {profile.name}
              </p>
              <p className="text-lg">
                <strong>Student ID:</strong> {profile.studentId}
              </p>
              <p className="text-lg">
                <strong>Email:</strong> {profile.email}
              </p>
            </div>

            <div className="mt-6">
              <h2 className="text-2xl font-semibold mb-4">Enrolled Courses</h2>
              {isEditing ? (
                <div>
                  <AddCourses />
                  <div className="mt-4 flex space-x-4">
                    <Button variant="outline" onClick={() => setIsEditing(false)}>
                      Cancel
                    </Button>
                  </div>
                </div>
              ) : profile.enrolledCourses.length > 0 ? (
                <div>
                  <ul className="list-disc pl-6">
                    {profile.enrolledCourses
                      .split(",")
                      .filter((course) => course.trim() !== "")
                      .map((course, index) => (
                        <li key={index}>{course}</li>
                      ))}
                  </ul>
                  <div className="mt-4">
                    <Button onClick={() => setIsEditing(true)}>Edit Courses</Button>
                  </div>
                </div>
              ) : (
                <div className="text-muted-foreground">
                  <p className="mb-4">No courses added yet.</p>
                  <AddCourses />
                </div>
              )}
            </div>
          </CardContent>
        )}
      </Card>
    </div>
  );
};

export default ProfilePage;
