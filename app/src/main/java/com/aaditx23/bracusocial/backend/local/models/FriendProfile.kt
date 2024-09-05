package com.aaditx23.bracusocial.backend.local.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class FriendProfile: RealmObject {
    @PrimaryKey
    var studentId: String = ""
    var studentName: String = ""
    var enrolledCourses: String = ""
    var addedFriends: String = ""
    var profilePicture: String = ""
    var email: String = ""
}