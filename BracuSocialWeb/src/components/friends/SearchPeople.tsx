import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import {
  UserPlusIcon,
  ClockIcon,
  CheckCircleIcon,
  HourglassIcon,
} from "lucide-react";

interface SearchPeopleProps {
  profile: Profile;
  setProfile: (profile: Profile) => void;
}

export function SearchPeople({ profile }: SearchPeopleProps) {
  const [searchText, setSearchText] = useState("");
  const [profiles, setProfiles] = useState<Profile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAllProfiles = async () => {
      try {
        const { data } = await axios.get(
          "https://bracusocial-web-backend-b6x213chy-aaditx23s-projects.vercel.app/api/profiles/getAll"
        );
        setProfiles(
          data.filter((p: Profile) => p.studentId !== profile.studentId)
        );
      } catch (err) {
        setError("Error fetching profiles.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchAllProfiles();
  }, [profile]);

  if (loading) return <p>Loading profiles...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  const filteredProfiles = profiles.filter((p) =>
    ["name", "email", "studentId", "enrolledCourses"].some((field) => {
      if (field in p) {
        return p[field as keyof Profile]
          ?.toLowerCase()
          ?.includes(searchText.toLowerCase());
      }
      return false;
    })
  );

  const isRequestPending = (friendRequest: string) => {
    return profile.friendRequests.includes(friendRequest);
  };

  const isAwaitingApproval = (friendRequests: string[]) => {
    return friendRequests.includes(profile.studentId);
  };

  const isFriend = (addedFriends: string[]) => {
    return addedFriends.includes(profile.studentId);
  };

  const handleSendRequest = async (friendId: string) => {
    try {
      const studentId = profile.studentId;

      const response = await axios.post(
        "https://bracusocial-web-backend-b6x213chy-aaditx23s-projects.vercel.app/api/profile/sendFriendRequest",
        {
          studentId,
          friendId,
        }
      );

      console.log("Friend request sent:", response.data);
    } catch (error) {
      console.error("Error sending friend request:", error);
    }
  };

  return (
    <Card className="max-h-[85vh] w-[30vw] p-6 ">
      <CardHeader>
        <CardTitle>Search People</CardTitle>
      </CardHeader>
      <CardContent className="mb-4">
        <Input
          placeholder="Search by name, email, ID, or course"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
      </CardContent>

      <div className="max-h-[65vh] overflow-y-auto">
        {filteredProfiles.map((p) => (
          <Card
            key={p.studentId}
            className="mb-4 p-4 flex items-center justify-between border border-gray-300 shadow-md"
          >
            <ProfileCard profile={p} />
            <Button
              className="ml-2 p-2"
              variant="outline"
              disabled={isFriend(
                p.addedFriends.split(",").map((id) => id.trim())
              )}
              onClick={() => {
                if (
                  !isRequestPending(p.studentId) &&
                  !isAwaitingApproval(
                    p.friendRequests.split(",").map((id) => id.trim())
                  ) &&
                  !isFriend(p.addedFriends.split(",").map((id) => id.trim()))
                ) {
                  handleSendRequest(p.studentId);
                }
              }}
            >
              {isRequestPending(p.studentId) ? (
                <HourglassIcon className="h-5 w-5 text-yellow-500" />
              ) : isAwaitingApproval(
                  p.friendRequests.split(",").map((id) => id.trim())
                ) ? (
                <ClockIcon className="h-5 w-5 text-blue-500" />
              ) : isFriend(p.addedFriends.split(",").map((id) => id.trim())) ? (
                <CheckCircleIcon className="h-5 w-5 text-green-500" />
              ) : (
                <UserPlusIcon className="h-5 w-5 text-green-500" />
              )}
            </Button>
          </Card>
        ))}
      </div>
    </Card>
  );
}
