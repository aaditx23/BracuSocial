import React, { useEffect, useState } from "react";
import axios from "axios";

const FindLab: React.FC = () => {
  const labTimeSlots = [
    "08:00 AM - 10:50 AM",
    "11:00 AM - 01:50 PM",
    "02:00 PM - 04:50 PM",
    "05:00 PM - 08:00 PM",
  ];

  const daysOfWeek = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];

  const [labs, setLabs] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [currentDay, setCurrentDay] = useState<string>(getCurrentDay());
  const [currentTimeSlot, setCurrentTimeSlot] = useState<string>(
    getCurrentTimeSlot()
  );
  const [isClosed, setIsClosed] = useState<boolean>(false);

  // Get the current day
  function getCurrentDay() {
    const currentDayIndex = new Date().getDay(); // 0 = Sun, 1 = Mon, etc.
    return daysOfWeek[currentDayIndex];
  }

  // Get the current time slot
  function getCurrentTimeSlot() {
    const currentTime = new Date();
    let hours = currentTime.getHours();
    let minutes = currentTime.getMinutes();
    const ampm = hours >= 12 ? "PM" : "AM";

    hours = hours % 12 || 12;
    const paddedHours = hours < 10 ? "0" + hours : hours;
    const paddedMinutes = minutes < 10 ? "0" + minutes : minutes;
    const currentTimeString = `${paddedHours}:${paddedMinutes} ${ampm}`;

    for (let slot of labTimeSlots) {
      const [start, end] = slot.split("-");
      if (
        currentTimeString >= start.trim() &&
        currentTimeString <= end.trim()
      ) {
        return slot;
      }
    }
    return "Closed";
  }

  // Fetch available labs
  const fetchEmptyLabs = async (day: string, time: string) => {
    setLoading(true);
    try {
      const response = await axios.post(
        "https://bracusocial-web-backend-b6x213chy-aaditx23s-projects.vercel.app/api/pdf/getLabs",
        { day, time }
      );
      setLabs(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching empty labs:", error);
      setError("Failed to load labs.");
      setLoading(false);
    }
  };

  // Initial fetch
  useEffect(() => {
    if (currentTimeSlot === "Closed") {
      setIsClosed(true);
      setLoading(false);
    } else {
      setIsClosed(false);
      fetchEmptyLabs(currentDay, currentTimeSlot);
    }
  }, [currentDay, currentTimeSlot]);

  // Handle day and time selection
  const handleDayClick = (day: string) => {
    setCurrentDay(day);
    setIsClosed(false);
    fetchEmptyLabs(day, currentTimeSlot);
  };

  const handleTimeSlotClick = (time: string) => {
    setCurrentTimeSlot(time);
    setIsClosed(false);
    fetchEmptyLabs(currentDay, time);
  };

  const filteredLabs = labs.filter((lab) => lab !== "");

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold text-center mb-6">
        Find Available Labs
      </h1>

      {/* Day Buttons */}
      <div className="mb-6">
        <div className="flex justify-center space-x-4">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => handleDayClick(day)}
              className={`px-4 py-2 rounded-lg text-lg ${
                currentDay === day ? "bg-blue-500 text-white" : "bg-gray-200"
              }`}
            >
              {day}
            </button>
          ))}
        </div>
      </div>

      {/* Time Slot Buttons */}
      <div className="mb-6">
        <div className="flex justify-center space-x-2 flex-wrap">
          {labTimeSlots.map((slot) => (
            <button
              key={slot}
              onClick={() => handleTimeSlotClick(slot)}
              className={`px-3 py-1 rounded-lg text-sm ${
                currentTimeSlot === slot
                  ? "bg-blue-500 text-white"
                  : "bg-gray-200"
              }`}
            >
              {slot}
            </button>
          ))}
        </div>
      </div>

      {/* Lab Results */}
      {loading ? (
        <p className="text-center">Loading labs...</p>
      ) : error ? (
        <p className="text-center text-red-500">{error}</p>
      ) : (
        <div>
          {isClosed ? (
            <p className="text-center text-blue-500 font-semibold">
              The university is closed at this moment. Please select a time slot
              manually.
            </p>
          ) : (
            <>
              <h3 className="text-xl font-semibold mb-2">Available Labs</h3>
              {filteredLabs.length === 0 ? (
                <p>No labs available.</p>
              ) : (
                <div>
                  <p className="mt-4 text-center text-lg font-semibold">
                    Total Labs: {filteredLabs.length}
                  </p>
                  <ul className="space-y-2">
                    {filteredLabs.map((lab, index) => (
                      <li
                        key={index}
                        className="border p-2 rounded-lg text-center"
                      >
                        {lab}
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default FindLab;
