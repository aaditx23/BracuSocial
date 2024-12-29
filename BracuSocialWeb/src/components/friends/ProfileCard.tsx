import { Profile } from "@/types/Profile";

interface ProfileCardProps {
  profile: Profile;
}

export function ProfileCard({ profile }: ProfileCardProps) {
  const courses = profile.enrolledCourses
    ? profile.enrolledCourses.split(",").map((course) => course.trim())
    : [];

  return (
    <div className="w-64 p-4">
      {/* Align the name to the start (left) */}
      <div className="text-left">
        <h3><strong>{profile.name}</strong></h3>
      </div>
      <div className="flex flex-col items-center justify-center mt-2">
        {courses.length > 0 ? (
          <div className="flex flex-wrap gap-2 justify-center">
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
      </div>
    </div>
  );
}
