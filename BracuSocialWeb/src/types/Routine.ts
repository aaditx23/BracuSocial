import { Course } from "@/types/Course";

export interface Routine {
    _id: string;
    course: string;
    section: string;
    faculty: string;
    classDay: string;
    classTime: string;
    labDay: string;
    labTime: string;
    room: String;
    name?: String;
    profilePicture?: String
  }


export const RoutineFromCourse = (course: Course, room: string): Routine => {
  return {
    _id: course._id,
    course: course.course,
    section: course.section,
    faculty: course.faculty,
    classDay: course.classDay,
    classTime: course.classTime,
    labDay: course.labDay,
    labTime: course.labTime,
    room: room,
  };
};

export const ProfileWithRoutine = (name: String, pic: String, routine: Routine): Routine => {
  routine.name = name
  routine.profilePicture = pic

  return routine
}