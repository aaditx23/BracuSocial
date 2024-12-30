import React from "react";
import RoutineParent from "@/components/routine/routine";

const FriendsRoutinePage: React.FC = () => {
  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="text-2xl font-bold text-gray-800 mb-6">
        Friends' Routine
      </h1>
      <div className=" p-4 bg-white rounded-lg shadow-md">
        <RoutineParent showFriends={true} />
      </div>
    </div>
  );
};

export default FriendsRoutinePage;
