import React, { useEffect, useState } from 'react';
import axios from 'axios';

const FindRoom: React.FC = () => {
  const timeSlots = [
    "08:00 AM - 09:20 AM",
    "09:30 AM - 10:50 AM",
    "11:00 AM - 12:20 PM",
    "12:30 PM - 01:50 PM",
    "02:00 PM - 03:20 PM",
    "03:30 PM - 04:50 PM",
    "05:00 PM - 06:20 PM",
    "06:30 PM - 08:00 PM"
  ];
  
  const daysOfWeek = ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"];

  const [classrooms, setClassrooms] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [currentDay, setCurrentDay] = useState<string>(getCurrentDay());
  const [currentTimeSlot, setCurrentTimeSlot] = useState<string>(getCurrentTimeSlot());

  // Function to get the current day
  function getCurrentDay() {
    const currentDayIndex = new Date().getDay(); // 0 = Sun, 1 = Mon, etc.
    return daysOfWeek[currentDayIndex]; // Return the current day
  }

  // Function to get the current time slot
  function getCurrentTimeSlot() {
    const currentTime = new Date();
    
    // Get current hours and minutes
    let hours = currentTime.getHours();
    let minutes = currentTime.getMinutes();
    
    // Determine AM or PM
    const ampm = hours >= 12 ? 'PM' : 'AM';
    
    // Convert to 12-hour format
    hours = hours % 12;
    hours = hours ? hours : 12; // 0 hour is 12 in 12-hour format
  
    // Pad hours and minutes to always have two digits
    const paddedHours = hours < 10 ? '0' + hours : hours;
    const paddedMinutes = minutes < 10 ? '0' + minutes : minutes;
    
    const currentTimeString = `${paddedHours}:${paddedMinutes} ${ampm}`;
    

    
    for (let slot of timeSlots) {
      const [start, end] = slot.split('-');

      if (currentTimeString >= start.trim() && currentTimeString <= end.trim()) {
        return slot;
      }
    }
    return "Closed";
  }
  

  // Fetch classrooms from API
  const fetchEmptyRooms = async (day: string, time: string) => {
    setLoading(true);
    try {
      const response = await axios.post('http://localhost:3000/api/pdf/getClasses', { day, time });
      setClassrooms(response.data); // Save fetched classrooms in state
      setLoading(false);
    } catch (error) {
      console.error('Error fetching empty classrooms:', error);
      setError('Failed to load classrooms.');
      setLoading(false);
    }
  };

  // Fetch empty classrooms for the initial load
  useEffect(() => {
    fetchEmptyRooms(currentDay, currentTimeSlot);
  }, [currentDay, currentTimeSlot]);

  // Handle day and time button clicks
  const handleDayClick = (day: string) => {
    setCurrentDay(day);
    fetchEmptyRooms(day, currentTimeSlot);
  };

  const handleTimeSlotClick = (time: string) => {
    setCurrentTimeSlot(time);
    fetchEmptyRooms(currentDay, time);
  };

  // Filter out empty classrooms
  const filteredClassrooms = classrooms.filter(classroom => classroom !== "");

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-3xl font-bold text-center mb-6">Find Available Classrooms</h1>
  
      {/* Day Buttons */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold mb-2">Select Day</h2>
        <div className="flex justify-center space-x-4">
          {daysOfWeek.map((day) => (
            <button
              key={day}
              onClick={() => handleDayClick(day)}
              className={`px-4 py-2 rounded-lg text-lg ${currentDay === day ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
            >
              {day}
            </button>
          ))}
        </div>
      </div>
  
      {/* Time Slot Buttons */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold mb-2">Select Time Slot</h2>
        <div className="flex justify-center space-x-2 flex-wrap">
          {timeSlots.map((slot) => (
            <button
              key={slot}
              onClick={() => handleTimeSlotClick(slot)}
              className={`px-3 py-1 rounded-lg text-sm ${currentTimeSlot === slot ? 'bg-blue-500 text-white' : 'bg-gray-200'}`}
            >
              {slot}
            </button>
          ))}
        </div>
      </div>
  
      {/* Classroom Results */}
      {loading ? (
        <p className="text-center">Loading classrooms...</p>
      ) : error ? (
        <p className="text-center text-red-500">{error}</p>
      ) : (
        <div>
          <h3 className="text-xl font-semibold mb-2">Available Classrooms</h3>
          {filteredClassrooms.length === 0 ? (
            <p>No classrooms available.</p>
          ) : (
            <div>
                <p className="mt-4 text-center text-lg font-semibold">
                Total Classrooms: {filteredClassrooms.length}
              </p>
              <ul className="space-y-2">
                {filteredClassrooms.map((classroom, index) => (
                  <li key={index} className="border p-2 rounded-lg text-center">
                    {classroom}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </div>
      )}
    </div>
  );
  
  
};

export default FindRoom;
