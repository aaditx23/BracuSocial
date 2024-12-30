import React, { useEffect, useState } from "react";
import axios from "axios";
import {
  Routine,
  RoutineFromCourse,
  ProfileWithRoutine,
} from "@/types/Routine";
import RoutineTable from "./routineTable";

interface RoutineParentProps {
  showFriends?: boolean;
}

const RoutineParent: React.FC<RoutineParentProps> = ({
  showFriends = true,
}) => {
  const [routineList, setRoutineList] = useState<Routine[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [friendMap, setFriendMap] = useState<Map<string, Routine[]>>(new Map());
  const [selectedFriend, setSelectedFriend] = useState<string>("All Friends");
  const [includeSelf, setIncludeSelf] = useState<boolean>(true);
  const [selfRoutine, setSelfRoutine] = useState<Routine[]>([]);

  useEffect(() => {
    const fetchRoutine = async () => {
      try {
        setLoading(true);
        setError(null);

        const routineDataMap: Map<string, Routine[]> = new Map();
        const myRoutine: Routine[] = [];
        const currentStudentId = localStorage.getItem("id");

        if (!currentStudentId) {
          throw new Error("No student ID found in local storage.");
        }

        // Fetch current user's profile
        const { data: currentProfile } = await axios.get(
          `https://bracusocial-web-backend.vercel.app/api/profile/${currentStudentId}`
        );
        const currentEnrolledCourses = currentProfile.enrolledCourses
          ? currentProfile.enrolledCourses
              .split(",")
              .map((c: string) => c.trim())
          : [];

        // Process current user's routine
        for (const courseString of currentEnrolledCourses) {
          const [courseCode, section] = courseString.split(" ");
          const { data: course } = await axios.post(
            "https://bracusocial-web-backend.vercel.app/api/pdf/getCourse",
            {
              course: courseCode.trim(),
              section: section.trim(),
            }
          );
          const routine = RoutineFromCourse(course, course.room);
          myRoutine.push(
            ProfileWithRoutine(
              currentProfile.name,
              currentProfile.profilePicture,
              routine
            )
          );
        }

        // Add self routine to "All Friends"
        routineDataMap.set("All Friends", [...myRoutine]);
        setSelfRoutine(myRoutine);

        // If showFriends is true, process routines for added friends
        if (showFriends) {
          const addedFriends = currentProfile.addedFriends
            ? currentProfile.addedFriends
                .split(",")
                .map((id: string) => id.trim())
            : [];

          for (const friendId of addedFriends) {
            const { data: friendProfile } = await axios.get(
              `https://bracusocial-web-backend.vercel.app/api/profile/${friendId}`
            );
            const friendEnrolledCourses = friendProfile.enrolledCourses
              ? friendProfile.enrolledCourses
                  .split(",")
                  .map((c: string) => c.trim())
              : [];

            const friendRoutine: Routine[] = [];
            for (const courseString of friendEnrolledCourses) {
              const [courseCode, section] = courseString.split(" ");
              const { data: course } = await axios.post(
                "https://bracusocial-web-backend.vercel.app/api/pdf/getCourse",
                {
                  course: courseCode.trim(),
                  section: section.trim(),
                }
              );
              const routine = RoutineFromCourse(course, course.room);
              friendRoutine.push(
                ProfileWithRoutine(
                  friendProfile.name,
                  friendProfile.profilePicture,
                  routine
                )
              );
            }

            // Add friend's routine to their own key, along with selfRoutine
            routineDataMap.set(friendProfile.name, [
              ...friendRoutine,
              ...myRoutine,
            ]);

            // Add friend's routine to "All Friends" (including self routine)
            routineDataMap.set("All Friends", [
              ...(routineDataMap.get("All Friends") || []), // Existing "All Friends" routines
              ...friendRoutine, // New friend's routines
            ]);
          }
        }

        setFriendMap(routineDataMap);
        setRoutineList(routineDataMap.get("All Friends") || []);
      } catch (err: any) {
        setError(err.response?.data?.message || "Error fetching routine data.");
      } finally {
        setLoading(false);
      }
    };

    fetchRoutine();
  }, [showFriends]);

  useEffect(() => {
    // If showFriends is true and the selected friend is changed
    if (friendMap.has(selectedFriend)) {
      let selectedRoutine = friendMap.get(selectedFriend) || [];

      // If the toggle is off, remove self routine from the list
      if (!includeSelf) {
        selectedRoutine = selectedRoutine.filter(
          (routine) => routine.name !== localStorage.getItem("name")
        );
      }

      setRoutineList(selectedRoutine);
    }
  }, [selectedFriend, includeSelf, friendMap]);

  const handleFriendChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedFriend(e.target.value);
  };

  const handleSwitchChange = () => {
    setIncludeSelf(!includeSelf);
  };
  const filterRoutineList = (): Routine[] => {
    if (includeSelf) {
      // Include self routine: Add any missing elements from selfRoutine to routineList
      selfRoutine.forEach((selfRoutineItem) => {
        const isAlreadyIncluded = routineList.some(
          (routineItem) => routineItem._id === selfRoutineItem._id
        );
        if (!isAlreadyIncluded) {
          routineList.push(selfRoutineItem);
        }
      });
    } else {
      // Exclude self routine: Remove matching elements from routineList
      return routineList.filter(
        (routineItem) =>
          !selfRoutine.some(
            (selfRoutineItem) => selfRoutineItem._id === routineItem._id
          )
      );
    }

    return routineList;
  };

  if (loading) {
    return <p>Loading routine...</p>;
  }

  if (error) {
    return <p className="text-red-500">{error}</p>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-4">
        {showFriends && (
          <select onChange={handleFriendChange} value={selectedFriend}>
            {[...friendMap.keys()].map((friend) => (
              <option key={friend} value={friend}>
                {friend}
              </option>
            ))}
          </select>
        )}
        {showFriends && (
          <label>
            <input
              type="checkbox"
              checked={includeSelf}
              onChange={handleSwitchChange}
            />
            Include My Routine
          </label>
        )}
      </div>

      <RoutineTable routines={filterRoutineList()} />
    </div>
  );
};

export default RoutineParent;
