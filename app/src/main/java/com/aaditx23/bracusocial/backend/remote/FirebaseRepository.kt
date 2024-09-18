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

}