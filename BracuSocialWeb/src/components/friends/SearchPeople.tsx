import { useEffect, useState } from "react";
import axios from "axios";
import { ProfileCard } from "./ProfileCard";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Profile } from "@/types/Profile";
import { UserPlusIcon, ClockIcon, CheckCircleIcon, HourglassIcon } from "lucide-react"; // Import necessary icons

interface SearchPeopleProps {
  profile: Profile;
}

export function SearchPeople({ profile }: SearchPeopleProps) {
  const [searchText, setSearchText] = useState("");
  const [profiles, setProfiles] = useState<Profile[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAllProfiles = async () => {
      try {
        const { data } = await axios.get("http://localhost:3000/api/profiles/getAll");
        setProfiles(data.filter((p: Profile) => p.studentId !== profile.studentId));
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
        return p[field as keyof Profile]?.toLowerCase()?.includes(searchText.toLowerCase());
      }
      return false;
    })
  );

  const isRequestPending = (friendRequest: string) => {
    console.log(friendRequest)
    return profile.friendRequests.includes(friendRequest);
  };

  const isAwaitingApproval = (friendRequests: string[]) => {
    return friendRequests.includes(profile.studentId);
  };

  const isFriend = (addedFriends: string[]) => {
    return addedFriends.includes(profile.studentId);
  };

  return (
    <Card className="p-6 max-h-[500px]">
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

      {/* Scrollable area for profile list */}
      <div className="max-h-[300px] overflow-y-auto">
        {filteredProfiles.map((p) => (
          <Card key={p.studentId} className="mb-4 p-4 flex items-center">
            <ProfileCard profile={p} />
            <Button className="ml-2 p-2" variant="outline" disabled={isFriend(p.addedFriends.split(",").map(id => id.trim()))}>
              {isRequestPending(p.studentId) ? (
                <HourglassIcon className="h-5 w-5 text-yellow-500" /> // Show waiting person icon
              ) : isAwaitingApproval(p.friendRequests.split(",").map(id => id.trim())) ? (
                <ClockIcon className="h-5 w-5 text-blue-500" /> // Show awaiting approval icon
              ) : isFriend(p.addedFriends.split(",").map(id => id.trim())) ? (
                <CheckCircleIcon className="h-5 w-5 text-green-500" /> // Show approved icon
              ) : (
                <UserPlusIcon className="h-5 w-5 text-green-500" /> // Show add friend icon
              )}
            </Button>
          </Card>
        ))}
      </div>
    </Card>
  );
}
