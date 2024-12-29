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
      .filter((id) => id !== ""); // Filter out empty values

    if (ids.length === 0) {
      setLoading(false);
      return; // No friends to fetch
    }

    try {
      const friendsData = await Promise.all(
        ids.map((id) =>
          axios
            .get(`http://localhost:3000/api/profile/${id}`)
            .then((res) => res.data)
        )
      );
      setFriends(friendsData);
      const updatedProfile = await axios.get(`http://localhost:3000/api/profile/${profile.studentId}`);
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
      const studentId = profile.studentId;  // Assuming profile is available and has studentId

      // Call the unfriend API
      const response = await axios.post('http://localhost:3000/api/profile/unfriend', {
        studentId,
        friendId
      });

      console.log("Unfriend API Response:", response.data);

      // Update the profile and friends list locally after unfriend
      setFriends((prevFriends) => prevFriends.filter((friend) => friend.studentId !== friendId));


    } catch (error) {
      console.error("Error unfriending:", error);
      // Handle the error (e.g., show an alert to the user)
    }
    window.location.reload()
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
        {/* Scrollable area for friends */}
        <div className="max-h-[65vh] overflow-y-auto">
          {friends.map((friend) => (
            <Card key={friend.studentId} className="mb-4 p-4 flex items-center justify-between border border-gray-300 shadow-md">
              <ProfileCard profile={friend} />
              <Button
                onClick={() => handleUnfriend(friend.studentId)}
                className="ml-2 p-2"
                variant="outline" // Optional for styling
              >
                <UserMinusIcon className="h-5 w-5 text-red-500" /> {/* Icon button */}
              </Button>
            </Card>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
