import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import { CheckCheckIcon, TrashIcon } from "lucide-react"; // Import the icons

interface FriendRequestsProps {
  profile: Profile;
}

export function FriendRequests({ profile }: FriendRequestsProps) {
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
        setRequests(requestsData);
      } catch (err) {
        setError("Error fetching friend requests.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, [profile]);

  if (loading) return <p>Loading friend requests...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  if (requests.length === 0) {
    return <p>No pending friend requests</p>; // If no requests are found, show this message
  }

  const handleAddFriend = async (friendId: string) => {
    // Placeholder for adding a friend API call
    console.log("Add friend:", friendId);
  };

  const handleCancelRequest = async (friendId: string) => {
    // Placeholder for canceling a friend request API call
    console.log("Cancel request:", friendId);
  };

  return (
    <Card className="p-6 max-h-[500px]">
      <CardHeader>
        <CardTitle>Pending Friend Requests</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        {/* Scrollable area for requests */}
        <div className="max-h-[300px] overflow-y-auto">
          {requests.map((request) => (
            <Card key={request.studentId} className="mb-4 p-4 flex items-center border border-gray-300 shadow-md">
              <ProfileCard profile={request} />
              <Button
                onClick={() => handleAddFriend(request.studentId)}
                className="ml-2 p-2"
                variant="outline"
              >
                <CheckCheckIcon className="h-5 w-5 text-green-500" /> {/* Add friend icon */}
              </Button>
              <Button
                onClick={() => handleCancelRequest(request.studentId)}
                className="ml-2 p-2"
                variant="outline"
              >
                <TrashIcon className="h-5 w-5 text-red-500" /> {/* Cancel request icon */}
              </Button>
            </Card>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
