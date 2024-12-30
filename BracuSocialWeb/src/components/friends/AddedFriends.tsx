import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Profile } from "@/types/Profile";
import { UserMinusIcon } from "lucide-react";

interface AddedFriendsProps {
  profile: Profile;
  setProfile: (profile: Profile) => void;
}

export function AddedFriends({ profile, setProfile }: AddedFriendsProps) {
  const [friends, setFriends] = useState<Profile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchFriends = async () => {
    const ids = profile.addedFriends
      .split(",")
      .map((id) => id.trim())
      .filter((id) => id !== "");

    if (ids.length === 0) {
      setLoading(false);
      return;
    }

    try {
      const friendsData = await Promise.all(
        ids.map((id) =>
          axios
            .get(`/api/profile/${id}`)
            .then((res) => res.data)
        )
      );
      setFriends(friendsData);
      const updatedProfile = await axios.get(
        `/api/profile/${profile.studentId}`
      );
      setProfile(updatedProfile.data);
    } catch (err) {
      setError("Error fetching added friends.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    console.log("profile changed", profile.addedFriends);
    fetchFriends();
  }, [profile]);

  const handleUnfriend = async (friendId: string) => {
    try {
      const studentId = profile.studentId;

      const response = await axios.post(
        "/api/profile/unfriend",
        {
          studentId,
          friendId,
        }
      );

      console.log("Unfriend API Response:", response.data);

      setFriends((prevFriends) =>
        prevFriends.filter((friend) => friend.studentId !== friendId)
      );
    } catch (error) {
      console.error("Error unfriending:", error);
    }
    window.location.reload();
  };

  if (loading) return <p>Loading added friends...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  if (friends.length === 0) {
    return (
      <Card className="p-6 max-h-[85vh] w-[30vw]">
        <CardHeader>
          <CardTitle>Added Friends</CardTitle>
        </CardHeader>
        <CardContent className="mb-4">
          <p>No friends added</p>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="p-6 max-h-[85vh] w-[30vw]">
      <CardHeader>
        <CardTitle>Added Friends</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        <div className="max-h-[65vh] overflow-y-auto">
          {friends.map((friend) => (
            <Card
              key={friend.studentId}
              className="mb-4 p-4 flex items-center justify-between border border-gray-300 shadow-md"
            >
              <ProfileCard profile={friend} />
              <Button
                onClick={() => handleUnfriend(friend.studentId)}
                className="ml-2 p-2"
                variant="outline"
              >
                <UserMinusIcon className="h-5 w-5 text-red-500" />
              </Button>
            </Card>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
