import React from "react";
import RoutineParent from "@/components/routine/routine";

const RoutinePage: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">My Routine</h1>
      <div className="p-4 bg-white rounded-lg shadow-md">
        <RoutineParent showFriends={false} />
      </div>
    </div>
  );
};

export default RoutinePage;
