import React from "react";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/table";
import { Routine } from "@/types/Routine";
import ImagePreview from "../image/imagePreview";

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
  routines: Routine[];
}

const RoutineTable: React.FC<RoutineTableProps> = ({ routines }) => {
  // Prepare tableData to store routines in time and day slots
  const tableData: Routine[][][] = Array(TimeSlots.length)
    .fill(null)
    .map(() =>
      Array(WeekDays.length)
        .fill(null)
        .map(() => [])
    );

  const getSlot = (dayInfo: string, timeInfo: string) => {
    const days = dayInfo.split(" ");
    const timeSlots = timeInfo.split(";").map((time) => time.trim());

    let rows: number[] = [];
    const columns: number[] = [];

    for (const day of days) {
      const column = WeekDays.indexOf(day);
      if (column !== -1) columns.push(column);
    }

    for (const timeInfo of timeSlots) {
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
    }

    return { rows, columns };
  };

  // Populate the tableData with routines
  routines.forEach((routine) => {
    const { rows, columns } = getSlot(routine.classDay, routine.classTime);
    columns.forEach((column) => {
      rows.forEach((row) => {
        tableData[row][column].push(routine);
      });
    });

    if (routine.labDay !== "-") {
      const { rows: labRows, columns: labColumns } = getSlot(
        routine.labDay,
        routine.labTime
      );
      labColumns.forEach((column) => {
        labRows.forEach((row) => {
          tableData[row][column].push(routine);
        });
      });
    }
  });

  return (
    <div className="overflow-auto max-h-[100vh]">
      <Table className="table-fixed w-full border-collapse border border-gray-300">
        <TableHeader>
          <TableRow>
            <TableHead className="w-[140px]">Time/Day</TableHead>
            {WeekDays.map((day) => (
              <TableHead
                key={day}
                className="w-[100px] text-center border border-gray-300"
              >
                {day}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>
          {TimeSlots.map((slot, rowIndex) => (
            <TableRow key={slot}>
              <TableCell className="border border-gray-300">{slot}</TableCell>
              {WeekDays.map((_, colIndex) => {
                const courseList = tableData[rowIndex][colIndex];

                return (
                  <TableCell
                    key={colIndex}
                    className="w-[100px] h-[80px] text-center border border-gray-300 align-top"
                  >
                    {courseList.length > 0 &&
                      courseList.map((routine, index) => (
                        <div
                          key={index}
                          className="bg-[#e0f7fa] rounded-md shadow-sm p-2 m-1 flex flex-col justify-between"
                        >
                          <div className="flex items-center space-x-2">
                            <ImagePreview
                              base64String={routine.profilePicture!!.toString()}
                            />
                            <span className="text-xs">
                              <strong>{routine.name}</strong>
                            </span>
                          </div>
                          <div className="text-xs mt-1">
                            <strong>
                              {routine.course} - {routine.section}
                            </strong>
                            <br />
                            {routine.room}
                          </div>
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
