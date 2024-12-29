import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Profile } from "@/types/Profile";
import { UserMinusIcon } from "lucide-react";

interface AddedFriendsProps {
  profile: Profile;
}

export function AddedFriends({ profile }: AddedFriendsProps) {
  const [friends, setFriends] = useState<Profile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
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
      } catch (err) {
        setError("Error fetching added friends.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchFriends();
  }, [profile]);

  const handleUnfriend = (friendId: string) => {
    // Placeholder for unfriend API call
    console.log("Unfriend:", friendId);
  };

  if (loading) return <p>Loading added friends...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  if (friends.length === 0) {
    return (
      <Card className="p-6 max-h-[500px]">
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
    <Card className="p-6 max-h-[500px]">
      <CardHeader>
        <CardTitle>Added Friends</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        {/* Scrollable area for friends */}
        <div className="max-h-[300px] overflow-y-auto">
          {friends.map((friend) => (
            <Card key={friend.studentId} className="mb-4 p-4 flex items-center border border-gray-300 shadow-md">
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
