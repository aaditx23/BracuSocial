import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";

const ProfilePage: React.FC = () => {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

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
        const response = await fetch(
          `http://localhost:3000/api/profiles/${studentId}`
        );

        if (!response.ok) {
          throw new Error("Failed to fetch profile.");
        }

        const data = await response.json();
        setProfile(data);
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
    <div className="max-w-4xl mx-auto p-6">
      <Card className="shadow-lg">
        <CardHeader>
          <CardTitle>Profile</CardTitle>
        </CardHeader>
        {profile && (
          <CardContent>
            <div className="mb-4 space-y-2">
              <p className="text-lg font-medium"><strong>Name:</strong> {profile.name}</p>
              <p className="text-lg"><strong>Student ID:</strong> {profile.studentId}</p>
              <p className="text-lg"><strong>Email:</strong> {profile.email}</p>
            </div>

            <div className="mt-6">
              <h2 className="text-2xl font-semibold mb-4">Enrolled Courses</h2>
              {profile.enrolledCourses.length > 0 ? (
                <ul className="list-disc pl-6">
                  {profile.enrolledCourses.map((course, index) => (
                    <li key={index}>{course}</li>
                  ))}
                </ul>
              ) : (
                <p className="text-muted-foreground">No courses added yet.</p>
              )}
            </div>
          </CardContent>
        )}
      </Card>
    </div>
  );
};

export default ProfilePage;
