import { useEffect, useState } from "react";
import axios from "axios";
import { AddedFriends } from "@/components/friends/AddedFriends";
import { FriendRequests } from "@/components/friends/FriendRequest";
import { SearchPeople } from "@/components/friends/SearchPeople";
import { Profile } from "@/types/Profile";

export function FriendsPage() {
  const [profile, setProfile] = useState<Profile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProfile = async () => {
      const studentId = localStorage.getItem("id");
      if (studentId) {
        try {
          const { data } = await axios.get(
            `/api/profile/${studentId}`
          );
          setProfile(data);
        } catch (err) {
          setError("Error fetching profile.");
          console.error(err);
        } finally {
          setLoading(false);
        }
      } else {
        setError("User not logged in.");
        setLoading(false);
      }
    };

    fetchProfile();
    const intervalId = setInterval(fetchProfile, 1000);
    return () => clearInterval(intervalId);
  }, []);

  if (loading) return <p>Loading...</p>;
  if (error) return <p className="text-red-600">{error}</p>;

  return (
    <div className="flex flex-row space-x-4">
      <AddedFriends profile={profile!} setProfile={setProfile} />
      <FriendRequests profile={profile!} setProfile={setProfile} />
      <SearchPeople profile={profile!} setProfile={setProfile} />
    </div>
  );
}
