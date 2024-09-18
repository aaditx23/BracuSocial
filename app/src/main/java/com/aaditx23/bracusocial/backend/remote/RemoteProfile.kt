package com.aaditx23.bracusocial.backend.remote

data class RemoteProfile(
    var studentId: String = "",
    var name: String = "",
    var email: String = "",
    var addedFriends: String = "",
    var enrolledCourses: String = "",
    var friendRequests: String = "",
    var profilePicture: String = ""
)