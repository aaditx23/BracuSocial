package com.aaditx23.bracusocial.backend.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FirebaseRepository @Inject constructor() {
    private val db = Firebase.firestore

    suspend fun addOrUpdateProfile(profile: RemoteProfile) {
        println("Entered firebase")
        val profileData = hashMapOf(
            "studentId" to profile.studentId,
            "name" to profile.name,
            "email" to profile.email,
            "addedFriends" to profile.addedFriends,
            "enrolledCourses" to profile.enrolledCourses,
            "friendRequests" to profile.friendRequests,
            "profilePicture" to profile.profilePicture
        )

        db.collection("profiles").document(profile.email)
            .set(profileData)
            .addOnSuccessListener {
                Log.d("Firebase addOrUpdateProfile", "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase addOrUpdateProfile", "Error writing document", e)
            }
    }

    suspend fun getProfileByEmail(email: String): RemoteProfile? = suspendCoroutine { continuation ->
        db.collection("profiles")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val profile = document.toObject(RemoteProfile::class.java)
                    continuation.resume(profile)
                } else {
                    // If no document is found, return null
                    continuation.resume(null)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firebase getProfileByEmail", "Error fetching document", e)
                continuation.resume(null)
            }
    }
    suspend fun getProfileById(id: String): RemoteProfile? {
        return try {
            val documents = db.collection("profiles")
                .whereEqualTo("studentId", id)
                .get()
                .await()  // Using the Kotlin coroutines extension for Firestore
            if (documents.isEmpty) {
                null
            } else {
                documents.documents[0].toObject(RemoteProfile::class.java)
            }
        } catch (e: Exception) {
            Log.w("Firebase getProfileById", "Error fetching document", e)
            null
        }
    }
    suspend fun updateEnrolledCoursesByEmail(email: String, newCourses: String) {
        // Find the profile by email
        db.collection("profiles")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val docId = document.id

                    // Update the enrolledCourses field
                    db.collection("profiles").document(docId)
                        .update("enrolledCourses", newCourses)
                        .addOnSuccessListener {
                            Log.d("FirebaseRepository", "Successfully updated enrolledCourses!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FirebaseRepository", "Error updating enrolledCourses", e)
                        }
                } else {
                    Log.d("FirebaseRepository", "No profile found with this email")
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error fetching profile by email", e)
            }
    }
    suspend fun updatePictureByEmail(email: String, pic: String) {
        // Find the profile by email
        db.collection("profiles")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val docId = document.id

                    // Update the enrolledCourses field
                    db.collection("profiles").document(docId)
                        .update("profilePicture", pic)
                        .addOnSuccessListener {
                            Log.d("FirebaseRepository", "Successfully updated Picture!")
                        }
                        .addOnFailureListener { e ->
                            Log.w("FirebaseRepository", "Error updating Picture", e)
                        }
                } else {
                    Log.d("FirebaseRepository", "No profile found with this email")
                }
            }
            .addOnFailureListener { e ->
                Log.w("FirebaseRepository", "Error fetching profile by email", e)
            }
    }

    suspend fun fetchProfilesByName(searchQuery: String): List<RemoteProfile> {
        val trimmedQuery = searchQuery.trim()
        val profileList = mutableListOf<RemoteProfile>()

        // Query to find profiles where the name contains the search query
        val querySnapshot = db.collection("profiles")
            .whereGreaterThanOrEqualTo("name", trimmedQuery)
            .whereLessThanOrEqualTo("name", "${trimmedQuery}\uF7FF") // Using Unicode to get all names starting with `trimmedQuery`
            .get()
            .await() // Using Kotlin coroutine to wait for the query to complete

        for (document in querySnapshot.documents) {
            val profile = document.toObject(RemoteProfile::class.java)
            profile?.let { profileList.add(it) }
        }

        return profileList
    }

    suspend fun fetchProfilesById(searchQuery: String): List<RemoteProfile> {

        val trimmedQuery = searchQuery.trim()
        val profileList = mutableListOf<RemoteProfile>()

        // Query to find profiles where the studentId contains the search query
        val querySnapshot = db.collection("profiles")
            .whereGreaterThanOrEqualTo("studentId", trimmedQuery)
            .whereLessThanOrEqualTo("studentId", "${trimmedQuery}\uF7FF") // Using Unicode to get all IDs starting with `trimmedQuery`
            .get()
            .await() // Using Kotlin coroutine to wait for the query to complete

        for (document in querySnapshot.documents) {
            val profile = document.toObject(RemoteProfile::class.java)
            profile?.let { profileList.add(it) }
        }

        return profileList
    }

    suspend fun fetchProfilesByCourse(searchQuery: String): List<RemoteProfile> {

        val trimmedQuery = searchQuery.trim()
        val profileList = mutableListOf<RemoteProfile>()

        // Query to find profiles where the enrolledCourses contains the search query
        val querySnapshot = db.collection("profiles")
            .whereGreaterThanOrEqualTo("enrolledCourses", trimmedQuery)
            .whereLessThanOrEqualTo("enrolledCourses", "${trimmedQuery}\uF7FF")
            .get()
            .await() // Using Kotlin coroutine to wait for the query to complete

        for (document in querySnapshot.documents) {
            val profile = document.toObject(RemoteProfile::class.java)
            profile?.let { profileList.add(it) }
        }

        return profileList
    }


    suspend fun sendFriendRequest(from: String, to: String) {
        try {
            // Retrieve the profiles based on studentId
            val fromProfile = getProfileById(from)
            val toProfile = getProfileById(to)

            if (fromProfile != null && toProfile != null) {
                // Check if the request already exists
                if (!toProfile.friendRequests.contains(fromProfile.studentId)) {
                    // Update the friendRequests of the 'toProfile'
                    val prevReq = toProfile.friendRequests
                    toProfile.friendRequests = "$prevReq,$from"
                    addOrUpdateProfile(toProfile) // Ensure this function updates the profile in Firestore

                    // Optionally, you can also update the 'fromProfile' to reflect the request was sent
                    // fromProfile.sentRequests.add(toProfile.studentId)
                    // updateProfile(fromProfile)

                    Log.d("sendFriendRequest", "Friend request sent from ${fromProfile.studentId} to ${toProfile.studentId}")
                } else {
                    Log.d("sendFriendRequest", "Friend request already exists from ${fromProfile.studentId} to ${toProfile.studentId}")
                }
            } else {
                Log.e("sendFriendRequest", "Profile not found for studentId: $from or $to")
            }
        } catch (e: Exception) {
            // Handle errors
            Log.e("sendFriendRequest", "Error sending friend request", e)
        }
    }
    suspend fun cancelRequest(from: String, to: String) {
        try {
            // Query the profile where 'studentId' matches the 'to' studentId
            val querySnapshot = db.collection("profiles")
                .whereEqualTo("studentId", to)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                // Get the first matching document
                val document = querySnapshot.documents[0]
                val profileData = document.toObject(RemoteProfile::class.java)

                if (profileData != null) {
                    // Remove the friend request
                    removeFriendRequest(profileData, from)

                    // Update the profile in Firebase
                    db.collection("profiles").document(document.id).set(profileData).await()
                } else {
                    Log.w("Firebase", "Profile data is null for studentId: $to")
                }
            } else {
                Log.w("Firebase", "No profile found for studentId: $to")
            }
        } catch (e: Exception) {
            Log.e("cancelRequest", "Error canceling friend request", e)
        }
    }
    suspend fun updateFriend(me: String, friend: String, add: Boolean = true) {
        val db = FirebaseFirestore.getInstance()

        try {
            // Query profile for 'me'
            val meProfileQuery = db.collection("profiles")
                .whereEqualTo("studentId", me)
                .get()
                .await()

            // Query profile for 'friend'
            val friendProfileQuery = db.collection("profiles")
                .whereEqualTo("studentId", friend)
                .get()
                .await()

            if (!meProfileQuery.isEmpty && !friendProfileQuery.isEmpty) {
                val meProfileDoc = meProfileQuery.documents[0]
                val friendProfileDoc = friendProfileQuery.documents[0]

                val meProfileData = meProfileDoc.toObject(RemoteProfile::class.java)
                val friendProfileData = friendProfileDoc.toObject(RemoteProfile::class.java)

                if (meProfileData != null && friendProfileData != null) {
                    if (add) {
                        addFriendToList(meProfileData, friend)
                        addFriendToList(friendProfileData, meProfileData.studentId)
                    } else {
                        removeFriendFromList(meProfileData, friend)
                        removeFriendFromList(friendProfileData, meProfileData.studentId)
                    }

                    // Update both profiles in Firebase
                    db.collection("profiles").document(meProfileDoc.id).set(meProfileData).await()
                    db.collection("profiles").document(friendProfileDoc.id).set(friendProfileData).await()
                } else {
                    Log.w("Firebase", "Profile data is null for me: $me or friend: $friend")
                }
            } else {
                Log.w("Firebase", "No profiles found for me: $me or friend: $friend")
            }
        } catch (e: Exception) {
            Log.e("updateFriend", "Error updating friends", e)
        }
    }

    private fun addFriendToList(profileData: RemoteProfile, friend: String){
//        println("add frine to list ${profileData.name} ${profileData.friendRequests}")
        if(!profileData.addedFriends.contains(friend)) {
            profileData.addedFriends = "${profileData.addedFriends},$friend"
        }
        val temp = stringToSet(profileData.friendRequests)
        var tempReq: String =""
        if (temp.isNotEmpty()){
            temp.remove(friend.toInt())
            temp.forEachIndexed { i, s ->
                tempReq = if (i==0){
                    s.toString()
                } else{
                    "$tempReq,$s"
                }

            }
        }
        profileData.friendRequests = tempReq
    }
    private fun removeFriendFromList(profileData: RemoteProfile, friend: String){
        val temp = stringToSet(profileData.addedFriends)
        var tempFriend =""
        if (temp.isNotEmpty()){
            temp.remove(friend.toInt())
            temp.forEachIndexed { i, s ->
                tempFriend = if (i == 0){
                    s.toString()
                } else{
                    "$tempFriend,$s"
                }

            }
        }
        profileData.addedFriends = tempFriend
    }

    private fun removeFriendRequest(profileData: RemoteProfile, friend: String){
        if (profileData.friendRequests != "" && profileData.friendRequests.contains(friend)){
//            println("FRIEND IS HERE in remote$friend ${profileData.friendRequests}")
            val temp = profileData.friendRequests.split(",")
            var tempReq = ""
            temp.forEachIndexed { _, s ->
                if(s != friend && s!= "") tempReq = "$tempReq,$s"
            }
            profileData.friendRequests = tempReq
        }
    }

    private fun stringToSet(st: String): MutableSet<Int>{
        val t = st.split(",")
        val temp = mutableSetOf<Int>()
        t.forEach{ string ->
            if(string != "") temp.add(string.toInt())
        }
        return temp
    }



}