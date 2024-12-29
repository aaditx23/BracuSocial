import { Profile } from "@/types/Profile";
import ImagePreview from "../image/imagePreview";

interface ProfileCardProps {
  profile: Profile;
}

export function ProfileCard({ profile }: ProfileCardProps) {
  const courses = profile.enrolledCourses
    ? profile.enrolledCourses.split(",").map((course) => course.trim())
    : [];

  return (
    <div className="p-4 ">
      <div className="flex items-center mb-4">
          <ImagePreview 
          base64String={profile.profilePicture || ""}
          size={50}
           />

        <div className="text-left mx-5">
          <h3 className="text-xl font-bold">{profile.name}</h3>
        </div>
      </div>

      <div className="flex flex-col items-center justify-center mt-4">
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
