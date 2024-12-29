import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import { CheckCheckIcon, TrashIcon } from "lucide-react"; // Import the icons

interface FriendRequestsProps {
  profile: Profile;
  setProfile: (profile: Profile) => void;
}

export function FriendRequests({ profile, setProfile }: FriendRequestsProps) {
  const [requests, setRequests] = useState<Profile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchRequests = async () => {
      const ids = profile.friendRequests
        .split(",")
        .map((id) => id.trim())
        .filter((id) => id !== ""); // Filter out empty values

      if (ids.length === 0) {
        setLoading(false);
        return; // No requests to fetch
      }

      try {
        
        const requestsData = await Promise.all(
          ids.map((id) =>
            axios
              .get(`http://localhost:3000/api/profile/${id}`)
              .then((res) => res.data)
          )
        );
        const updatedProfile = await axios.get(`http://localhost:3000/api/profile/${profile.studentId}`);
        setProfile(updatedProfile.data);
        setRequests(requestsData);
      } catch (err) {
        setError("Error fetching friend requests.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests(); // Initial fetch
    const intervalId = setInterval(fetchRequests, 1000); // Fetch every second

    // Cleanup the interval when the component unmounts
    return () => clearInterval(intervalId);
  }, [profile]);

  if (loading) return <p>Loading friend requests...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  // Show message if there are no requests
  if (requests.length === 0) {
    return (
      <Card className="p-6 max-h-[85vh] w-[30vw]">
        <CardHeader>
          <CardTitle>Pending Friend Requests</CardTitle>
        </CardHeader>
        <CardContent className="mb-4">
          <p>No pending friend requests.</p> {/* Message for no requests */}
        </CardContent>
      </Card>
    );
  }

  const handleAddFriend = async (friendId: string) => {
    try {
      // Assuming you have access to `profile.studentId` (current user's ID)
      const studentId = profile.studentId;  // Make sure `profile.studentId` is available
  
  
      // Send POST request to the API to accept the friend request
      const response = await axios.post("http://localhost:3000/api/profile/acceptRequest", {
        studentId,
        friendId,
      });
  
      // Handle the response, e.g., update UI or state
      console.log("Friend request accepted:", response.data);
    } catch (error) {
      console.error("Error accepting friend request:", error);
      // Optionally, handle error, e.g., show an error message to the user
    }
    window.location.reload()
  };

  const handleCancelRequest = async (friendId: string) => {
    try {
      // Get the studentId from the profile (already available in the props or state)
      const studentId = profile.studentId;
  
      // Make the API call to cancel the friend request
      const response = await axios.post("http://localhost:3000/api/profile/cancelRequest", {
        studentId,
        friendId,
      });
  
      // Handle success response
      console.log(response.data.message); // "Friend request canceled"
      // Optionally, update the state to reflect the changes, like removing the friend request from UI
      // For example, you can update the `profile.friendRequests` state here.
  
    } catch (error) {
      console.error("Error canceling friend request:", error);
      // Optionally, display an error message
    }
    window.location.reload()
  };

  return (
    <Card className="p-6 max-h-[85vh] w-[30vw]">
      <CardHeader>
        <CardTitle>Pending Friend Requests</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        {/* Scrollable area for requests */}
        <div className="max-h-[65vh] overflow-y-auto">
          {requests.map((request) => (
            <Card key={request.studentId} className="mb-4 p-4 flex items-center justify-between border border-gray-300 shadow-md">
              <ProfileCard profile={request} />
              <Button
                onClick={() => handleAddFriend(request.studentId)}
                className="ml-2 p-2"
                variant="outline"
              >
                <CheckCheckIcon className="h-5 w-5 text-green-500" />
              </Button>
              <Button
                onClick={() => handleCancelRequest(request.studentId)}
                className="ml-2 p-2"
                variant="outline"
              >
                <TrashIcon className="h-5 w-5 text-red-500" /> 
              </Button>
            </Card>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
