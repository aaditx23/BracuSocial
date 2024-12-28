import React from "react";
import { Table, TableHeader, TableRow, TableHead, TableBody, TableCell } from "@/components/ui/table";
import { Course } from "@/types/Course";
import { Routine, RoutineFromCourse } from "@/types/Routine";

const TimeSlots = [
  "08:00 AM - 09:20 AM",
  "09:30 AM - 10:50 AM",
  "11:00 AM - 12:20 PM",
  "12:30 PM - 01:50 PM",
  "02:00 PM - 03:20 PM",
  "03:30 PM - 04:50 PM",
  "05:00 PM - 06:20 PM",
  "06:30 PM - 08:00 PM",
];

const WeekDays = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];

interface RoutineTableProps {
  addedCourses: Course[];
  getClash: (hasClash: boolean) => void;
}

const RoutineTable: React.FC<RoutineTableProps> = ({ addedCourses, getClash }) => {
  const tableData: Routine[][][] = Array(TimeSlots.length)
        .fill(null)
        .map(() =>
          Array(WeekDays.length)
            .fill(null)
            .map(() => [])
        );

  
    const getSlot = (dayInfo: string, timeInfo: string) => {
      const days = dayInfo.split(" ");
      const columns = days.map((d) => WeekDays.indexOf(d));
      let rows: number[] = [];
  
      switch (timeInfo) {
        case "08:00 AM - 09:20 AM":
          rows = [0];
          break;
        case "09:30 AM - 10:50 AM":
          rows = [1];
          break;
        case "11:00 AM - 12:20 PM":
          rows = [2];
          break;
        case "12:30 PM - 01:50 PM":
          rows = [3];
          break;
        case "02:00 PM - 03:20 PM":
          rows = [4];
          break;
        case "03:30 PM - 04:50 PM":
          rows = [5];
          break;
        case "05:00 PM - 06:20 PM":
          rows = [6];
          break;
        case "06:30 PM - 08:00 PM":
          rows = [7];
          break;
        case "08:00 AM - 10:50 AM":
          rows = [0, 1];
          break;
        case "11:00 AM - 01:50 PM":
          rows = [2, 3];
          break;
        case "02:00 PM - 04:50 PM":
          rows = [4, 5];
          break;
        case "05:00 PM - 07:50 PM":
          rows = [6, 7];
          break;
      }
  
      return { rows, columns };
    };
  
    const findClash = () => {
      let hasClash = false;
      for (let i = 0; i < tableData.length; i++) {
        for (let j = 0; j < tableData[i].length; j++) {
          if (tableData[i][j].length > 1) {
            hasClash = true;
            break;
          }
        }
      }
      getClash(hasClash);
    };
  
    addedCourses.forEach((course) => {
      const { rows, columns } = getSlot(course.classDay, course.classTime);
      columns.forEach((column) => {
        rows.forEach((row) => {
          tableData[row][column].push(RoutineFromCourse(course, course.classRoom));
        });
      });
  
      if (course.labDay !== "-") {
        const { rows: labRows, columns: labColumns } = getSlot(course.labDay, course.labTime);
        labColumns.forEach((column) => {
          labRows.forEach((row) => {
            tableData[row][column].push(RoutineFromCourse(course, course.labRoom));
          });
        });
      }
    });
  
    findClash();
  
    return (
      <div className="overflow-auto max-h-[50vh]">
        <Table className="table-fixed w-full border-collapse border border-gray-300">
          <TableHeader>
            <TableRow>
              <TableHead className="w-[140px]">Time/Day</TableHead>
              {WeekDays.map((day) => (
                <TableHead key={day} className="w-[100px] text-center border border-gray-300">
                  {day}
                </TableHead>
              ))}
            </TableRow>
          </TableHeader>
          <TableBody>
            {TimeSlots.map((slot, rowIndex) => (
              <TableRow key={slot}>
                <TableCell className="w-[140px] border border-gray-300">{slot}</TableCell>
                {WeekDays.map((_, colIndex) => {
                  const courseList = tableData[rowIndex][colIndex];
                  const isClash = courseList.length > 1;
    
                  return (
                    <TableCell
                      key={colIndex}
                      className={`w-[100px] h-[80px] text-center border border-gray-300 align-top ${
                        isClash ? "bg-red-500 text-white" : ""
                      }`}
                    >
                      {courseList.length > 0 &&
                        courseList.map((course, index) => (
                          <div key={index} className="p-1 text-sm leading-tight">
                            <strong>{course.course} - {course.section}</strong><br />
                            {course.room}
                          </div>
                        ))}
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    );
    
  };

export default RoutineTable;
