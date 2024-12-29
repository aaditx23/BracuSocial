import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";

interface ProfileCardProps {
  profile: Profile;
}

export function ProfileCard({ profile }: ProfileCardProps) {
  const courses = profile.enrolledCourses
    ? profile.enrolledCourses.split(",").map((course) => course.trim())
    : [];

  return (
    <Card className="w-64">
      <CardHeader>
        <CardTitle>{profile.name}</CardTitle>
      </CardHeader>
      <CardContent>
        {courses.length > 0 ? (
          <div className="flex flex-wrap gap-2">
            {courses.map((course, index) => (
              <div
                key={index}
                className="bg-blue-500 text-white text-xs py-1 px-3 rounded-full"
              >
                {course}
              </div>
            ))}
          </div>
        ) : (
          <p>No courses added yet</p>
        )}
      </CardContent>
    </Card>
  );
}
