import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import { CheckCheckIcon, TrashIcon } from "lucide-react";

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
        .filter((id) => id !== "");

      if (ids.length === 0) {
        setLoading(false);
        return;
      }

      try {
        const requestsData = await Promise.all(
          ids.map((id) =>
            axios
              .get(`https://bracusocial-web-backend.vercel.app/api/profile/${id}`)
              .then((res) => res.data)
          )
        );
        const updatedProfile = await axios.get(
          `https://bracusocial-web-backend.vercel.app/api/profile/${profile.studentId}`
        );
        setProfile(updatedProfile.data);
        setRequests(requestsData);
      } catch (err) {
        setError("Error fetching friend requests.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
    const intervalId = setInterval(fetchRequests, 1000);

    return () => clearInterval(intervalId);
  }, [profile]);

  if (loading) return <p>Loading friend requests...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  if (requests.length === 0) {
    return (
      <Card className="p-6 max-h-[85vh] w-[30vw]">
        <CardHeader>
          <CardTitle>Pending Friend Requests</CardTitle>
        </CardHeader>
        <CardContent className="mb-4">
          <p>No pending friend requests.</p>
        </CardContent>
      </Card>
    );
  }

  const handleAddFriend = async (friendId: string) => {
    try {
      const studentId = profile.studentId;

      const response = await axios.post(
        "https://bracusocial-web-backend.vercel.app/api/profile/acceptRequest",
        {
          studentId,
          friendId,
        }
      );

      console.log("Friend request accepted:", response.data);
    } catch (error) {
      console.error("Error accepting friend request:", error);
    }
    window.location.reload();
  };

  const handleCancelRequest = async (friendId: string) => {
    try {
      const studentId = profile.studentId;

      const response = await axios.post(
        "https://bracusocial-web-backend.vercel.app/api/profile/cancelRequest",
        {
          studentId,
          friendId,
        }
      );

      console.log(response.data.message);
    } catch (error) {
      console.error("Error canceling friend request:", error);
    }
    window.location.reload();
  };

  return (
    <Card className="p-6 max-h-[85vh] w-[30vw]">
      <CardHeader>
        <CardTitle>Pending Friend Requests</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        <div className="max-h-[65vh] overflow-y-auto">
          {requests.map((request) => (
            <Card
              key={request.studentId}
              className="mb-4 p-4 flex items-center justify-between border border-gray-300 shadow-md"
            >
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
