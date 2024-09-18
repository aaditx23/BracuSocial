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

        val trimmedQuery = searchQuery.trim().lowercase()
        val profileList = mutableListOf<RemoteProfile>()

        // Query to find profiles where the enrolledCourses contains the search query
        val querySnapshot = db.collection("profiles")
            .whereArrayContains("enrolledCourses", trimmedQuery)
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
                    toProfile.friendRequests = "${toProfile.friendRequests},$from"
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



}